package xf.xflp.opt.grasp;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.item.Item;
import xf.xflp.base.monitor.StatusCode;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.XFLPBase;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;

import java.util.Arrays;
import java.util.Random;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public class SingleBinOptimizedPacker extends XFLPBase {

    private OneContainerOneTypePacker packer = new OneContainerOneTypePacker();

    private Random rand = new Random(1234);

    @Override
    public void execute(XFLPModel model) throws XFLPException {
        packer.execute(model);
        model.getStatusManager().fireMessage(StatusCode.RUNNING,"Init "+model.getUnplannedItems().length);

        /*if(model.getUnplannedItems().length > 0) {
            doBestSwap(model);
        }
        if(model.getUnplannedItems().length > 0) {
            doSwapNextLocalSearch(model);
        }
        if(model.getUnplannedItems().length > 0) {
            doSwapLocalSearch(model);
        }*/
        if(model.getUnplannedItems().length > 0) {
            doRelocateLocalSearch(model);
        }
    }

    private void doBestSwap(XFLPModel model) throws XFLPException {
        packer.execute(model);
        model.getStatusManager().fireMessage(StatusCode.RUNNING,"Init Swap Next "+model.getUnplannedItems().length);

        Item[] items = model.getItems();
        int[] bestValue = new int[]{model.getUnplannedItems().length,-1,-1, 0};
        for (int i = 0; i < items.length - 2; i++) {
            // Change item queue
            swap(items, i, i + 1);
            // Pack
            packer.execute(model);

            model.getStatusManager().fireMessage(StatusCode.RUNNING,i + " " + model.getUnplannedItems().length);
            // Check if there are unplanned items
            if (model.getUnplannedItems().length < bestValue[0]) {
                bestValue[0] = model.getUnplannedItems().length;
                bestValue[1] = i;
                bestValue[2] = i + 1;
                bestValue[3] = 1;

                if(model.getUnplannedItems().length == 0) {
                    return;
                }
            }
            // Change back
            swap(items, i, i + 1);
        }

        if (bestValue[3] == 1) {
            swap(items, bestValue[1], bestValue[2]);
        }
    }

    private void doSwapNextLocalSearch(XFLPModel model) throws XFLPException {
        packer.execute(model);
        model.getStatusManager().fireMessage(StatusCode.RUNNING,"Init Swap Next LS "+model.getUnplannedItems().length);

        Item[] items = model.getItems();
        int[] bestValue = new int[]{model.getUnplannedItems().length,-1,-1, 1};
        while(bestValue[3] == 1) {
            bestValue[3] = 0;

            for (int i = 0; i < items.length - 2; i++) {
                // Change item queue
                swap(items, i, i + 1);
                // Pack
                packer.execute(model);
                // Check if there are unplanned items
                if (model.getUnplannedItems().length < bestValue[0]) {
                    setBestMove(model, bestValue, i, i + 1);
                }
                // Change back
                swap(items, i, i + 1);
            }

            if (bestValue[3] == 1) {
                swap(items, bestValue[1], bestValue[2]);
            }
        }
    }

    private void doSwapLocalSearch(XFLPModel model) throws XFLPException {
        packer.execute(model);
        model.getStatusManager().fireMessage(StatusCode.RUNNING,"Init Swap "+model.getUnplannedItems().length);

        Item[] items = model.getItems();
        int[] bestValue = new int[]{model.getUnplannedItems().length,-1,-1, 1};
        while(bestValue[3] == 1) {
            bestValue[3] = 0;

            for (int i = 0; i < items.length - 2; i++) {
                for (int j = i + 1; j < items.length - 1; j++) {
                    // Change item queue
                    swap(items, i, j);
                    // Pack
                    packer.execute(model);
                    // Check if there are unplanned items
                    if (model.getUnplannedItems().length < bestValue[0]) {
                        setBestMove(model, bestValue, i, j);

                        if (model.getUnplannedItems().length == 0) {
                            return;
                        }
                    }
                    // Change back
                    swap(items, i, j);
                }
            }

            if (bestValue[3] == 1) {
                swap(items, bestValue[1], bestValue[2]);
            }
        }
    }

    private void doRelocateLocalSearch(XFLPModel model) throws XFLPException {
        packer.execute(model);
        model.getStatusManager().fireMessage(StatusCode.RUNNING,"Init RelocateLS "+model.getUnplannedItems().length);

        Item[] items = model.getItems();
        Item[] bestItems = Arrays.copyOf(items, items.length);
        int[] bestValue = new int[]{model.getUnplannedItems().length,-1,-1, 1};
        for (int k = 0; k < 10; k++) {

            model.getStatusManager().fireMessage(StatusCode.RUNNING,"iter "+k);
            bestValue[3] = 1;
            while (bestValue[3] == 1) {
                bestValue[3] = 0;

                for (int i = 0; i < items.length - 1; i++) {
                    for (int j = 0; j < items.length; j++) {
                        if (j == i + 1 || j == i)
                            continue;
                        // Change item queue
                        move(items, i, j);
                        // Pack
                        packer.execute(model);

                        model.getStatusManager().fireMessage(StatusCode.RUNNING,i+" "+j+" "+bestValue[0]);

                        // Check if there are unplanned items
                        if (model.getUnplannedItems().length < bestValue[0]) {
                            setBestMove(model, bestValue, i, j);

                            bestItems = Arrays.copyOf(items, items.length);

                            if (model.getUnplannedItems().length == 0) {
                                return;
                            }
                        }
                        // Change back
                        if (i < j) {
                            move(items, j - 1, i);
                        } else {
                            move(items, j, i + 1);
                        }
                    }
                }

                if (bestValue[3] == 1) {
                    swap(items, bestValue[1], bestValue[2]);
                }
            }

            // Make random move in search space
            perturb(items);
        }

        // Reset best solution
        model.setItems(bestItems);
        packer.execute(model);
    }

    private void setBestMove(XFLPModel model, int[] bestValue, int value1, int value2) {
        bestValue[0] = model.getUnplannedItems().length;
        bestValue[1] = value1;
        bestValue[2] = value2;
        bestValue[3] = 1;
        model.getStatusManager().fireMessage(StatusCode.RUNNING,"Better " + Arrays.toString(bestValue));
    }

    private void perturb(Item[] items) {
        for (int n = 0; n < 4; n++) {
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

    /**
     * Exchange the position of two items at given positions
     */
    private void move(Item[] items, int indexSrc, int indexDst) {
        Item src = items[indexSrc];
        if(indexSrc < indexDst) {
            System.arraycopy(items, indexSrc + 1, items, indexSrc, indexDst - indexSrc);
            items[indexDst - 1] = src;
        } else {
            System.arraycopy(items, indexDst, items, indexDst + 1, indexSrc - indexDst);
            items[indexDst] = src;
        }
    }
}
