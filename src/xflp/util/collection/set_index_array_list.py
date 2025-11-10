"""
Set-index array list implementation for Python.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Generic, TypeVar, Optional, List

T = TypeVar('T')


class SetIndexArrayList(Generic[T]):
    """
    Indexed list where:
    - Elements can only be set at specific indices (not appended without index)
    - Adding or Removing will not shift entry positions
    - Elements don't need to implement Indexable

    Similar to IndexedArrayList but simpler - just for positional storage.
    """

    def __init__(self):
        """Initialize an empty set-index array list."""
        self._data: List[Optional[T]] = []
        self._length = 0  # Number of non-null elements

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
            self._length += 1
        else:
            old_item = self._data[index]
            if old_item is None and item is not None:
                self._length += 1

        self._data[index] = item
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
        Remove an item by index (sets it to None).

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
        self._data[index] = None
        return item

    def __len__(self) -> int:
        """Return the number of non-null elements."""
        return self._length

    def __getitem__(self, index: int) -> Optional[T]:
        """Get an item by index (bracket notation)."""
        return self.get(index)

    def __setitem__(self, index: int, item: T) -> None:
        """Set an item by index (bracket notation)."""
        self.set(index, item)

    def __iter__(self):
        """Iterate over all items (including None slots)."""
        return iter(self._data)

    def length(self) -> int:
        """Return the number of non-null elements."""
        return self._length

    def size(self) -> int:
        """Return the total capacity (including null slots)."""
        return len(self._data)

    def clear(self) -> None:
        """Clear the list."""
        self._data.clear()
        self._length = 0

    def _expand_to(self, size: int) -> None:
        """Expand the internal list to at least the given size."""
        while len(self._data) < size:
            self._data.append(None)
