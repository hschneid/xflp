package xf.xflp.base.problem

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.ComplexContainer
import xf.xflp.base.container.GroundContactRule

class ContainerStackingSpec extends Specification {

    def "add to stack simple"() {
        ComplexContainer con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def pList = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = con.getPossibleInsertPositionList(i2)
        def found = pList2.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1}

        then:
        pList.size() > 0
        found != null
    }

    def "stacking is not possible"() {
        ComplexContainer con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 2, 10, 0)
        def pList = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = con.getPossibleInsertPositionList(i2)
        def found = pList2.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1}

        then:
        pList.size() > 0
        found == null
    }

    def "add to a stack with two stack"() {
        ComplexContainer con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 10, 0)
        def pList = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = con.getPossibleInsertPositionList(i2)
        def found2 = pList2.find {p -> p.getX() == 1 && p.getY() == 0 && p.getZ() == 0}
        con.add(i2, found2)
        def pList3 = con.getPossibleInsertPositionList(i3)
        def found3 = pList3.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1}

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 != null
    }

    def "do not add to a stack with two stack (too heavy/bearing capacity)"() {
        ComplexContainer con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 3, 10, 0)
        def pList = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = con.getPossibleInsertPositionList(i2)
        def found2 = pList2.find {p -> p.getX() == 1 && p.getY() == 0 && p.getZ() == 0}
        con.add(i2, found2)
        def pList3 = con.getPossibleInsertPositionList(i3)
        def found3 = pList3.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 1}

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 == null
    }

    def "placing one item over two stacks - stacking groups not fitting"() {
        ComplexContainer con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 1)
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1,0,0))

        when:
        def pList3 = con.getPossibleInsertPositionList(i3)

        then:
        Helper.findPos(pList3, 0,0, 1) == null
    }

    def "placing one item over two stacks - stacking groups are fitting"() {
        ComplexContainer con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 2)
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1,0,0))

        when:
        def pList3 = con.getPossibleInsertPositionList(i3)

        then:
        Helper.findPos(pList3, 0,0, 1) != null
    }


    def "do not add to a double stack (too heavy/bearing capacity)"() {
        def con = Helper.getContainer(2,2,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList1.get(0))

        def pList2 = con.getPossibleInsertPositionList(i2)
        def pos2 = Helper.findPos(pList2, 1, 0, 0)
        con.add(i2, pos2)

        def pList3 = con.getPossibleInsertPositionList(i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = con.getPossibleInsertPositionList(i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 == null
    }

    def "do not add to a double stack (nearly too heavy/bearing capacity)"() {
        def con = Helper.getContainer(2,2,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList1.get(0))

        def pList2 = con.getPossibleInsertPositionList(i2)
        def pos2 = Helper.findPos(pList2, 1, 0, 0)
        con.add(i2, pos2)

        def pList3 = con.getPossibleInsertPositionList(i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = con.getPossibleInsertPositionList(i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 == null
    }

    def "add to a double stack (not too heavy/bearing capacity)"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(2, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(3, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList1.get(0))

        def pList2 = con.getPossibleInsertPositionList(i2)
        def pos2 = Helper.findPos(pList2, 2, 0, 0)
        con.add(i2, pos2)

        def pList3 = con.getPossibleInsertPositionList(i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = con.getPossibleInsertPositionList(i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 != null
    }

    def "add to a double stack with bigger size (not too heavy/bearing capacity)"() {
        def con = Helper.getContainer(6,3,3)
        def i1 = Helper.getItem(4, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(6, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = con.getPossibleInsertPositionList(i1)
        con.add(i1, pList1.get(0))

        def pList2 = con.getPossibleInsertPositionList(i2)
        def pos2 = Helper.findPos(pList2, 4, 0, 0)
        con.add(i2, pos2)

        def pList3 = con.getPossibleInsertPositionList(i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = con.getPossibleInsertPositionList(i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 != null
    }

    def "All items in same stacking group"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        def pList = con.getPossibleInsertPositionList(i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) != null
    }

    def "2 items not in same stacking group"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        def pList = con.getPossibleInsertPositionList(i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) == null
    }

    def "1 item not in same stacking group"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        def pList = con.getPossibleInsertPositionList(i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) == null
    }

    def "Items can bear multiple stacking groups"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        def pList = con.getPossibleInsertPositionList(i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) != null
    }

    def "Items cannot bear multiple stacking groups"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 2)

        when:
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1, 0, 0))
        def pList = con.getPossibleInsertPositionList(i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) == null
    }

    def "check overlapping items"() {
        def con = Helper.getContainer(3,3,2)
        con.groundContactRule = GroundContactRule.COVERED
        def i1 = Helper.getItem(2, 2, 1, 1, 111,0)
        def i2 = Helper.getItem(3, 1, 1, 1, 111,0 )
        def i3 = Helper.getItem(2, 1, 1, 1, 111,0 )
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0, 0, 0))
        when:
        def pList2 = con.getPossibleInsertPositionList(i2)
        def pos2 = Helper.findPos(pList2, 0, 0, 1)
        def pList3 = con.getPossibleInsertPositionList(i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        then:
        pos2 == null
        pos3 != null
    }

}
