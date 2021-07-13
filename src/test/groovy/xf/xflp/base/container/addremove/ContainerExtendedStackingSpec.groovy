package xf.xflp.base.container.addremove

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.ComplexContainer
import xf.xflp.base.container.Container
import xf.xflp.base.position.PositionService

class ContainerExtendedStackingSpec extends Specification {

    def "Item can be placed on 2 other items"() {
        def nbrOfAllowedItemsBelow = 2

        Container con = Helper.getContainer2(1,10,2)
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

        Container con = Helper.getContainer2(1,10,2)
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

        Container con = Helper.getContainer2(1,10,2)
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
