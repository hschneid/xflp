package xf.xflp.base.problem

import spock.lang.Specification

class ContainerBaseSpec extends Specification {

    def itemIdx = 0

    def "find first position in empty container"() {
        when:
        def con = getContainer(2,2,2);
        def i = getItem(1, 1, 1, 1, 10, 0);
        def pList = con.getPossibleInsertPositionList(i);
        def pp = findPos(pList, 0, 0, 0);

        then:
        pp != null
        pList.size() == 1
        con.getNbrOfLoadedThings() == 0
    }

    def "add item into empty container"() {
        def con = getContainer(2,2,2);
        def i = getItem(1, 1, 1, 1, 10, 0);
        def pList = con.getPossibleInsertPositionList(i);

        when:
        con.add(i, pList.get(0));

        then:
        con.getNbrOfLoadedThings() == 1
        i.x == 0 && i.y == 0 && i.z == 0
    }

    def "find second position in container"() {
        def con = getContainer(2,2,2);
        def i = getItem(1, 1, 1, 1, 10, 0);
        def i2 = getItem(1, 1, 1, 1, 10, 0);
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
        def con = getContainer(2,2,2);
        def i = getItem(1, 1, 1, 1, 10, 0);
        def i2 = getItem(1, 1, 1, 1, 10, 0);
        con.add(i, con.getPossibleInsertPositionList(i).get(0));
        def pList = con.getPossibleInsertPositionList(i2);

        when:
        con.add(i2, findPos(pList, 1, 0, 0));

        then:
        con.getNbrOfLoadedThings() == 2
        i2.x == 1 && i2.y == 0 && i2.z == 0
    }

    def "find and add third item in container"() {
        def con = getContainer(2,2,2);
        def i = getItem(1, 1, 1, 1, 10, 0);
        def i2 = getItem(1, 1, 1, 1, 10, 0);
        def i3 = getItem(2, 1, 1, 1, 10, 0);
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
        def con = getContainer(2,2,2)
        def i1 = getItem(1, 1, 1, 1, 10, 0)
        def i2 = getItem(1, 1, 1, 1, 10, 0)
        def i3 = getItem(2, 1, 1, 1, 10, 0)
        def i4 = getItem(1, 1, 1, 1, 10, 0)
        con.add(i1, con.getPossibleInsertPositionList(i1).get(0))
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
        def con = getContainer(2,2,2)
        def i1 = getItem(1, 1, 1, 1, 10, 0)
        def i2 = getItem(1, 1, 1, 1, 10, 0)
        def i3 = getItem(2, 1, 1, 1, 10, 0)
        def i4 = getItem(1, 1, 1, 1, 10, 0)
        def i5 = getItem(1, 2, 1, 1, 10, 0)
        con.add(i1, con.getPossibleInsertPositionList(i1).get(0))
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
        def con = getContainer(2,2,2)
        def i = getItem(1, 1, 1, 1, 10, 0)
        def i2 = getItem(1, 1, 1, 1, 10, 0)
        def i3 = getItem(2, 1, 1, 1, 10, 0)
        def i4 = getItem(1, 1, 1, 1, 10, 0)
        def i5 = getItem(1, 2, 1, 1, 10, 0)
        def i6 = getItem(1, 1, 1, 1, 10, 0)
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
        def con = getContainer(2,2,2)
        def i = getItem(2, 1, 1, 1, 10, 0)

        when:
        def pList = con.getPossibleInsertPositionList(i)

        then:
        pList.size() == 2
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && p instanceof RotatedPosition} != null
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && !(p instanceof RotatedPosition)} != null
    }

    def "do not find insert positions for too big items"() {
        def con = getContainer(2,2,2, 10)
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

    def "add and remove items from container"() {
        def con = getContainer(2,2,5)
        def i1 = getItem(1, 1, 1, 1, 100, 0)
        def i2 = getItem(1, 1, 1, 1, 100, 0)
        def i3 = getItem(1, 1, 1, 1, 100, 0)

        when:
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0,0,1))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 0,0,2))
        def apList1 = new ArrayList<>(con.getActivePosList())
        con.remove(i2)
        def apList2 = new ArrayList<>(con.getActivePosList())
        con.remove(i1)
        def apList3 = new ArrayList<>(con.getActivePosList())
        con.remove(i3)
        def apList4 = new ArrayList<>(con.getActivePosList())

        then:
        findPos(apList1, 1, 0, 0) != null
        findPos(apList1, 0, 1, 0) != null
        findPos(apList1, 1, 0, 1) != null
        findPos(apList1, 0, 1, 1) != null
        findPos(apList1, 1, 0, 2) != null
        findPos(apList1, 0, 1, 2) != null
        findPos(apList1, 0, 0, 3) != null
        findPos(apList2, 0, 0, 1) != null
        findPos(apList3, 0, 0, 0) != null
        con.getActivePosList().size() == 1
        findPos(apList4, 0, 0, 0) != null
    }

    def "test covered positions (X axis)"(){
        def con = getContainer(3,3,3);
        def i1 = getItem(1, 1, 1, 1, 1, 0);
        def i2 = getItem(1, 1, 1, 1, 1, 0);
        def i3 = getItem(2, 1, 1, 1, 1, 0);

        when:
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0));
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 0, 1, 0, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = findPos(con.getActivePosList(), 1, 1, 0);
        def pos2 = findPos(con.getInActivePosList(), 1, 1, 0);
        def pos3 = findPos(con.getCoveredPosList(), 1, 1, 0);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Y axis)"(){
        def con = getContainer(3,3,3);
        def i1 = getItem(1, 1, 1, 1, 1, 0);
        def i2 = getItem(1, 1, 1, 1, 1, 0);
        def i3 = getItem(1, 2, 1, 1, 1, 0);

        when:
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0, 1, 0));
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 1, 0, 0, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = findPos(con.getActivePosList(), 1, 1, 0);
        def pos2 = findPos(con.getInActivePosList(), 1, 1, 0);
        def pos3 = findPos(con.getCoveredPosList(), 1, 1, 0);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-X axis)"(){
        def con = getContainer(3,3,3);
        def i1 = getItem(1, 1, 1, 1, 1, 0);
        def i2 = getItem(1, 1, 1, 1, 1, 0);
        def i3 = getItem(2, 1, 1, 1, 1, 0);

        when:
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0));
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 0, 0, 1, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = findPos(con.getActivePosList(), 1, 0, 1);
        def pos2 = findPos(con.getInActivePosList(), 1, 0, 1);
        def pos3 = findPos(con.getCoveredPosList(), 1, 0, 1);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-Y axis)"(){
        def con = getContainer(3,3,3);
        def i1 = getItem(1, 1, 1, 1, 1, 0);
        def i2 = getItem(1, 1, 1, 1, 1, 0);
        def i3 = getItem(1, 2, 1, 1, 1, 0);

        when:
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0, 1, 0));
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 0, 0, 1, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = findPos(con.getActivePosList(), 0, 1, 1);
        def pos2 = findPos(con.getInActivePosList(), 0, 1, 1);
        def pos3 = findPos(con.getCoveredPosList(), 0, 1, 1);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test touching perimeter (only one wall)"() {
        def con = getContainer(10,10,10);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 48
    }

    def "test touching perimeter (only two walls on the width)"() {
        def con = getContainer(4,10,10);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the length)"() {
        def con = getContainer(10,4,10);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the height)"() {
        def con = getContainer(10,10,4);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only three walls on the width, length)"() {
        def con = getContainer(4,4,10);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the width, height)"() {
        def con = getContainer(4,10,4);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the length, height)"() {
        def con = getContainer(10,4,4);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only all walls)"() {
        def con = getContainer(4,4,4);
        def i = getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 96
    }

    def "test touching perimeter (mixed wall and box)"() {
        def con = getContainer(4,4,4);
        def i1 = getItem(1, 4, 4, 1, 1, 0);
        def i2 = getItem(2, 2, 2, 1, 1, 0);

        when:
        def pos = findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0)
        float f = con.getTouchingPerimeter(i2, pos2, 1, true, true);

        then:
        pos != null
        pos2 != null
        f == 12
    }

    def "test touching perimeter (mixed wall and half-height box)"() {
        def con = getContainer(4,4,4);
        def i1 = getItem(1, 4, 1, 1, 1, 0);
        def i2 = getItem(2, 2, 2, 1, 1, 0);

        when:
        def pos = findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0)
        float f = con.getTouchingPerimeter(i2, pos2, 1, true, true);

        then:
        pos != null
        pos2 != null
        f == 10
    }

    def "test touching perimeter (only box)"() {
        def con = getContainer(4,4,4)
        def i1 = getItem(1, 4, 4, 1, 1, 0)
        def i2 = getItem(3, 1, 4, 1, 1, 0);
        def i3 = getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0)
        con.add(i2, pos2)
        def pos3 = findPos(con.getPossibleInsertPositionList(i2), 1, 1, 0)
        float f = con.getTouchingPerimeter(i3, pos3, 1, true, true);

        then:
        pos != null
        pos2 != null
        pos3 != null
        f == 12
    }

    def "test full coverage of item ground"() {
        def con = getContainer(5,1,3);
        def i1 = getItem(1, 1, 2, 1, 100, 0);
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        def i2 = getItem(2, 1, 1, 1, 100, 0);
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0));
        def i3 = getItem(2, 1, 2, 1, 100, 0);
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 3, 0, 0, false));

        when:
        def i4 = getItem(2, 1, 1, 1, 100, 0);
        def pos4 = findPos(con.getPossibleInsertPositionList(i4), 0, 0, 2, false);
        def i5 = getItem(3, 1, 1, 1, 100, 0);
        def pos5 = findPos(con.getPossibleInsertPositionList(i5), 0, 0, 2, false);
        def i6 = getItem(4, 1, 1, 1, 100, 0);
        def pos6 = findPos(con.getPossibleInsertPositionList(i6), 0, 0, 2, false);

        then:
        pos4 == null
        pos5 == null
        pos6 != null
    }

    def "test rotation"() {
        def con = getContainer(2,2,1);
        def i1 = getItem(1, 1, 1, 1, 100, 0);
        def i2 = getItem(2, 1, 1, 1, 100, 0);
        def i3 = getItem(2, 1, 1, 1, 100, 0);
        i3.spinable = false
        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));


        when:
        def pList2 = con.getPossibleInsertPositionList(i2);
        def pList3 = con.getPossibleInsertPositionList(i3);

        then:
        findPos(pList2, 1, 0, 0, true) != null
        findPos(pList2, 0, 1, 0, false) != null
        findPos(pList3, 1, 0, 0, true) == null
        findPos(pList3, 0, 1, 0, false) != null
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
