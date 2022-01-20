package xf.xflp.base.container.constraints;

import xf.xflp.base.container.ContainerBase;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 */
public class LoadBearingChecker {

    public void update(ContainerBase container, List<Item> initialItems) {
        float[] bearingWeights = new float[container.getItems().size()];

        // Collect the bearing weight per item - top-down
        List<Item> floorItems = collectBearingWeight(initialItems, bearingWeights, container);
        // Set bearing capacity per item - bottom-up
        updateBearingCapacities(floorItems, bearingWeights, container);
    }

    private List<Item> collectBearingWeight(List<Item> initialItems, float[] bearingWeights, ContainerBase container) {
        BearingWeightQueue queue = new BearingWeightQueue(container.getItems().size());

        // Add all initial items to queue
        for (Item initialItem : initialItems) {
            queue.add(initialItem, container.getBaseData().getZGraph());
        }

        List<Item> floorItems = new ArrayList<>();
        while(queue.hasMore()) {
            Item item = container.getItems().get(queue.getNext());
            queue.setProcessed(item.index);

            // Fetch lower items of item
            List<Item> lowerItems = container.getBaseData().getZGraph().getItemsBelow(item);
            float[] weightRatios = new float[lowerItems.size()];
            for (int i = lowerItems.size() - 1; i >= 0; i--) {
                Item lowerItem = lowerItems.get(i);
                // Calculate the share of bearing weights - item.weight to lower items (must be normed to 1)
                weightRatios[i] = Tools.getCutRatio(item, lowerItem);
                // Add lower items to queue
                queue.add(lowerItem, container.getBaseData().getZGraph());
            }
            normWeightRatios(weightRatios);

            // Add bearing weight to lower Item
            for (int i = 0; i < weightRatios.length; i++) {
                bearingWeights[lowerItems.get(i).index] += weightRatios[i] * (item.getWeight() + bearingWeights[item.index]);
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

    private void updateBearingCapacities(List<Item> floorItems, float[] bearingWeights, ContainerBase container) {
        final List<Item> currentItems = new ArrayList<>(floorItems);
        final List<Item> nextItems = new ArrayList<>();

        while(currentItems.size() > 0) {

            for (int i = currentItems.size() - 1; i >= 0; i--) {
                Item currentItem = currentItems.get(i);

                List<Item> lowerItems = container.getBaseData().getZGraph().getItemsBelow(currentItem);
                float lowerBearingCapacity = getLowerBearingCapacity(container, currentItem, lowerItems);
                float ownBearingCapacity = currentItem.getStackingWeightLimit() - bearingWeights[currentItem.index];

                // the bearing capacity of current item is the minimum of
                // the sum of lower bearing capacities and the own (bearing capacity - bearingWeight)
                float currentBearingCapacity = (lowerItems.size() == 0) ?
                        ownBearingCapacity:
                        Math.min(lowerBearingCapacity, ownBearingCapacity);
                container.getBearingCapacities().put(currentItem.index, currentBearingCapacity);

                // Add next items (upper items)
                List<Item> upperItems = container.getBaseData().getZGraph().getItemsAbove(currentItem);
                for (int j = upperItems.size() - 1; j >= 0; j--) {
                    nextItems.add(upperItems.get(j));
                }
            }

            currentItems.clear();
            currentItems.addAll(nextItems);
            nextItems.clear();
        }
    }

    private float getLowerBearingCapacity(ContainerBase container, Item currentItem, List<Item> lowerItems) {
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
