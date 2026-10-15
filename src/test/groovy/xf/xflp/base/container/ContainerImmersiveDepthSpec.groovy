package xf.xflp.base.container

import helper.Helper
import spock.lang.Specification
import spock.lang.Unroll
import xf.xflp.XFLP
import xf.xflp.base.item.PlacedItem
import xf.xflp.base.position.PositionService
import xf.xflp.opt.XFLPOptType
import xf.xflp.opt.construction.strategy.Strategy
import xf.xflp.report.LoadType

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
        i1.item = i1.item.withImmersiveDepth(2)
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
        i1.item = i1.item.withImmersiveDepth(2)
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
        i1.item = i1.item.withImmersiveDepth(2)
        def i2 = Helper.getPlacedItem(1, 1, 12, 1, 10, 0)
        i2.item = i2.item.withImmersiveDepth(1)
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
        i1.item = i1.item.withImmersiveDepth(2)
        def i2 = Helper.getPlacedItem(1, 1, 10, 1, 10, 0)
        i2.item = i2.item.withImmersiveDepth(1)
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
        i1.item = i1.item.withImmersiveDepth(2)
        def i2 = Helper.getPlacedItem(1, 1, 12, 1, 10, 0)
        i2.item = i2.item.withImmersiveDepth(1)
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
    //  Full planning regression test (immersive depth cache bug)
    // ------------------------------------------------------------------

    /**
     * Reproduces a bug related to the immersive depth lookup during stacking.
     *
     * Input:
     *  - A truck with 2 identical loading spaces (Jumbo-WB) of 7650 x 2480 x 3000 mm, 11000 kg each.
     *  - 6 identical items of 2000 x 1200 x 1014 mm with an immersiveDepth of 30 mm.
     *
     * Parameters:
     *  - FAST_FIXED_CONTAINER_PACKER
     *  - WIDTH_PROPORTION
     *  - GroundContactRule.COVERED
     *
     * Expectation:
     *  The 6 boxes are planned onto 2 stacks (3 boxes each) inside the first loading space.
     *
     * Formerly observed bug:
     *  The first stack is stacked 3 times, the second stack only 2 times, so XFLP planned
     *  3 stacks instead of 2. This was caused by stale values in the (now removed)
     *  position-object-keyed immersive depth cache.
     */
    def "6 immersive-depth items should be planned onto 2 stacks in the first loading space"() {
        given:
        def xflp = new XFLP()

        // The truck has 2 identical loading spaces (Jumbo-WB)
        2.times {
            xflp.addContainer()
                    .setContainerType("Jumbo-WB 2*(7,65 x 2,48 x 3,0)")
                    .setLength(7650)
                    .setWidth(2480)
                    .setHeight(3000)
                    .setMaxWeight(11000f)
        }

        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
        xflp.getParameter().setPreferredPackingStrategy(Strategy.WIDTH_PROPORTION)
        xflp.getParameter().setGroundContactRule(GroundContactRule.COVERED)

        // 6 identical items with immersive depth of 30 mm
        for (int i = 0; i < 6; i++) {
            xflp.addItem()
                    .setExternID("item-" + i)
                    .setLength(2000)
                    .setWidth(1200)
                    .setHeight(1014)
                    .setImmersiveDepth(30)
                    .setWeight(100f)
                    .setStackingWeightLimit(Float.MAX_VALUE)
        }

        when:
        xflp.executeLoadPlanning()
        def report = xflp.getReport()

        then:
        report != null
        report.getUnplannedPackages().size() == 0

        and: "all items are planned into the first loading space"
        report.getContainerReports().size() == 1

        and: "the 6 items are placed onto exactly 2 stacks (3 boxes each)"
        def loadEvents = report.getContainerReports()
                .get(0)
                .getPackageEvents()
                .findAll { it.type() == LoadType.LOAD }

        // Group placed items by their ground footprint (x/y) -> one group per stack
        def stacks = loadEvents.groupBy { [it.x(), it.y()] }

        stacks.size() == 2
        stacks.values().every { it.size() == 3 }
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

