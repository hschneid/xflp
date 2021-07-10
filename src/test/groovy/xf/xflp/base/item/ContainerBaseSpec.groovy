package xf.xflp.base.item

import helper.Helper
import spock.lang.Specification

class ContainerBaseSpec extends Specification {

    def "find first position in empty container"() {
        when:
        def con = Helper.getContainer(2,2,2);
        def i = Helper.getItem(1, 1, 1, 1, 10, 0);
        def pList = con.getPossibleInsertPositionList(i);
        def pp = Helper.findPos(pList, 0, 0, 0);

        then:
        pp != null
        pList.size() == 1
        con.getNbrOfLoadedThings() == 0
    }

    def "add item into empty container"() {
        def con = Helper.getContainer(2,2,2);
        def i = Helper.getItem(1, 1, 1, 1, 10, 0);
        def pList = con.getPossibleInsertPositionList(i);

        when:
        con.add(i, pList.get(0));

        then:
        con.getNbrOfLoadedThings() == 1
        i.x == 0 && i.y == 0 && i.z == 0
    }

    def "find second position in container"() {
        def con = Helper.getContainer(2,2,2);
        def i = Helper.getItem(1, 1, 1, 1, 10, 0);
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0);
        con.add(i, con.getPossibleInsertPositionList(i).get(0));

        when:
        def pList = con.getPossibleInsertPositionList(i2);

        then:
        pList.size() == 3
        Helper.findPos(pList, 0, 0, 0) == null
        Helper.findPos(pList, 1, 0, 0) != null
        Helper.findPos(pList, 0, 1, 0) != null
        Helper.findPos(pList, 0, 0, 1) != null
    }

    def "add second item to container"() {
        def con = Helper.getContainer(2,2,2);
        def i = Helper.getItem(1, 1, 1, 1, 10, 0);
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0);
        con.add(i, con.getPossibleInsertPositionList(i).get(0));
        def pList = con.getPossibleInsertPositionList(i2);

        when:
        con.add(i2, Helper.findPos(pList, 1, 0, 0));

        then:
        con.getNbrOfLoadedThings() == 2
        i2.x == 1 && i2.y == 0 && i2.z == 0
    }

    def "find and add third item in container"() {
        def con = Helper.getContainer(2,2,2);
        def i = Helper.getItem(1, 1, 1, 1, 10, 0);
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0);
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0);
        con.add(i, con.getPossibleInsertPositionList(i).get(0));
        def pList = con.getPossibleInsertPositionList(i2);
        con.add(i2, Helper.findPos(pList, 1, 0, 0));

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
        def con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i1, con.getPossibleInsertPositionList(i1).get(0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
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
        def con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i5 = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i1, con.getPossibleInsertPositionList(i1).get(0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
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
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i5 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i6 = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, con.getPossibleInsertPositionList(i).get(0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
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
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(2, 1, 1, 1, 10, 0)

        when:
        def pList = con.getPossibleInsertPositionList(i)

        then:
        pList.size() == 2
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && p instanceof RotatedPosition} != null
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && !(p instanceof RotatedPosition)} != null
    }

    def "do not find insert positions for too big items"() {
        def con = Helper.getContainer(2,2,2, 10)
        def i1 = Helper.getItem(3, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 3, 1, 1, 10, 0);
        def i3 = Helper.getItem(1, 1, 3, 1, 10, 0);
        def i4 = Helper.getItem(1, 1, 1, 11, 10, 0);
        def i5 = Helper.getItem(1, 1, 1, 9, 10, 0);

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
        def con = Helper.getContainer(2,2,5)
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(1, 1, 1, 1, 100, 0)

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0,0,1))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 0,0,2))
        def apList1 = new ArrayList<>(con.activePosList)
        con.remove(i2)
        def apList2 = new ArrayList<>(con.activePosList)
        con.remove(i1)
        def apList3 = new ArrayList<>(con.activePosList)
        con.remove(i3)
        def apList4 = new ArrayList<>(con.activePosList)

        then:
        Helper.findPos(apList1, 1, 0, 0) != null
        Helper.findPos(apList1, 0, 1, 0) != null
        Helper.findPos(apList1, 1, 0, 1) != null
        Helper.findPos(apList1, 0, 1, 1) != null
        Helper.findPos(apList1, 1, 0, 2) != null
        Helper.findPos(apList1, 0, 1, 2) != null
        Helper.findPos(apList1, 0, 0, 3) != null
        Helper.findPos(apList2, 0, 0, 1) != null
        Helper.findPos(apList3, 0, 0, 0) != null
        con.activePosList.size() == 1
        Helper.findPos(apList4, 0, 0, 0) != null
    }

    def "test covered positions (X axis)"(){
        def con = Helper.getContainer(3,3,3);
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 0);

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0));
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 0, 1, 0, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.activePosList, 1, 1, 0);
        def pos2 = Helper.findPos(con.inactivePosList, 1, 1, 0);
        def pos3 = Helper.findPos(con.coveredPosList, 1, 1, 0);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Y axis)"(){
        def con = Helper.getContainer(3,3,3);
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i3 = Helper.getItem(1, 2, 1, 1, 1, 0);

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0, 1, 0));
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 1, 0, 0, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.activePosList, 1, 1, 0);
        def pos2 = Helper.findPos(con.inactivePosList, 1, 1, 0);
        def pos3 = Helper.findPos(con.coveredPosList, 1, 1, 0);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-X axis)"(){
        def con = Helper.getContainer(3,3,3);
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 0);

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0));
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 0, 0, 1, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.activePosList, 1, 0, 1);
        def pos2 = Helper.findPos(con.inactivePosList, 1, 0, 1);
        def pos3 = Helper.findPos(con.coveredPosList, 1, 0, 1);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-Y axis)"(){
        def con = Helper.getContainer(3,3,3);
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0);
        def i3 = Helper.getItem(1, 2, 1, 1, 1, 0);

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0, 1, 0));
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 0, 0, 1, false));
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.activePosList, 0, 1, 1);
        def pos2 = Helper.findPos(con.inactivePosList, 0, 1, 1);
        def pos3 = Helper.findPos(con.coveredPosList, 0, 1, 1);

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test touching perimeter (only one wall)"() {
        def con = Helper.getContainer(10,10,10);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 48
    }

    def "test touching perimeter (only two walls on the width)"() {
        def con = Helper.getContainer(4,10,10);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the length)"() {
        def con = Helper.getContainer(10,4,10);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the height)"() {
        def con = Helper.getContainer(10,10,4);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only three walls on the width, length)"() {
        def con = Helper.getContainer(4,4,10);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the width, height)"() {
        def con = Helper.getContainer(4,10,4);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the length, height)"() {
        def con = Helper.getContainer(10,4,4);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only all walls)"() {
        def con = Helper.getContainer(4,4,4);
        def i = Helper.getItem(4, 4, 4, 1, 1, 0);

        when:
        def pList = con.getPossibleInsertPositionList(i);
        def pos = Helper.findPos(pList, 0, 0, 0);
        float f = con.getTouchingPerimeter(i, pos, 1, true, true);

        then:
        pos != null
        f == 96
    }

    def "test touching perimeter (mixed wall and box)"() {
        def con = Helper.getContainer(4,4,4);
        def i1 = Helper.getItem(1, 4, 4, 1, 1, 0);
        def i2 = Helper.getItem(2, 2, 2, 1, 1, 0);

        when:
        def pos = Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0)
        float f = con.getTouchingPerimeter(i2, pos2, 1, true, true);

        then:
        pos != null
        pos2 != null
        f == 12
    }

    def "test touching perimeter (mixed wall and half-height box)"() {
        def con = Helper.getContainer(4,4,4);
        def i1 = Helper.getItem(1, 4, 1, 1, 1, 0);
        def i2 = Helper.getItem(2, 2, 2, 1, 1, 0);

        when:
        def pos = Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0)
        float f = con.getTouchingPerimeter(i2, pos2, 1, true, true);

        then:
        pos != null
        pos2 != null
        f == 10
    }

    def "test touching perimeter (only box)"() {
        def con = Helper.getContainer(4,4,4)
        def i1 = Helper.getItem(1, 4, 4, 1, 1, 0)
        def i2 = Helper.getItem(3, 1, 4, 1, 1, 0);
        def i3 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0)
        con.add(i2, pos2)
        def pos3 = Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 1, 0)
        float f = con.getTouchingPerimeter(i3, pos3, 1, true, true);

        then:
        pos != null
        pos2 != null
        pos3 != null
        f == 12
    }

    def "test full coverage of item ground"() {
        def con = Helper.getContainer(5,1,3);
        def i1 = Helper.getItem(1, 1, 2, 1, 100, 0);
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0);
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0));
        def i3 = Helper.getItem(2, 1, 2, 1, 100, 0);
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 3, 0, 0, false));

        when:
        def i4 = Helper.getItem(2, 1, 1, 1, 100, 0);
        def pos4 = Helper.findPos(con.getPossibleInsertPositionList(i4), 0, 0, 2, false);
        def i5 = Helper.getItem(3, 1, 1, 1, 100, 0);
        def pos5 = Helper.findPos(con.getPossibleInsertPositionList(i5), 0, 0, 2, false);
        def i6 = Helper.getItem(4, 1, 1, 1, 100, 0);
        def pos6 = Helper.findPos(con.getPossibleInsertPositionList(i6), 0, 0, 2, false);

        then:
        pos4 == null
        pos5 == null
        pos6 != null
    }

    def "test rotation"() {
        def con = Helper.getContainer(2,2,1);
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0);
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0);
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0);
        i3.spinable = false
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0));


        when:
        def pList2 = con.getPossibleInsertPositionList(i2);
        def pList3 = con.getPossibleInsertPositionList(i3);

        then:
        Helper.findPos(pList2, 1, 0, 0, true) != null
        Helper.findPos(pList2, 0, 1, 0, false) != null
        Helper.findPos(pList3, 1, 0, 0, true) == null
        Helper.findPos(pList3, 0, 1, 0, false) != null
    }

    def "test horizontal projection of insert position"() {
        def con = Helper.getContainer(3, 5,2)
        def i1 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 3, 1, 1, 100, 0)
        def i3 = Helper.getItem(3, 2, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 2, 0, 0))
        when:
        def pList = con.getPossibleInsertPositionList(i3)
        then:
        Helper.findPos(pList, 0, 3, 0) != null
    }

    def "test vertical projection of insert position"() {
        def con = Helper.getContainer(3, 5,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(1, 3, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0, 2, 0))
        when:
        def pList = con.getPossibleInsertPositionList(i3)
        then:
        Helper.findPos(pList, 2, 0, 0) != null
    }

    def "test vertical projection of insert position at other box"() {
        def con = Helper.getContainer(4, 6,2)
        def i1 = Helper.getItem(3, 2, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getItem(2, 3, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0, 2, 0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 0, 4, 0))
        when:
        def pList = con.getPossibleInsertPositionList(i4)
        def pos = Helper.findPos(pList, 2, 2, 0)
        then:
        pos != null
    }

    def "big test projected insert positions"() {
        def con = Helper.getContainer(4, 6,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 1, 2, 0))
        when:
        def pList = con.getPossibleInsertPositionList(i4)
        then:
        // Normal pos
        Helper.findPos(pList, 0, 1, 0) != null
        Helper.findPos(pList, 1, 3, 0) != null
        Helper.findPos(pList, 3, 2, 0) != null
        Helper.findPos(pList, 2, 0, 0) != null
        // Horizontal projected pos
        Helper.findPos(pList, 0, 2, 0) != null
        Helper.findPos(pList, 0, 3, 0) != null
        // Vertical projected pos
        Helper.findPos(pList, 3, 0, 0) != null
    }


}
