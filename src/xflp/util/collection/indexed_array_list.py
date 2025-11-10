"""
Indexed array list implementation for Python.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Generic, TypeVar, Optional, List
from .indexable import Indexable

T = TypeVar('T', bound=Indexable)


class IndexedArrayList(Generic[T]):
    """
    Indexed list where:
    - Entries have the index of their position in the list
    - Adding or Removing will not shift entry positions
    - Reuses empty slots

    This provides O(1) access by index and maintains stable indices.
    """

    def __init__(self):
        """Initialize an empty indexed array list."""
        self._data: List[Optional[T]] = []
        self._free_indices: List[int] = []
        self._length = 0  # Number of non-null elements
        self._last_used_index = 0

    def add(self, item: T) -> bool:
        """
        Add an item to the list.

        Reuses free indices if available, otherwise appends.
        Sets the item's index to its position.

        Args:
            item: The item to add

        Returns:
            True if successfully added
        """
        self._length += 1

        if self._free_indices:
            # Reuse a free index
            insert_pos = self._free_indices.pop()
            item.set_idx(insert_pos)
            self._data[insert_pos] = item
            return True

        # Append to end
        item.set_idx(len(self._data))
        self._data.append(item)
        if len(self._data) > self._last_used_index:
            self._last_used_index = len(self._data)
        return True

    def set(self, index: int, item: T) -> Optional[T]:
        """
        Set an item at a specific index.

        Expands the list if necessary.

        Args:
            index: The index to set
            item: The item to set

        Returns:
            The previous item at that index, or None
        """
        if index >= len(self._data):
            # Expand the list
            self._expand_to(index + 1)
            old_item = None
        else:
            old_item = self._data[index]

        item.set_idx(index)
        self._data[index] = item

        if index > self._last_used_index:
            self._last_used_index = index

        return old_item

    def get(self, index: int) -> Optional[T]:
        """
        Get an item by index.

        Args:
            index: The index to retrieve

        Returns:
            The item at that index, or None
        """
        if 0 <= index < len(self._data):
            return self._data[index]
        return None

    def remove(self, index: int) -> Optional[T]:
        """
        Remove an item by index.

        The slot becomes available for reuse.

        Args:
            index: The index to remove

        Returns:
            The removed item, or None
        """
        if index < 0 or index >= len(self._data):
            return None

        item = self._data[index]
        if item is None:
            return None

        self._length -= 1
        item.set_idx(-1)
        self._data[index] = None
        self._free_indices.append(index)

        # Update last used index if we removed from the end
        if index >= self._last_used_index:
            for i in range(self._last_used_index, -1, -1):
                if self._data[i] is not None:
                    self._last_used_index = i
                    break
            else:
                self._last_used_index = 0

        return item

    def remove_item(self, item: T) -> bool:
        """
        Remove an item by value (using its index).

        Args:
            item: The item to remove

        Returns:
            True if removed, False otherwise
        """
        idx = item.get_idx()
        if 0 <= idx < len(self._data) and self._data[idx] is item:
            self.remove(idx)
            return True
        return False

    def __len__(self) -> int:
        """Return the number of non-null elements."""
        return self._length

    def __getitem__(self, index: int) -> Optional[T]:
        """Get an item by index (bracket notation)."""
        return self.get(index)

    def __iter__(self):
        """Iterate over all items (including None slots)."""
        return iter(self._data)

    def size(self) -> int:
        """Return the number of non-null elements."""
        return self._length

    def length(self) -> int:
        """Return the total capacity (including null slots)."""
        return len(self._data)

    def get_last_used_index(self) -> int:
        """Return the highest index that has been used."""
        return self._last_used_index

    def clear(self) -> None:
        """Clear the list."""
        self._data.clear()
        self._free_indices.clear()
        self._length = 0
        self._last_used_index = 0

    def _expand_to(self, size: int) -> None:
        """Expand the internal list to at least the given size."""
        while len(self._data) < size:
            self._data.append(None)
