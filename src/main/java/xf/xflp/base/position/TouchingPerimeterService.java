package xf.xflp.base.position;

import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;

import java.util.List;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class TouchingPerimeterService {

    public static float getTouchingPerimeter(
            Container container,
            PositionCandidate candidate,
            int itemTouchValue,
            boolean considerWalls,
            boolean considerBaseFloor) {
        Item item = candidate.item();
        Position pos = candidate.position();

        int value = 0;
        int w = item.w;
        int l = item.l;
        int h = item.h;
        if(candidate.isRotated()) {
            w = item.l;
            l = item.w;
        }

        int posX = pos.x();
        int posY = pos.y();
        int posZ = pos.z();
        int xw = posX + w;
        int yl = posY + l;
        int zh = posZ + h;

        List<Integer> positionCoords, itemCoords;

        // x-Achse
        {
            if(posX == 0 && considerWalls)
                value += h * l;
            if(xw == container.getWidth() && considerWalls)
                value += h * l;

            positionCoords = container.getBaseData().getXMap().get(posX);
            itemCoords = container.getBaseData().getXMap().get(xw);

            value += touchValueX(positionCoords, container, posX, xw, posY, yl, posZ, zh, itemTouchValue);
            value += touchValueX(itemCoords, container, posX, xw, posY, yl, posZ, zh, itemTouchValue);
        }

        // Y-Achse
        {
            if(posY == 0 && considerWalls)
                value += h * w;
            if(yl == container.getLength() && considerWalls)
                value += h * w;

            positionCoords = container.getBaseData().getYMap().get(posY);
            itemCoords = container.getBaseData().getYMap().get(yl);

            value += touchValueY(positionCoords, container, posX, xw, posY, yl, posZ, zh, itemTouchValue);
            value += touchValueY(itemCoords, container, posX, xw, posY, yl, posZ, zh, itemTouchValue);
        }

        // Z-Achse
        {
            if(posZ == 0 && considerBaseFloor)
                value += w * l;
            if(zh == container.getHeight() && considerWalls)
                value += w * l;

            positionCoords = container.getBaseData().getZMap().get(posZ);
            itemCoords = container.getBaseData().getZMap().get(zh);

            value += touchValueZ(positionCoords, container, posX, xw, posY, yl, posZ, zh, itemTouchValue);
            value += touchValueZ(itemCoords, container, posX, xw, posY, yl, posZ, zh, itemTouchValue);
        }

        return value;
    }

    private static int touchValueX(List<Integer> itemIndices, Container container,
                                    int posX, int xw, int posY, int yl, int posZ, int zh,
                                    int itemTouchValue) {
        if (itemIndices == null) return 0;
        int val = 0;
        for (int j = itemIndices.size() - 1; j >= 0; j--) {
            Item i = container.getItems().get(itemIndices.get(j));
            if (i.xw == posX || i.x == xw) {
                if (i.y > yl || i.yl < posY) continue;
                if (i.z > zh || i.zh < posZ) continue;
                int yLength = Math.min(yl, i.yl) - Math.max(i.y, posY);
                int zLength = Math.min(zh, i.zh) - Math.max(i.z, posZ);
                val += yLength * zLength * itemTouchValue;
            }
        }
        return val;
    }

    private static int touchValueY(List<Integer> itemIndices, Container container,
                                    int posX, int xw, int posY, int yl, int posZ, int zh,
                                    int itemTouchValue) {
        if (itemIndices == null) return 0;
        int val = 0;
        for (int j = itemIndices.size() - 1; j >= 0; j--) {
            Item i = container.getItems().get(itemIndices.get(j));
            if (i.yl == posY || i.y == yl) {
                if (i.x > xw || i.xw < posX) continue;
                if (i.z > zh || i.zh < posZ) continue;
                int xLength = Math.min(xw, i.xw) - Math.max(i.x, posX);
                int zLength = Math.min(zh, i.zh) - Math.max(i.z, posZ);
                val += xLength * zLength * itemTouchValue;
            }
        }
        return val;
    }

    private static int touchValueZ(List<Integer> itemIndices, Container container,
                                    int posX, int xw, int posY, int yl, int posZ, int zh,
                                    int itemTouchValue) {
        if (itemIndices == null) return 0;
        int val = 0;
        for (int j = itemIndices.size() - 1; j >= 0; j--) {
            Item i = container.getItems().get(itemIndices.get(j));
            if (i.zh == posZ || i.z == zh) {
                if (i.y > yl || i.yl < posY) continue;
                if (i.x > xw || i.xw < posX) continue;
                int yLength = Math.min(yl, i.yl) - Math.max(i.y, posY);
                int xLength = Math.min(xw, i.xw) - Math.max(i.x, posX);
                val += yLength * xLength * itemTouchValue;
            }
        }
        return val;
    }
}
