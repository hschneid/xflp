package xf.xflp.base.problem

import helper.Helper
import spock.lang.Specification

class ContainerExtendedStackingSpec extends Specification {

    def "Item can be placed on 2 other items"() {
        def nbrOfAllowedItemsBelow = 2

        ComplexContainer con = Helper.getContainer(1,10,2)
        def i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,0, 0))
        i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,2, 0))
        i = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,0, 1))
        // The critical item
        i = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        when:
        def pList = con.getPossibleInsertPositionList(i)
        def found = Helper.findPos(pList, 0, 1, 1)

        then:
        found != null
    }

    def "Item can not be placed on 2 other items"() {
        def nbrOfAllowedItemsBelow = 1

        ComplexContainer con = Helper.getContainer(1,10,2)
        def i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,0, 0))
        i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,2, 0))
        i = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,0, 1))
        // The critical item
        i = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        when:
        def pList = con.getPossibleInsertPositionList(i)
        def found = Helper.findPos(pList, 0, 1, 1)

        then:
        found == null
    }

    def "Item can be placed only on floor, because no stackable"() {
        def nbrOfAllowedItemsBelow = 0

        ComplexContainer con = Helper.getContainer(1,10,2)
        def i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,0, 0))
        i = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,2, 0))
        i = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, Helper.findPos(con.getPossibleInsertPositionList(i), 0,0, 1))
        // The critical item
        i = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        when:
        def pList = con.getPossibleInsertPositionList(i)

        then:
        pList.count {p -> p.z > 0} == 0
        pList.count {p -> p.z == 0} > 0
    }

}
