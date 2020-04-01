package xf.xflp.base.problem

import spock.lang.Specification

class ContainerRemoveSpec extends Specification {

    def itemIdx = 0

    def "clear means clear - full container"() {
        Container con = getContainer(3,3,2)
        def i1 = getItem(3, 1, 1, 1, 111, 0)
        def i2 = getItem(2, 1, 1, 1, 111, 0)
        def i3 = getItem(1, 2, 1, 1, 111, 0)
        def i4 = getItem(1, 1, 1, 1, 111, 0)
        def i5 = getItem(1, 1, 1, 1, 111, 0)

        def i6 = getItem(2, 2, 1, 1, 111, 0)
        def i7 = getItem(1, 3, 1, 1, 111, 0)
        def i8 = getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 2,1,0))
        con.add(i4, findPos(con.getPossibleInsertPositionList(i4), 0,2,0))
        con.add(i5, findPos(con.getPossibleInsertPositionList(i5), 1,2,0))
        con.add(i6, findPos(con.getPossibleInsertPositionList(i6), 0,0,1))
        con.add(i7, findPos(con.getPossibleInsertPositionList(i7), 2,0,1))
        con.add(i8, findPos(con.getPossibleInsertPositionList(i8), 0,2,1))

        when:
        con.clear()
        then:
        con.getLoadedWeight() == 0
        findPos(con.getPossibleInsertPositionList(i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getNbrOfLoadedThings() == 0
    }

    def "clear means clear - empty container"() {
        Container con = getContainer(3,3,2)
        def i1 = getItem(3, 1, 1, 1, 111, 0)

        when:
        con.clear()
        then:
        con.getLoadedWeight() == 0
        findPos(con.getPossibleInsertPositionList(i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getNbrOfLoadedThings() == 0
    }

    def "remove an item - a valid item"() {
        Container con = getContainer(3,3,2)
        def i1 = getItem(3, 1, 1, 1, 111, 0)
        def i2 = getItem(1, 2, 1, 1, 111, 0)
        def i3 = getItem(1, 2, 1, 1, 111, 0)
        def i4 = getItem(1, 2, 1, 1, 111, 0)

        def i6 = getItem(2, 2, 1, 1, 111, 0)
        def i7 = getItem(1, 3, 1, 1, 111, 0)
        def i8 = getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 1,1,0))
        con.add(i4, findPos(con.getPossibleInsertPositionList(i4), 2,1,0))

        when:
        con.remove(i3)
        then:
        findPos(con.getPossibleInsertPositionList(i3), 1,1,0) != null
        con.getLoadedVolume() == 7
        con.getNbrOfLoadedThings() == 3
    }

    def "remove an item - an invalid item"() {
        Container con = getContainer(3,3,2)
        def i1 = getItem(3, 1, 1, 1, 111, 0)
        def i2 = getItem(1, 2, 1, 1, 111, 0)
        def i3 = getItem(1, 2, 1, 1, 111, 0)
        def i4 = getItem(1, 2, 1, 1, 111, 0)

        def i6 = getItem(2, 2, 1, 1, 111, 0)
        def i7 = getItem(1, 3, 1, 1, 111, 0)
        def i8 = getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 1,1,0))
        con.add(i4, findPos(con.getPossibleInsertPositionList(i4), 2,1,0))
        con.add(i6, findPos(con.getPossibleInsertPositionList(i6), 0,0,1))
        con.add(i7, findPos(con.getPossibleInsertPositionList(i7), 2,0,1))
        con.add(i8, findPos(con.getPossibleInsertPositionList(i8), 0,2,1))

        when:
        con.remove(i3)
        then:
        findPos(con.getPossibleInsertPositionList(i3), 1,1,0) != null
        con.getLoadedVolume() == 16
        con.getNbrOfLoadedThings() == 6
    }

    def "remove everything one by one"() {
        Container con = getContainer(3,3,2)
        def i1 = getItem(3, 1, 1, 1, 111, 0)
        def i2 = getItem(2, 1, 1, 1, 111, 0)
        def i3 = getItem(1, 2, 1, 1, 111, 0)
        def i4 = getItem(1, 1, 1, 1, 111, 0)
        def i5 = getItem(1, 1, 1, 1, 111, 0)

        def i6 = getItem(2, 2, 1, 1, 111, 0)
        def i7 = getItem(1, 3, 1, 1, 111, 0)
        def i8 = getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 2,1,0))
        con.add(i4, findPos(con.getPossibleInsertPositionList(i4), 0,2,0))
        con.add(i5, findPos(con.getPossibleInsertPositionList(i5), 1,2,0))
        con.add(i6, findPos(con.getPossibleInsertPositionList(i6), 0,0,1))
        con.add(i7, findPos(con.getPossibleInsertPositionList(i7), 2,0,1))
        con.add(i8, findPos(con.getPossibleInsertPositionList(i8), 0,2,1))

        when:
        con.remove(i1)
        con.remove(i2)
        con.remove(i3)
        con.remove(i4)
        con.remove(i5)
        con.remove(i6)
        con.remove(i7)
        con.remove(i8)
        then:
        con.getLoadedWeight() == 0
        findPos(con.getPossibleInsertPositionList(i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getNbrOfLoadedThings() == 0
    }

    private Container getContainer(int width, int length, int height) {
        return new Container(width, length, height, 999999999, 0, 0);
    }
    private Container getContainer(int width, int length, int height, float maxWeight) {
        return new Container(width, length, height, maxWeight, 0, 0);
    }

    private Item getItem(int w, int l, int h, int ww, long wC, int sG) {
        return getItem(w, l, h, ww, wC, sG, Math.max(1,sG));
    }
    private Item getItem(int w, int l, int h, int ww, long wC, int sG, int allowedSG) {
        Set<Integer> set = new HashSet<>();
        set.add(0);

        Item i = new Item(
                itemIdx,
                itemIdx,
                itemIdx,
                itemIdx + 1,
                w,
                l,
                h,
                ww,
                wC,
                set,
                Math.max(1,sG),
                allowedSG,
                true,
                true
        );
        itemIdx++;

        return i;
    }

    private Position findPos(List<Position> pList, int x, int y, int z) {
        return findPos(pList, x, y, z, false);
    }

    private Position findPos(List<Position> pList, int x, int y, int z, boolean rotated) {
        for (Position position : pList) {
            if(position.getX() == x && position.getY() == y && position.getZ() == z)
                if((rotated && position instanceof RotatedPosition)
                        || (!rotated && !(position instanceof RotatedPosition)))
                    return position;
        }

        return null;
    }
}
