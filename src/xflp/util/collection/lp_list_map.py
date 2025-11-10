"""
List-per-key map implementation for Python.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Dict, List, Optional, Set, TypeVar, Generic

K = TypeVar('K')
E = TypeVar('E')


class LPListMap(Generic[K, E]):
    """
    A map where each key can have multiple values stored in a list.

    This is useful for maintaining multiple items per coordinate or category.
    """

    def __init__(self, initial_size: Optional[int] = None):
        """
        Initialize an empty list map.

        Args:
            initial_size: Optional hint for initial capacity
        """
        self._map: Dict[K, List[E]] = {}

    def put(self, key: K, element: E) -> None:
        """
        Add an element to the list for the given key.

        Args:
            key: The key
            element: The element to add
        """
        if key in self._map:
            self._map[key].append(element)
        else:
            self._map[key] = [element]

    def put_list(self, key: K, elements: List[E]) -> None:
        """
        Add multiple elements to the list for the given key.

        Args:
            key: The key
            elements: The list of elements to add
        """
        if key in self._map and self._map[key] is not None:
            self._map[key].extend(elements)
        else:
            self._map[key] = elements

    def get(self, key: K) -> Optional[List[E]]:
        """
        Get the list of elements for the given key.

        Args:
            key: The key

        Returns:
            The list of elements, or None if key doesn't exist
        """
        return self._map.get(key)

    def remove(self, key: K) -> None:
        """
        Remove all elements for the given key.

        Args:
            key: The key to remove
        """
        if key in self._map:
            del self._map[key]

    def remove_element(self, key: K, element: E) -> None:
        """
        Remove a specific element from the list for the given key.

        Args:
            key: The key
            element: The element to remove
        """
        if key in self._map and element in self._map[key]:
            self._map[key].remove(element)

    def remove_fully(self, key: K, element: E) -> None:
        """
        Remove an element and remove the key if the list becomes empty.

        Args:
            key: The key
            element: The element to remove
        """
        if key in self._map:
            if element in self._map[key]:
                self._map[key].remove(element)
            if not self._map[key]:
                del self._map[key]

    def contains_key(self, key: K) -> bool:
        """
        Check if a key exists in the map.

        Args:
            key: The key to check

        Returns:
            True if the key exists
        """
        return key in self._map

    def key_set(self) -> Set[K]:
        """
        Get the set of all keys.

        Returns:
            Set of keys
        """
        return set(self._map.keys())

    def size(self) -> int:
        """
        Get the number of keys in the map.

        Returns:
            Number of keys
        """
        return len(self._map)

    def clear(self) -> None:
        """Clear the map."""
        self._map.clear()

    def __contains__(self, key: K) -> bool:
        """Check if a key exists (for 'in' operator)."""
        return key in self._map

    def __getitem__(self, key: K) -> Optional[List[E]]:
        """Get elements by key (bracket notation)."""
        return self._map.get(key)
