package helper

import xf.xflp.base.problem.Container
import xf.xflp.base.problem.Item
import xf.xflp.base.problem.Position
import xf.xflp.base.problem.RotatedPosition

class Helper {

    static int itemIdx = 0;

    static Container getContainer(int width, int length, int height) {
        return new Container(width, length, height, 999999999, 0, 0);
    }

    static Container getContainer(int width, int length, int height, float maxWeight) {
        return new Container(width, length, height, maxWeight, 0, 0);
    }

    static Item getItem(int w, int l, int h, int ww, long wC, int sG) {
        return getItem(w, l, h, ww, wC, sG, Math.max(1,sG));
    }

    static Item getItem(int w, int l, int h, int ww, long wC, int sG, int allowedSG) {
        Set<Integer> set = new HashSet<>();
        set.add(0);

        Item i = new Item();

        i.setExternalIndex(itemIdx);
        i.setOrderIndex(itemIdx);
        i.setLoadingLoc(itemIdx);
        i.setUnLoadingLoc(itemIdx + 1);//manager.getLocationIdx(unloadingLocation),
        i.setW(w);
        i.setL(l);
        i.setH(h);
        i.setWeight(ww);
        i.setStackingWeightLimit(wC);
        i.setAllowedContainerSet(set);
        i.setStackingGroup(Math.max(1,sG));
        i.setStackingGroup(Math.max(1,sG));
        i.setAllowedStackingGroups(allowedSG);
        i.setSpinable(true);
        i.setLoading(true);
        i.postInit()

        itemIdx++;

        return i;
    }

    static Position findPos(List<Position> pList, int x, int y, int z) {
        return findPos(pList, x, y, z, false);
    }

    static Position findPos(List<Position> pList, int x, int y, int z, boolean rotated) {
        for (Position position : pList) {
            if(position.getX() == x && position.getY() == y && position.getZ() == z)
                if((rotated && position instanceof RotatedPosition)
                        || (!rotated && !(position instanceof RotatedPosition)))
                    return position;
        }

        return null;
    }
}
