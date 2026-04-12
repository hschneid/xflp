package xf.xflp.base.container;

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
 *
 * @author hschneid
 *
 */
public class ZItemGraphEntry {

    public final ItemPlacement item;
    public final List<ItemPlacement> lowerItemList;
    public final List<Float> cutRatioList;
    public final Object[] itemRatioArr;

    public ZItemGraphEntry(ItemPlacement item, List<ItemPlacement> lowerItemList) {
        this.item = item;
        this.cutRatioList = new ArrayList<>();
        this.lowerItemList = lowerItemList;
        this.itemRatioArr = new Object[]{this.lowerItemList, this.cutRatioList};

        update();
    }

    public final void update() {
        float[] bCuts = new float[lowerItemList.size()];
        float sum = 0;
        for (int i = 0; i < lowerItemList.size(); i++) {
            bCuts[i] = Tools.getCutRatio(item, lowerItemList.get(i));
            sum += bCuts[i];
        }
        // Anpassen der Anteile auf 100%
        for (int i = 0; i < lowerItemList.size(); i++)
            cutRatioList.add(bCuts[i] * (1f / sum));
    }

}
