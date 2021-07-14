package xf.xflp.base.container.add

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container
import xf.xflp.base.container.GroundContactRule
import xf.xflp.base.container.ParameterType
import xf.xflp.base.position.PositionService

class AddContainerStackingSpec extends Specification {

    def "add to stack simple"() {
        Container con = Helper.getAddContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def pList = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def found = Helper.findPos(pList2,0,0,1)

        then:
        pList.size() > 0
        found != null
    }

    def "stacking is not possible"() {
        Container con = Helper.getAddContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 2, 10, 0)
        def pList = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def found = Helper.findPos(pList2,0,0,1)

        then:
        pList.size() > 0
        found == null
    }

    def "add to a stack with two stack"() {
        Container con = Helper.getAddContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 10, 0)
        def pList = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def found2 = Helper.findPos(pList2,1,0,0)
        con.add(i2, found2)
        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def found3 = Helper.findPos(pList3,0,0,1)

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 != null
    }

    def "do not add to a stack with two stack (too heavy/bearing capacity)"() {
        Container con = Helper.getAddContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 3, 10, 0)
        def pList = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList.get(0))

        when:
        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def found2 = Helper.findPos(pList2,1,0,0)
        con.add(i2, found2)
        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def found3 = Helper.findPos(pList3,0,0,1)

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 == null
    }

    def "placing one item over two stacks - stacking groups not fitting"() {
        Container con = Helper.getAddContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 1)
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0,0,0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1,0,0))

        when:
        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList3, 0,0, 1) == null
    }

    def "placing one item over two stacks - stacking groups are fitting"() {
        Container con = Helper.getAddContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 2)
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0,0,0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1,0,0))

        when:
        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList3, 0,0, 1) != null
    }


    def "do not add to a double stack (too heavy/bearing capacity)"() {
        def con = Helper.getAddContainer(2,2,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList1.get(0))

        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def pos2 = Helper.findPos(pList2, 1, 0, 0)
        con.add(i2, pos2)

        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = PositionService.getPossibleInsertPositionList(con, i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 == null
    }

    def "do not add to a double stack (nearly too heavy/bearing capacity)"() {
        def con = Helper.getAddContainer(2,2,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList1.get(0))

        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def pos2 = Helper.findPos(pList2, 1, 0, 0)
        con.add(i2, pos2)

        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = PositionService.getPossibleInsertPositionList(con, i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 == null
    }

    def "add to a double stack (not too heavy/bearing capacity)"() {
        def con = Helper.getAddContainer(3,3,3)
        def i1 = Helper.getItem(2, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(3, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList1.get(0))

        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def pos2 = Helper.findPos(pList2, 2, 0, 0)
        con.add(i2, pos2)

        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = PositionService.getPossibleInsertPositionList(con, i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 != null
    }

    def "add to a double stack with bigger size (not too heavy/bearing capacity)"() {
        def con = Helper.getAddContainer(6,3,3)
        def i1 = Helper.getItem(4, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(6, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = PositionService.getPossibleInsertPositionList(con, i1)
        con.add(i1, pList1.get(0))

        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def pos2 = Helper.findPos(pList2, 4, 0, 0)
        con.add(i2, pos2)

        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        con.add(i3, pos3)

        def pList4 = PositionService.getPossibleInsertPositionList(con, i4)
        def pos4 = Helper.findPos(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 != null
    }

    def "All items in same stacking group"() {
        def con = Helper.getAddContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        def pList = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) != null
    }

    def "2 items not in same stacking group"() {
        def con = Helper.getAddContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        def pList = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) == null
    }

    def "1 item not in same stacking group"() {
        def con = Helper.getAddContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        def pList = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) == null
    }

    def "Items can bear multiple stacking groups"() {
        def con = Helper.getAddContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        def pList = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) != null
    }

    def "Items cannot bear multiple stacking groups"() {
        def con = Helper.getAddContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 2)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        def pList = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList, 0, 0, 1, false) == null
    }

    def "check overlapping items"() {
        def con = Helper.getAddContainer(3,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def i1 = Helper.getItem(2, 2, 1, 1, 111,0)
        def i2 = Helper.getItem(3, 1, 1, 1, 111,0 )
        def i3 = Helper.getItem(2, 1, 1, 1, 111,0 )
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        when:
        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def pos2 = Helper.findPos(pList2, 0, 0, 1)
        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def pos3 = Helper.findPos(pList3, 0, 0, 1)
        then:
        pos2 == null
        pos3 != null
    }

    def "Item can be placed on 2 other items"() {
        def nbrOfAllowedItemsBelow = 2

        Container con = Helper.getAddContainer(1,10,2)
        def i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,0, 0))
        i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,2, 0))
        i = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,0, 1))
        // The critical item
        i = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def found = Helper.findPos(pList, 0, 1, 1)

        then:
        found != null
    }

    def "Item can not be placed on 2 other items"() {
        def nbrOfAllowedItemsBelow = 1

        Container con = Helper.getAddContainer(1,10,2)
        def i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,0, 0))
        i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,2, 0))
        i = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,0, 1))
        // The critical item
        i = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def found = Helper.findPos(pList, 0, 1, 1)

        then:
        found == null
    }

    def "Item can be placed only on floor, because no stackable"() {
        def nbrOfAllowedItemsBelow = 0

        Container con = Helper.getAddContainer(1,10,2)
        def i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,0, 0))
        i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,2, 0))
        i = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i), 0,0, 1))
        // The critical item
        i = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)

        then:
        pList.count {p -> p.z > 0} == 0
        pList.count {p -> p.z == 0} > 0
    }
}