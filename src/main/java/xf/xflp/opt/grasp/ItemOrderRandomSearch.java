package xf.xflp.opt.grasp;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.item.Item;
import xf.xflp.base.monitor.StatusCode;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.Packer;
import xf.xflp.opt.XFLPBase;

import java.util.Arrays;
import java.util.Collections;
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
public class ItemOrderRandomSearch extends XFLPBase {

    private final Packer packer;

    private final Random rand = new Random(1234);
    private int nbrOfIterations = 2000;

    public ItemOrderRandomSearch(Packer packer) {
        this.packer = packer;
    }

    @Override
    public void execute(XFLPModel model) throws XFLPException {
        packer.execute(model);
        model.getStatusManager().fireMessage(StatusCode.RUNNING, "Init Random search "+model.getUnplannedItems().length);

        Item[] bestItems = Arrays.copyOf(model.getItems(), model.getItems().length);
        int bestValue = model.getUnplannedItems().length;
        for (int k = 0; k < nbrOfIterations; k++) {
            Item[] items = Arrays.copyOf(bestItems, bestItems.length);

            // Make random move in search space
            model.setItems(perturb(items));

            // Pack
            packer.execute(model);

            // Check if there are unplanned items
            if (model.getUnplannedItems().length < bestValue) {
                bestItems = Arrays.copyOf(model.getItems(), model.getItems().length);
                model.getStatusManager().fireMessage(StatusCode.RUNNING, "Better " + model.getUnplannedItems().length);
                bestValue = model.getUnplannedItems().length;

                if (model.getUnplannedItems().length == 0) {
                    break;
                }
            }
        }

        // Reset best solution
        model.setItems(bestItems);
        packer.execute(model);
    }

    private Item[] perturb(Item[] items) {
        Collections.shuffle(Arrays.asList(items), rand);

        return items;
    }
}
