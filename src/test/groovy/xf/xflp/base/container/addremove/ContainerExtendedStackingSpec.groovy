package xf.xflp.base.container.addremove

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container
import xf.xflp.base.position.PositionService

class ContainerExtendedStackingSpec extends Specification {

    def "Item can be placed on 2 other items"() {
        def nbrOfAllowedItemsBelow = 2

        Container con = Helper.getContainer(1,10,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getItem(1, 1, 1, 1, 10, 0)
        // The critical item
        def i4 = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def found = Helper.findCand(pList, 0, 1, 1)

        then:
        found != null
    }

    def "Item can not be placed on 2 other items"() {
        def nbrOfAllowedItemsBelow = 1

        Container con = Helper.getContainer(1,10,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getItem(1, 1, 1, 1, 10, 0)
        // The critical item
        def i4 = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def found = Helper.findCand(pList, 0, 1, 1)

        then:
        found == null
    }

    def "Item can be placed only on floor, because no stackable"() {
        def nbrOfAllowedItemsBelow = 0

        Container con = Helper.getContainer(1,10,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getItem(1, 1, 1, 1, 10, 0)
        // The critical item
        def i4 = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)

        then:
        pList.count {p -> p.position.z > 0} == 0
        pList.count {p -> p.position.z == 0} > 0
    }

}
