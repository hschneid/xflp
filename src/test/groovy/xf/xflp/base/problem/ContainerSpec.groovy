package xf.xflp.base.problem

import spock.lang.Specification

class ContainerSpec extends Specification {

    def itemIdx = 0

    def "find first position in empty container"() {
        when:
        Container con = getContainer(2,2,2);
        Item i = getItem(1, 1, 1, 1, 10, 0);
        List<Position> pList = con.getPossibleInsertPositionList(i);
        Position pp = findPos(pList, 0, 0, 0);

        then:
        pp != null
        pList.size() == 1
        con.getNbrOfLoadedThings() == 0
    }

    def "add item into empty container"() {
        Container con = getContainer(2,2,2);
        Item i = getItem(1, 1, 1, 1, 10, 0);
        List<Position> pList = con.getPossibleInsertPositionList(i);

        when:
        con.add(i, pList.get(0));

        then:
        con.getNbrOfLoadedThings() == 1
        i.x == 0 && i.y == 0 && i.z == 0
    }

    def "find second position in container"() {
        Container con = getContainer(2,2,2);
        Item i = getItem(1, 1, 1, 1, 10, 0);
        Item i2 = getItem(1, 1, 1, 1, 10, 0);
        con.add(i, con.getPossibleInsertPositionList(i).get(0));

        when:
        def pList = con.getPossibleInsertPositionList(i2);

        then:
        pList.size() == 3
        findPos(pList, 0, 0, 0) == null
        findPos(pList, 1, 0, 0) != null
        findPos(pList, 0, 1, 0) != null
        findPos(pList, 0, 0, 1) != null
    }

    def "add second item to container"() {
        Container con = getContainer(2,2,2);
        Item i = getItem(1, 1, 1, 1, 10, 0);
        Item i2 = getItem(1, 1, 1, 1, 10, 0);
        con.add(i, con.getPossibleInsertPositionList(i).get(0));
        def pList = con.getPossibleInsertPositionList(i2);

        when:
        con.add(i2, findPos(pList, 1, 0, 0));

        then:
        con.getNbrOfLoadedThings() == 2
        i2.x == 1 && i2.y == 0 && i2.z == 0
    }

    def "find and add third item in container"() {
        Container con = getContainer(2,2,2);
        Item i = getItem(1, 1, 1, 1, 10, 0);
        Item i2 = getItem(1, 1, 1, 1, 10, 0);
        Item i3 = getItem(2, 1, 1, 1, 10, 0);
        con.add(i, con.getPossibleInsertPositionList(i).get(0));
        def pList = con.getPossibleInsertPositionList(i2);
        con.add(i2, findPos(pList, 1, 0, 0));

        when:
        pList = con.getPossibleInsertPositionList(i3);
        def foundPos = pList.find {p -> p.getX() == 0 && p.getY() == 1 && p.getZ() == 0}
        def found2 = pList.findAll {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1}.size() == 1
        con.add(i3, foundPos);

        then:
        con.getNbrOfLoadedThings() == 3
        foundPos != null
        found2
    }

    def "find and add forth item in container"() {
        Container con = getContainer(2,2,2)
        Item i = getItem(1, 1, 1, 1, 10, 0)
        Item i2 = getItem(1, 1, 1, 1, 10, 0)
        Item i3 = getItem(2, 1, 1, 1, 10, 0)
        Item i4 = getItem(1, 1, 1, 1, 10, 0)
        con.add(i, con.getPossibleInsertPositionList(i).get(0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        con.add(i3, con.getPossibleInsertPositionList(i3)
                .find {p -> p.getX() == 0 && p.getY() == 1 && p.getZ() == 0})
        when:
        def pList = con.getPossibleInsertPositionList(i4);
        def foundPos = pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1}
        def found2 = pList.findAll {p -> p.getX() == 1 && p.getY() == 0 && p.getZ() == 1}.size() == 1
        def found3 = pList.findAll {p -> p.getX() == 0 && p.getY() == 1 && p.getZ() == 1}.size() == 1
        con.add(i4, foundPos);

        then:
        con.getNbrOfLoadedThings() == 4
        foundPos != null
        found2
        found3
    }

    def "find and add fifth item in container"() {
        Container con = getContainer(2,2,2)
        Item i = getItem(1, 1, 1, 1, 10, 0)
        Item i2 = getItem(1, 1, 1, 1, 10, 0)
        Item i3 = getItem(2, 1, 1, 1, 10, 0)
        Item i4 = getItem(1, 1, 1, 1, 10, 0)
        Item i5 = getItem(1, 2, 1, 1, 10, 0)
        con.add(i, con.getPossibleInsertPositionList(i).get(0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        con.add(i3, con.getPossibleInsertPositionList(i3)
                .find {p -> p.getX() == 0 && p.getY() == 1 && p.getZ() == 0})
        con.add(i4, con.getPossibleInsertPositionList(i4)
                .find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1})
        when:
        def pList = con.getPossibleInsertPositionList(i5);
        def foundPos = pList.find {p -> p.getX() == 1 && p.getY() == 0 && p.getZ() == 1}
        con.add(i5, foundPos)

        then:
        con.getNbrOfLoadedThings() == 5
        foundPos != null
    }

    def "find and add sixth item in container"() {
        Container con = getContainer(2,2,2)
        Item i = getItem(1, 1, 1, 1, 10, 0)
        Item i2 = getItem(1, 1, 1, 1, 10, 0)
        Item i3 = getItem(2, 1, 1, 1, 10, 0)
        Item i4 = getItem(1, 1, 1, 1, 10, 0)
        Item i5 = getItem(1, 2, 1, 1, 10, 0)
        Item i6 = getItem(1, 1, 1, 1, 10, 0)
        con.add(i, con.getPossibleInsertPositionList(i).get(0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        con.add(i3, con.getPossibleInsertPositionList(i3)
                .find {p -> p.getX() == 0 && p.getY() == 1 && p.getZ() == 0})
        con.add(i4, con.getPossibleInsertPositionList(i4)
                .find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1})
        con.add(i5, con.getPossibleInsertPositionList(i5)
                .find {p -> p.getX() == 1 && p.getY() == 0 && p.getZ() == 1})
        when:
        def pList = con.getPossibleInsertPositionList(i6);
        def foundPos = pList.find {p -> p.getX() == 0 && p.getY() == 1 && p.getZ() == 1}
        def hasOtherPos = pList.findAll {p -> !(p.getX() == 0 && p.getY() == 1 && p.getZ() == 1)}.size() > 0
        con.add(i6, foundPos)

        then:
        con.getNbrOfLoadedThings() == 6
        foundPos != null
        !hasOtherPos
    }

    def "find rotated insert position"() {
        Container con = getContainer(2,2,2)
        Item i = getItem(2, 1, 1, 1, 10, 0)

        when:
        def pList = con.getPossibleInsertPositionList(i)

        then:
        pList.size() == 2
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && p instanceof RotatedPosition} != null
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && !(p instanceof RotatedPosition)} != null
    }

    def "do not find insert positions for too big items"() {
        Container con = getContainer(2,2,2, 10)
        def i1 = getItem(3, 1, 1, 1, 10, 0)
        def i2 = getItem(1, 3, 1, 1, 10, 0);
        def i3 = getItem(1, 1, 3, 1, 10, 0);
        def i4 = getItem(1, 1, 1, 11, 10, 0);
        def i5 = getItem(1, 1, 1, 9, 10, 0);

        when:
        def pList1 = con.getPossibleInsertPositionList(i1);
        def pList2 = con.getPossibleInsertPositionList(i2);
        def pList3 = con.getPossibleInsertPositionList(i3);
        def pList4 = con.getPossibleInsertPositionList(i4);
        def pList5 = con.getPossibleInsertPositionList(i5);

        then:
        pList1.size() == 0
        pList2.size() == 0
        pList3.size() == 0
        pList4.size() == 0
        pList5.size() > 0
    }

    private Container getContainer(int width, int length, int height) {
        return new Container(width, length, height, 999999999, 0, 0);
    }
    private Container getContainer(int width, int length, int height, float maxWeight) {
        return new Container(width, length, height, maxWeight, 0, 0);
    }

    private Item getItem(int w, int l, int h, int ww, long wC, int sG) {
        return getItem(w, l, h, ww, wC, sG, 1 << sG);
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
                1<<sG,
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
