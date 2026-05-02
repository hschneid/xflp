package helper

import xf.xflp.base.container.*
import xf.xflp.base.fleximport.ContainerData
import xf.xflp.base.item.Item
import xf.xflp.base.item.PlacedItem
import xf.xflp.base.item.Position
import xf.xflp.base.monitor.DefaultStatusMonitor
import xf.xflp.base.monitor.StatusManager
import xf.xflp.base.position.PositionCandidate
import xf.xflp.base.position.PositionService

class Helper {

    static int itemIdx = 0

    static Container getAddSpaceContainer(int width, int length, int height) {
        return getAddSpaceContainer(width, length, height, 999999999)
    }

    static Container getAddSpaceContainer(int width, int length, int height, float maxWeight) {
        return new AddContainer(
                width,
                length,
                height,
                maxWeight,
                ContainerData.DEFAULT_CONTAINER_TYPE,
                new DirectContainerParameter(
                        groundContactRule: GroundContactRule.COVERED,
                        lifoImportance: 0
                )
        )
    }

    static Container getAddSpaceContainer2(int width, int length, int height) {
        return getAddSpaceContainer2(width, length, height, Integer.MAX_VALUE)
    }

    static Container getAddSpaceContainer2(int width, int length, int height, float maxWeight) {
        return new AddRemoveContainer(
                width,
                length,
                height,
                maxWeight,
                ContainerData.DEFAULT_CONTAINER_TYPE,
                new DirectContainerParameter(
                        groundContactRule: GroundContactRule.COVERED,
                        lifoImportance: 0
                )
        )
    }

    static Item getItem(int w, int l, int h, float ww, long wC, int sG) {
        return getItem(w, l, h, ww, wC, sG, Math.max(1,sG))
    }

    static Item getItem(int w, int l, int h, float ww, long wC, int sG, int allowedSG) {
        return getItem(w, l, h, ww, wC, sG, allowedSG, 9999999)
    }

    static Item getItem(int w, int l, int h, float ww, long wC, int sG, int allowedSG, int nbrAllowedStackItems) {
        Set<Integer> set = new HashSet<>()
        set.add(0)

        def idx = itemIdx
        itemIdx++

        return new Item(
                w, l, h,
                true, true,
                idx, idx + 1,
                Math.max(1, sG) as long, set,
                allowedSG as long, nbrAllowedStackItems,
                0,
                ww, wC as float,
                idx, idx,
                true
        )
    }

    static PlacedItem getPlacedItem(int w, int l, int h, float ww, long wC, int sG) {
        return getPlacedItem(w, l, h, ww, wC, sG, Math.max(1,sG))
    }

    static PlacedItem getPlacedItem(int w, int l, int h, float ww, long wC, int sG, int allowedSG) {
        return getPlacedItem(w, l, h, ww, wC, sG, allowedSG, 9999999)
    }

    static PlacedItem getPlacedItem(int w, int l, int h, float ww, long wC, int sG, int allowedSG, int nbrAllowedStackItems) {
        return new PlacedItem(getItem(w, l, h, ww, wC, sG, allowedSG, nbrAllowedStackItems))
    }

    static PlacedItem getPlacedItemAtPos(int x, int y, int w, int l) {
        return getPlacedItemAtPos(x, y, 0, w, l, 1)
    }

    static PlacedItem getPlacedItemAtPos(int x, int y, int z, int w, int l, int h) {
        return getPlacedItemAtPos(x, y, z, w, l, h, 1, 1, 1, 1, 9999999)
    }

    static PlacedItem getPlacedItemAtPos(int x, int y, int z, int w, int l, int h, float ww, long wC, int sG, int allowedSG, int nbrAllowedStackItems) {
        def ip = new PlacedItem(getItem(w, l, h, ww, wC, sG, allowedSG, nbrAllowedStackItems))
        ip.setPosition(Position.of(x, y, z))

        return ip
    }

    static void add(Container con, PositionCandidate cand) {
        con.add(cand.item, cand.position, cand.isRotated)
    }

    static void add(Container con, PlacedItem i, int x, int y, int z) {
        add(con, findCand(PositionService.findPositionCandidates(con, i), x, y, z))
    }

    static StatusManager getStatusManager() {
        def stat = new StatusManager()
        stat.addObserver(new DefaultStatusMonitor())
        return stat
    }

    static PositionCandidate findCand(Collection<PositionCandidate> pList, int x, int y, int z) {
        return findCand(pList, x, y, z, false)
    }

    static PositionCandidate findCand(Collection<PositionCandidate> candidates, int x, int y, int z, boolean rotated) {
        for (PositionCandidate cand : candidates) {
            if(cand.position.x() == x && cand.position.y() == y && cand.position.z() == z && cand.isRotated == rotated)
                    return cand
        }

        return null
    }

    static Position findPos(Collection<Position> positions, int x, int y, int z) {
        for (Position p : positions) {
            if(p.x() == x && p.y() == y && p.z() == z)
                return p
        }

        return null
    }
}
