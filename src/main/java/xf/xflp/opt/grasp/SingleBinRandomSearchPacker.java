package xf.xflp.opt.grasp;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Item;
import xf.xflp.opt.XFLPBase;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;

import java.util.Arrays;
import java.util.Random;

/**
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public class SingleBinRandomSearchPacker extends XFLPBase {

    private OneContainerOneTypePacker packer = new OneContainerOneTypePacker();

    private Random rand = new Random(1234);

    @Override
    public void execute(XFLPModel model) {
        packer.execute(model);
        System.out.println("Init "+model.getUnplannedItems().length);

        if(model.getUnplannedItems().length > 0) {
            search(model, 2000, 0.1f);
        }
        if(model.getUnplannedItems().length > 0) {
            search(model, 1000, 0.05f);
        }
        if(model.getUnplannedItems().length > 0) {
            search(model, 1000, 0.01f);
        }
    }

    private void search(XFLPModel model, int K, float p) {
        packer.execute(model);
        System.out.println("Init Random search "+model.getUnplannedItems().length);


        Item[] bestItems = Arrays.copyOf(model.getItems(), model.getItems().length);
        int[] bestValue = new int[]{model.getUnplannedItems().length,-1,-1, 1};
        for (int k = 0; k < K; k++) {
            Item[] items = Arrays.copyOf(bestItems, bestItems.length);

            // Make random move in search space
            perturb(items, p);

            // Pack
            model.setItems(items);
            packer.execute(model);

            // Check if there are unplanned items
            if (model.getUnplannedItems().length < bestValue[0]) {
                bestItems = Arrays.copyOf(model.getItems(), model.getItems().length);
                System.out.println("Better " + model.getUnplannedItems().length);
                bestValue[0] = model.getUnplannedItems().length;
            }
        }

        // Reset best solution
        model.setItems(bestItems);
        packer.execute(model);
    }

    private void perturb(Item[] items, float p) {
        for (int n = 0; n < items.length * p; n++) {
            int i, j;
            do {
                i = rand.nextInt(items.length);
                j = rand.nextInt(items.length);
            } while(i == j);
            swap(items, i, j);
        }
    }

    /**
     * Exchange the position of two items at given positions
     */
    private void swap(Item[] items, int indexA, int indexB) {
        Item b = items[indexB];
        items[indexB] = items[indexA];
        items[indexA] = b;
    }
}
