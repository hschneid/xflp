package xf.xflp.base.position

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.ParameterType

class PositionServiceExtendedSpec extends Specification {

    // A square item (w == l) should not produce a rotated candidate because rotation would be identical.
    def "square item does not produce rotated candidate"() {
        def con = Helper.getAddSpaceContainer2(4,4,2)
        def i1 = Helper.getPlacedItem(2, 2, 1, 1, 100, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)

        then:
        pList.size() == 1
        !pList[0].isRotated
    }

    // A non-spinable item should never produce a rotated candidate.
    def "non-spinable item does not produce rotated candidate"() {
        def con = Helper.getAddSpaceContainer2(4,4,2)
        def i1 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        i1.item.spinable = false

        when:
        def pList = PositionService.findPositionCandidates(con, i1)

        then:
        pList.every { !it.isRotated }
    }

    // An item exceeding the container max weight should not have any valid position.
    def "item exceeds container max weight - no positions"() {
        def con = Helper.getAddSpaceContainer2(3, 3, 3, 10)
        def i1 = Helper.getPlacedItem(1, 1, 1, 11, 100, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)

        then:
        pList.size() == 0
    }

    // Two items together exceeding container max weight - second item should have no position.
    def "two items together exceed container max weight"() {
        def con = Helper.getAddSpaceContainer2(3, 3, 3, 10)
        def i1 = Helper.getPlacedItem(1, 1, 1, 6, 100, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 5, 100, 0)

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)

        then:
        pList.size() == 0
    }

    // An item that exactly matches the container max weight should still be valid.
    def "item exactly matches container max weight"() {
        def con = Helper.getAddSpaceContainer2(3, 3, 3, 10)
        def i1 = Helper.getPlacedItem(1, 1, 1, 10, 100, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)

        then:
        pList.size() > 0
    }

    // An item too wide for the container should have no valid position.
    def "item too wide for container"() {
        def con = Helper.getAddSpaceContainer2(3, 3, 3)
        def i1 = Helper.getPlacedItem(4, 1, 1, 1, 100, 0)
        i1.item.spinable = false

        when:
        def pList = PositionService.findPositionCandidates(con, i1)

        then:
        pList.size() == 0
    }

    // An item too tall for the container should have no valid position.
    def "item too tall for container"() {
        def con = Helper.getAddSpaceContainer2(3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 4, 1, 100, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)

        then:
        pList.size() == 0
    }

    // LIFO check should block a position if a higher-priority unload item is in the corridor.
    def "LIFO blocks position when unload corridor is occupied"() {
        def con = Helper.getAddSpaceContainer2(3, 10, 1)
        con.parameter.add(ParameterType.LIFO_IMPORTANCE, 1.0f as float)

        // i1: loads at loc 0, unloads at loc 1 (earlier unload)
        def i1 = Helper.getPlacedItem(3, 2, 1, 1, 100, 0)
        i1.item.setLoadingLoc(0)
        i1.item.setUnLoadingLoc(1)
        Helper.add(con, i1, 0, 0, 0)

        // i2: loads at loc 0, unloads at loc 5 (later unload)
        // Should not be blocked because it unloads later than i1
        def i2 = Helper.getPlacedItem(3, 2, 1, 1, 100, 0)
        i2.item.setLoadingLoc(0)
        i2.item.setUnLoadingLoc(5)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 2, 0)

        then:
        found == null
    }

    // When LIFO importance is 0, items should be placed without LIFO restriction.
    def "LIFO importance 0 - no LIFO restriction"() {
        def con = Helper.getAddSpaceContainer2(3, 10, 1)
        con.parameter.add(ParameterType.LIFO_IMPORTANCE, 0.0f as float)

        def i1 = Helper.getPlacedItem(3, 2, 1, 1, 100, 0)
        i1.item.setLoadingLoc(0)
        i1.item.setUnLoadingLoc(1)
        Helper.add(con, i1, 0, 0, 0)

        def i2 = Helper.getPlacedItem(3, 2, 1, 1, 100, 0)
        i2.item.setLoadingLoc(0)
        i2.item.setUnLoadingLoc(5)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 2, 0)

        then:
        found != null
    }
}

