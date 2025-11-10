"""
Load bearing capacity checker and updater.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import List, TYPE_CHECKING

from .bearing_weight_queue import BearingWeightQueue

if TYPE_CHECKING:
    from ..container_base import ContainerBase
    from ...item.item import Item
    from ....util.collection.indexed_array_list import IndexedArrayList


class LoadBearingChecker:
    """
    Updates and manages load bearing capacities for stacked items.
    
    Calculates how much additional weight each item can support based on:
    - Its own stacking weight limit
    - The bearing capacity of items below it
    - The weight it's already bearing
    """
    
    def update(self, container: 'ContainerBase', initial_items: List['Item']) -> None:
        """
        Update bearing capacities for items in the container.
        
        Args:
            container: The container with items
            initial_items: Items that need capacity updates
        """
        from ....util.collection.indexed_array_list import IndexedArrayList
        
        item_list = container.get_items()
        if isinstance(item_list, IndexedArrayList):
            last_used = item_list.get_last_used_index()
        else:
            last_used = len(item_list)
        
        bearing_weights = [0.0] * (last_used + 1)
        
        # Collect the bearing weight per item - top-down
        floor_items = self._collect_bearing_weight(initial_items, bearing_weights, container)
        
        # Set bearing capacity per item - bottom-up
        self._update_bearing_capacities(floor_items, bearing_weights, container)
    
    def _collect_bearing_weight(
        self, 
        initial_items: List['Item'], 
        bearing_weights: List[float], 
        container: 'ContainerBase'
    ) -> List['Item']:
        """
        Collect bearing weights top-down using a queue.
        
        Args:
            initial_items: Starting items
            bearing_weights: Array to store bearing weights
            container: The container
            
        Returns:
            List of floor items
        """
        from ....util.collection.indexed_array_list import IndexedArrayList
        from ...item.tools import get_cut_ratio
        
        item_list = container.get_items()
        if isinstance(item_list, IndexedArrayList):
            last_used = item_list.get_last_used_index()
        else:
            last_used = len(item_list)
        
        queue = BearingWeightQueue(last_used + 1)
        
        # Add all initial items to queue
        for initial_item in initial_items:
            queue.add(initial_item, container.get_base_data().get_z_graph())
        
        floor_items: List['Item'] = []
        
        while queue.has_more():
            item_idx = queue.get_next()
            item = container.get_items()[item_idx]
            queue.set_processed(item.index)
            
            # Fetch lower items of item
            lower_items = container.get_base_data().get_z_graph().get_items_below(item)
            
            # Calculate weight ratios
            weight_ratios = []
            for lower_item in lower_items:
                # Calculate the share of bearing weights
                ratio = get_cut_ratio(item, lower_item)
                weight_ratios.append(ratio)
                # Add lower items to queue
                queue.add(lower_item, container.get_base_data().get_z_graph())
            
            self._norm_weight_ratios(weight_ratios)
            
            # Add bearing weight to lower items
            for i, lower_item in enumerate(lower_items):
                if i < len(weight_ratios):
                    bearing_weights[lower_item.index] += (
                        weight_ratios[i] * (item.weight + bearing_weights[item.index])
                    )
            
            # Check for floor item
            if item.z == 0:
                floor_items.append(item)
        
        return floor_items
    
    def _norm_weight_ratios(self, arr: List[float]) -> None:
        """
        Normalize weight ratios to sum to 1.0.
        
        Args:
            arr: Array of ratios to normalize in-place
        """
        if not arr:
            return
        
        # Calculate sum
        total = sum(arr)
        
        if total == 0:
            return
        
        # Divide by sum and track remainder
        sum_rest = 0.0
        for i in range(len(arr)):
            arr[i] /= total
            sum_rest += arr[i]
        
        # Add remainder evenly
        avg = (1.0 - sum_rest) / len(arr)
        for i in range(len(arr)):
            arr[i] += avg
    
    def _update_bearing_capacities(
        self, 
        floor_items: List['Item'], 
        bearing_weights: List[float], 
        container: 'ContainerBase'
    ) -> None:
        """
        Update bearing capacities bottom-up from floor items.
        
        Args:
            floor_items: Items on the floor
            bearing_weights: Bearing weights per item
            container: The container
        """
        from ...item.tools import get_cut_ratio
        
        current_items = list(floor_items)
        next_items: List['Item'] = []
        
        while current_items:
            for current_item in current_items:
                lower_items = container.get_base_data().get_z_graph().get_items_below(current_item)
                lower_bearing_capacity = self._get_lower_bearing_capacity(
                    container, current_item, lower_items
                )
                own_bearing_capacity = (
                    current_item.stacking_weight_limit - bearing_weights[current_item.index]
                )
                
                # The bearing capacity of current item is the minimum of
                # the sum of lower bearing capacities and the own capacity
                if len(lower_items) == 0:
                    current_bearing_capacity = own_bearing_capacity
                else:
                    current_bearing_capacity = min(lower_bearing_capacity, own_bearing_capacity)
                
                container.get_bearing_capacities()[current_item.index] = current_bearing_capacity
                
                # Add next items (upper items)
                upper_items = container.get_base_data().get_z_graph().get_items_above(current_item)
                next_items.extend(upper_items)
            
            current_items = next_items
            next_items = []
    
    def _get_lower_bearing_capacity(
        self, 
        container: 'ContainerBase', 
        current_item: 'Item', 
        lower_items: List['Item']
    ) -> float:
        """
        Calculate the effective bearing capacity from lower items.
        
        Args:
            container: The container
            current_item: The item to check
            lower_items: Items below current item
            
        Returns:
            The effective lower bearing capacity
        """
        from ...item.tools import get_cut_ratio
        
        lower_bearing_capacity = float('inf')
        
        for lower_item in lower_items:
            reciprocal_area_ratio = 1.0 / get_cut_ratio(current_item, lower_item)
            
            lower_capacity = container.get_bearing_capacities().get(lower_item.index, 0.0)
            lower_bearing_capacity = min(
                lower_bearing_capacity,
                lower_capacity * reciprocal_area_ratio
            )
        
        return lower_bearing_capacity
