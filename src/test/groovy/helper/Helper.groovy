package helper

import xf.xflp.base.container.AddRemoveContainer
import xf.xflp.base.container.Container
import xf.xflp.base.container.GroundContactRule
import xf.xflp.base.fleximport.ContainerData
import xf.xflp.base.item.Item
import xf.xflp.base.item.Position
import xf.xflp.base.item.RotatedPosition

class Helper {

    static int itemIdx = 0

    static Container getContainer2(int width, int length, int height) {
        return getContainer2(width, length, height, 999999999)
    }

    static Container getContainer2(int width, int length, int height, float maxWeight) {
        return new AddRemoveContainer(
                width,
                length,
                height,
                maxWeight,
                ContainerData.DEFAULT_CONTAINER_TYPE,
                GroundContactRule.FREE,
                0
        )
    }

    static Item getItem(int w, int l, int h, int ww, long wC, int sG) {
        return getItem(w, l, h, ww, wC, sG, Math.max(1,sG))
    }

    static Item getItem(int w, int l, int h, int ww, long wC, int sG, int allowedSG) {
        return getItem(w, l, h, ww, wC, sG, allowedSG, -1)
    }

    static Item getItem(int w, int l, int h, int ww, long wC, int sG, int allowedSG, int nbrAllowedStackItems) {
        Set<Integer> set = new HashSet<>()
        set.add(0)

        Item i = new Item()

        i.setExternalIndex(itemIdx)
        i.setOrderIndex(itemIdx)
        i.setLoadingLoc(itemIdx)
        i.setUnLoadingLoc(itemIdx + 1)
        i.setW(w)
        i.setL(l)
        i.setH(h)
        i.setWeight(ww)
        i.setStackingWeightLimit(wC)
        i.setAllowedContainerSet(set)
        i.setStackingGroup(Math.max(1,sG))
        i.setStackingGroup(Math.max(1,sG))
        i.setAllowedStackingGroups(allowedSG)
        i.setNbrOfAllowedStackedItems(nbrAllowedStackItems)
        i.setSpinable(true)
        i.setLoading(true)
        i.postInit()

        itemIdx++

        return i
    }

    static Position findPos(Collection<Position> pList, int x, int y, int z) {
        return findPos(pList, x, y, z, false)
    }

    static Position findPos(Collection<Position> pList, int x, int y, int z, boolean rotated) {
        for (Position position : pList) {
            if(position.getX() == x && position.getY() == y && position.getZ() == z)
                if((rotated && position instanceof RotatedPosition)
                        || (!rotated && !(position instanceof RotatedPosition)))
                    return position
        }

        return null
    }
}
