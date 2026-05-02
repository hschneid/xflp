package xf.xflp.base.container

import helper.Helper
import spock.lang.Specification
import spock.lang.Unroll
import xf.xflp.base.position.PositionService

/**
 * Consolidated tests that verify both AddContainer and AddRemoveContainer
 * behave identically for common add/find operations.
 *
 * This spec replaces the duplicated tests found in:
 *   - addspace.AddContainerBaseSpec
 *   - addremove.ContainerBaseSpec
 *
 * Each test is executed for both container types via @Unroll.
 */
class ContainerBaseSpec extends Specification {

    private static Container createContainer(String type, int w, int l, int h) {
        return createContainer(type, w, l, h, Integer.MAX_VALUE)
    }

    private static Container createContainer(String type, int w, int l, int h, float maxWeight) {
        if (type == "Add") return Helper.getAddSpaceContainer(w, l, h, maxWeight)
        return Helper.getAddSpaceContainer2(w, l, h, maxWeight)
    }

    // ------------------------------------------------------------------
    //  Consolidated sequential fill tests (1st → 6th item)
    //  The "sixth item" test dominates all previous incremental steps.
    //  We combine them into one test that checks every intermediate state.
    // ------------------------------------------------------------------

    @Unroll
    def "fill 2x2x2 container step by step (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i5 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        def i6 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)

        when: "find first position in empty container"
        def pList1 = PositionService.findPositionCandidates(con, i1)

        then:
        pList1.size() == 1
        Helper.findCand(pList1, 0, 0, 0) != null
        con.getItems().size() == 0

        when: "add first item"
        Helper.add(con, pList1.get(0))

        then:
        con.getItems().size() == 1
        i1.x == 0 && i1.y == 0 && i1.z == 0

        when: "find second position"
        def pList2 = PositionService.findPositionCandidates(con, i2)

        then:
        pList2.size() == 3
        Helper.findCand(pList2, 0, 0, 0) == null
        Helper.findCand(pList2, 1, 0, 0) != null
        Helper.findCand(pList2, 0, 1, 0) != null
        Helper.findCand(pList2, 0, 0, 1) != null

        when: "add second item"
        Helper.add(con, Helper.findCand(pList2, 1, 0, 0))

        then:
        con.getItems().size() == 2
        i2.x == 1 && i2.y == 0 && i2.z == 0

        when: "find and add third item"
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pos3 = Helper.findCand(pList3, 0, 1, 0)
        def has3z = Helper.findCand(pList3, 0, 0, 1) != null
        Helper.add(con, pos3)

        then:
        con.getItems().size() == 3
        pos3 != null
        has3z

        when: "find and add fourth item"
        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 0, 1)
        def has4a = Helper.findCand(pList4, 1, 0, 1) != null
        def has4b = Helper.findCand(pList4, 0, 1, 1) != null
        Helper.add(con, pos4)

        then:
        con.getItems().size() == 4
        pos4 != null
        has4a
        has4b

        when: "find and add fifth item"
        def pList5 = PositionService.findPositionCandidates(con, i5)
        def pos5 = Helper.findCand(pList5, 1, 0, 1)
        Helper.add(con, pos5)

        then:
        con.getItems().size() == 5
        pos5 != null

        when: "find and add sixth (last) item"
        def pList6 = PositionService.findPositionCandidates(con, i6)
        def pos6 = Helper.findCand(pList6, 0, 1, 1)
        def hasOther = pList6.findAll { p ->
            !(p.position.x == 0 && p.position.y == 1 && p.position.z == 1)
        }.size() > 0
        Helper.add(con, pos6)

        then:
        con.getItems().size() == 6
        pos6 != null
        !hasOther

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Rotation
    // ------------------------------------------------------------------

    @Unroll
    def "find rotated insert position (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 2)
        def i = Helper.getPlacedItem(2, 1, 1, 1, 10, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)

        then:
        pList.size() == 2
        pList.find { p -> p.position.x == 0 && p.position.y == 0 && p.position.z == 0 && p.isRotated } != null
        pList.find { p -> p.position.x == 0 && p.position.y == 0 && p.position.z == 0 && !p.isRotated } != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test rotation with spinable flag (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 1)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        i3.item = i3.item.withSpinable(false)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pList3 = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList2, 1, 0, 0, true) != null
        Helper.findCand(pList2, 0, 1, 0, false) != null
        Helper.findCand(pList3, 1, 0, 0, true) == null
        Helper.findCand(pList3, 0, 1, 0, false) != null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Too-big items
    // ------------------------------------------------------------------

    @Unroll
    def "do not find insert positions for too big items (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 2, 10)
        def i1 = Helper.getPlacedItem(3, 1, 1, 1, 10, 0)
        def i2 = Helper.getPlacedItem(1, 3, 1, 1, 10, 0)
        def i3 = Helper.getPlacedItem(1, 1, 3, 1, 10, 0)
        def i4 = Helper.getPlacedItem(1, 1, 1, 11, 10, 0)
        def i5 = Helper.getPlacedItem(1, 1, 1, 9, 10, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i1)
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pList5 = PositionService.findPositionCandidates(con, i5)

        then:
        pList1.size() == 0
        pList2.size() == 0
        pList3.size() == 0
        pList4.size() == 0
        pList5.size() > 0

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Covered positions – the AddRemoveContainer variant dominates
    //  the AddContainer variant (checks more internal lists).
    //  We test both container types but assert the common invariant:
    //  the covered position must NOT be in activePositions.
    // ------------------------------------------------------------------

    @Unroll
    def "test covered positions - X axis (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)

        then:
        Helper.findPos(con.getActivePositions(), 1, 1, 0) == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test covered positions - Y axis (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 1, 0, 0)

        then:
        Helper.findPos(con.getActivePositions(), 1, 1, 0) == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test covered positions - Z-X axis (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 0, 1)

        then:
        Helper.findPos(con.getActivePositions(), 1, 0, 1) == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test covered positions - Z-Y axis (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 0, 0, 1)

        then:
        Helper.findPos(con.getActivePositions(), 0, 1, 1) == null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Full ground coverage
    // ------------------------------------------------------------------

    @Unroll
    def "test full coverage of item ground (#type)"() {
        given:
        def con = createContainer(type, 5, 1, 3)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def i1 = Helper.getPlacedItem(1, 1, 2, 1, 100, 0)
        def i2 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(2, 1, 2, 1, 100, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 3, 0, 0)

        when:
        def i4 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def pos4 = Helper.findCand(PositionService.findPositionCandidates(con, i4), 0, 0, 2, false)
        def i5 = Helper.getPlacedItem(3, 1, 1, 1, 100, 0)
        def pos5 = Helper.findCand(PositionService.findPositionCandidates(con, i5), 0, 0, 2, false)
        def i6 = Helper.getPlacedItem(4, 1, 1, 1, 100, 0)
        def pos6 = Helper.findCand(PositionService.findPositionCandidates(con, i6), 0, 0, 2, false)

        then:
        pos4 == null
        pos5 == null
        pos6 != null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Projection tests
    // ------------------------------------------------------------------

    @Unroll
    def "test horizontal projection of insert position (#type)"() {
        given:
        def con = createContainer(type, 3, 5, 2)
        def i1 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(1, 3, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(3, 2, 1, 1, 100, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 2, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 3, 0) != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test vertical projection of insert position (#type)"() {
        given:
        def con = createContainer(type, 3, 5, 2)
        def i1 = Helper.getPlacedItem(1, 2, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(1, 3, 1, 1, 100, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 2, 0, 0) != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test vertical projection of insert position at other box (#type)"() {
        given:
        def con = createContainer(type, 4, 6, 2)
        def i1 = Helper.getPlacedItem(3, 2, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getPlacedItem(2, 3, 1, 1, 100, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 4, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)

        then:
        Helper.findCand(pList, 2, 2, 0) != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "big test projected insert positions (#type)"() {
        given:
        def con = createContainer(type, 4, 6, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 1, 2, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)

        then:
        // Normal positions
        Helper.findCand(pList, 0, 1, 0) != null
        Helper.findCand(pList, 1, 3, 0) != null
        Helper.findCand(pList, 3, 2, 0) != null
        Helper.findCand(pList, 2, 0, 0) != null
        // Horizontal projected positions
        Helper.findCand(pList, 0, 2, 0) != null
        Helper.findCand(pList, 0, 3, 0) != null
        // Vertical projected position
        Helper.findCand(pList, 3, 0, 0) != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test horizontal projection of insert position with space limiting box (#type)"() {
        given:
        def con = createContainer(type, 4, 6, 2)
        con.getParameter().add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getPlacedItem(1, 3, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getPlacedItem(2, 2, 1, 1, 100, 0)
        def i5 = Helper.getPlacedItem(2, 1, 1, 1, 100, 0)
        def i6 = Helper.getPlacedItem(1, 3, 1, 1, 100, 0)
        def i7 = Helper.getPlacedItem(3, 1, 1, 1, 100, 0)
        def i8 = Helper.getPlacedItem(3, 2, 1, 1, 100, 0)
        def i9 = Helper.getPlacedItem(3, 1, 2, 1, 100, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 3, 0)
        Helper.add(con, i3, 0, 4, 0)
        Helper.add(con, i4, 0, 3, 1)
        Helper.add(con, i5, 1, 0, 0)
        Helper.add(con, i6, 3, 0, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i7)
        def pList2 = PositionService.findPositionCandidates(con, i8)
        def pList3 = PositionService.findPositionCandidates(con, i9)

        then:
        Helper.findCand(pList1, 1, 3, 0) != null
        Helper.findCand(pList2, 1, 3, 0) == null
        Helper.findCand(pList3, 1, 3, 0) == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "test vertical projection of insert position at other box with space limiting box (#type)"() {
        given:
        def con = createContainer(type, 6, 4, 2)
        con.getParameter().add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getPlacedItem(3, 1, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 100, 0)
        def i4 = Helper.getPlacedItem(2, 2, 1, 1, 100, 0)
        def i5 = Helper.getPlacedItem(1, 2, 1, 1, 100, 0)
        def i6 = Helper.getPlacedItem(3, 1, 1, 1, 100, 0)
        def i7 = Helper.getPlacedItem(1, 3, 1, 1, 100, 0)
        def i8 = Helper.getPlacedItem(2, 3, 1, 1, 100, 0)
        def i9 = Helper.getPlacedItem(1, 3, 2, 1, 100, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 3, 0, 0)
        Helper.add(con, i3, 4, 0, 0)
        Helper.add(con, i4, 3, 0, 1)
        Helper.add(con, i5, 0, 1, 0)
        Helper.add(con, i6, 0, 3, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i7)
        def pList2 = PositionService.findPositionCandidates(con, i8)
        def pList3 = PositionService.findPositionCandidates(con, i9)

        then:
        Helper.findCand(pList1, 3, 1, 0) != null
        Helper.findCand(pList2, 3, 1, 0) == null
        Helper.findCand(pList3, 3, 1, 0) == null

        where:
        type << ["Add", "AddRemove"]
    }
}
