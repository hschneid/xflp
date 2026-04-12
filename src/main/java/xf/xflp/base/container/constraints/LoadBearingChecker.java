package xf.xflp.base.container.constraints;

import util.collection.IndexedArrayList;
import xf.xflp.base.container.ContainerBase;
import xf.xflp.base.item.ItemPlacement;
import xf.xflp.base.item.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 */
public class LoadBearingChecker {

    public void update(ContainerBase container, List<ItemPlacement> initialItems) {
        float[] bearingWeights = new float[((IndexedArrayList<ItemPlacement>)container.getItems()).getLastUsedIndex()];

        // Collect the bearing weight per item - top-down
        List<ItemPlacement> floorItems = collectBearingWeight(initialItems, bearingWeights, container);
        // Set bearing capacity per item - bottom-up
        updateBearingCapacities(floorItems, bearingWeights, container);
    }

    private List<ItemPlacement> collectBearingWeight(List<ItemPlacement> initialItems, float[] bearingWeights, ContainerBase container) {
        BearingWeightQueue queue = new BearingWeightQueue(((IndexedArrayList<ItemPlacement>)container.getItems()).getLastUsedIndex());

        // Add all initial items to queue
        for (ItemPlacement initialItem : initialItems) {
            queue.add(initialItem, container.getBaseData().getZGraph());
        }

        List<ItemPlacement> floorItems = new ArrayList<>();
        while(queue.hasMore()) {
            ItemPlacement item = container.getItems().get(queue.getNext());
            queue.setProcessed(item.index);

            // Fetch lower items of item
            List<ItemPlacement> lowerItems = container.getBaseData().getZGraph().getItemsBelow(item);
            float[] weightRatios = new float[lowerItems.size()];
            for (int i = lowerItems.size() - 1; i >= 0; i--) {
                ItemPlacement lowerItem = lowerItems.get(i);
                // Calculate the share of bearing weights - item.weight to lower items (must be normed to 1)
                weightRatios[i] = Tools.getCutRatio(item, lowerItem);
                // Add lower items to queue
                queue.add(lowerItem, container.getBaseData().getZGraph());
            }
            normWeightRatios(weightRatios);

            // Add bearing weight to lower Item
            for (int i = 0; i < weightRatios.length; i++) {
                bearingWeights[lowerItems.get(i).index] += weightRatios[i] * (item.getItem().getWeight() + bearingWeights[item.index]);
            }

            // Check for floor item
            if(item.z == 0) {
                floorItems.add(item);
            }
        }

        return floorItems;
    }

    private void normWeightRatios(float[] arr) {
        // sum
        float sum = 0;
        for (int i = arr.length - 1; i >= 0; i--) sum += arr[i];
        // divide
        float sumRest = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            arr[i] /= sum;
            sumRest += arr[i];
        }
        // Add rest
        float avg = (1f - sumRest) / arr.length;
        for (int i = arr.length - 1; i >= 0; i--) arr[i] += avg;
    }

    private void updateBearingCapacities(List<ItemPlacement> floorItems, float[] bearingWeights, ContainerBase container) {
        final List<ItemPlacement> currentItems = new ArrayList<>(floorItems);
        final List<ItemPlacement> nextItems = new ArrayList<>();

        while(!currentItems.isEmpty()) {

            for (int i = currentItems.size() - 1; i >= 0; i--) {
                ItemPlacement currentItem = currentItems.get(i);

                List<ItemPlacement> lowerItems = container.getBaseData().getZGraph().getItemsBelow(currentItem);
                float lowerBearingCapacity = getLowerBearingCapacity(container, currentItem, lowerItems);
                float ownBearingCapacity = currentItem.getItem().getStackingWeightLimit() - bearingWeights[currentItem.index];

                // the bearing capacity of current item is the minimum of
                // the sum of lower bearing capacities and the own (bearing capacity - bearingWeight)
                float currentBearingCapacity = (lowerItems.isEmpty()) ?
                        ownBearingCapacity:
                        Math.min(lowerBearingCapacity, ownBearingCapacity);
                container.getBearingCapacities().put(currentItem.index, currentBearingCapacity);

                // Add next items (upper items)
                List<ItemPlacement> upperItems = container.getBaseData().getZGraph().getItemsAbove(currentItem);
                for (int j = upperItems.size() - 1; j >= 0; j--) {
                    nextItems.add(upperItems.get(j));
                }
            }

            currentItems.clear();
            currentItems.addAll(nextItems);
            nextItems.clear();
        }
    }

    private float getLowerBearingCapacity(ContainerBase container, ItemPlacement currentItem, List<ItemPlacement> lowerItems) {
        float lowerBearingCapacity = Float.MAX_VALUE;
        for (int j = lowerItems.size() - 1; j >= 0; j--) {
            float reciprocalAreaRatio = 1f / Tools.getCutRatio(currentItem, lowerItems.get(j));

            lowerBearingCapacity = Math.min(
                    lowerBearingCapacity,
                    container.getBearingCapacities().getOrDefault(lowerItems.get(j).index, 0f) * reciprocalAreaRatio
            );
        }
        return lowerBearingCapacity;
    }
}
