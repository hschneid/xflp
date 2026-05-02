package xf.xflp.base.container

import helper.Helper
import spock.lang.Specification
import spock.lang.Unroll
import xf.xflp.base.item.PlacedItem
import xf.xflp.base.position.PositionService

/**
 * Consolidated immersive depth tests that verify both AddContainer and AddRemoveContainer
 * behave identically for common immersive depth operations.
 *
 * This spec replaces the duplicated tests found in:
 *   - addspace.AddContainerImmersiveDepthSpec
 *   - addremove.ContainerImmersiveDepthSpec
 *
 * Each test is executed for both container types via @Unroll.
 */
class ContainerImmersiveDepthSpec extends Specification {

    private static Container createContainer(String type, int w, int l, int h) {
        if (type == "Add") return Helper.getAddSpaceContainer(w, l, h)
        return Helper.getAddSpaceContainer2(w, l, h)
    }

    // ------------------------------------------------------------------
    //  Common tests (both container types)
    // ------------------------------------------------------------------

    @Unroll
    def "only immersive depth, item is fitting, just adding (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 20)
        def i1 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)
        i1.item.immersiveDepth = 2
        Helper.add(con, PositionService.findPositionCandidates(con, i1).get(0))

        def i2 = Helper.getPlacedItem(1, 1, 12, 1, 10, 0)

        when:
        def found = Helper.findCand(PositionService.findPositionCandidates(con, i2), 0, 0, 10)
        Helper.add(con, found)

        then:
        found != null
        check(i2, 10, 10, 20, 12)

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "even with immersive depth, item is not fitting (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 19)
        def i1 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)
        i1.item.immersiveDepth = 2
        Helper.add(con, PositionService.findPositionCandidates(con, i1).get(0))

        def i2 = Helper.getPlacedItem(1, 1, 12, 1, 10, 0)

        when:
        def found = Helper.findCand(PositionService.findPositionCandidates(con, i2), 0, 0, 10)

        then:
        found == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "stack 3 items with immersive depth (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 30)
        def i1 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)
        i1.item.immersiveDepth = 2
        def i2 = Helper.getPlacedItem(1, 1, 12, 1, 10, 0)
        i2.item.immersiveDepth = 1
        def i3 = Helper.getPlacedItem(1, 1, 11, 1, 10, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 0, 10)

        when:
        def found = Helper.findCand(PositionService.findPositionCandidates(con, i3), 0, 0, 20)
        Helper.add(con, found)

        then:
        found != null
        check(i2, 10, 10, 20, 12)
        check(i3, 10, 20, 30, 11)

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "stack item on multiple items, with different immersive depth (#type)"() {
        given:
        Container con = createContainer(type, 3, 3, 30)
        def i1 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)
        i1.item.immersiveDepth = 2
        def i2 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)
        i2.item.immersiveDepth = 1
        def i3 = Helper.getPlacedItem(2, 1, 13, 1, 10, 0)
        def i4 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i4, 1, 1, 0)

        when:
        def found = Helper.findCand(PositionService.findPositionCandidates(con, i3), 0, 0, 10)
        Helper.add(con, found)

        then:
        found != null
        check(i3, 12, 10, 22, 13)

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  AddRemove-only test
    // ------------------------------------------------------------------

    def "add, remove and readd 3 items with immersive depth"() {
        given:
        Container con = Helper.getAddSpaceContainer2(2, 2, 30)
        def i1 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)
        i1.item.immersiveDepth = 2
        def i2 = Helper.getPlacedItem(1, 1, 12, 1, 10, 0)
        i2.item.immersiveDepth = 1
        def i3 = Helper.getPlacedItem(1, 1, 11, 1, 10, 0)

        Helper.add(con, PositionService.findPositionCandidates(con, i1).get(0))
        Helper.add(con, i2, 0, 0, 10)
        Helper.add(con, i3, 0, 0, 20)
        con.remove(i3)
        con.remove(i2)
        con.remove(i1)
        Helper.add(con, PositionService.findPositionCandidates(con, i1).get(0))
        Helper.add(con, i2, 0, 0, 10)

        when:
        def found = Helper.findCand(PositionService.findPositionCandidates(con, i3), 0, 0, 20)
        Helper.add(con, found)

        then:
        found != null
        check(i2, 10, 10, 20, 12)
        check(i3, 10, 20, 30, 11)
    }

    // ------------------------------------------------------------------
    //  Helper
    // ------------------------------------------------------------------

    static void check(PlacedItem item, int h, int z, int zh, int origH) {
        assert item.h == h
        assert item.z == z
        assert item.zh == zh
        assert item.item.origH == origH
    }
}

