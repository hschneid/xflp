package xf.xflp.base.position;

import xf.xflp.base.container.Container;
import xf.xflp.base.item.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class MaxWidthAreaService {

    public float getMaxEmptyArea(Container container) {
        List<Position> posList = new ArrayList<>(container.getActivePositions());
        posList.sort(Comparator.comparingInt(o -> o.x));

        // Obere Absch�tzung der Fl�che
        float area = 0;
        for (int i = 1; i < posList.size(); i++) {
            Position prevPos = posList.get(i - 1);
            Position pos = posList.get(i);
            area += (pos.x - prevPos.x) * (container.getLength() - prevPos.y);
        }

        // F�ge die Rest-Fl�che bis zur Wand hinzu
        // Annahme dass es immer am unteren Ende einen geben muss.
        Position lastPos = posList.get(posList.size() - 1);
        area += (container.getWidth() - lastPos.x) * container.getLength();

        return area;
    }
}
