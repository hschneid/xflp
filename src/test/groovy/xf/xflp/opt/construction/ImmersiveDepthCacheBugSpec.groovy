package xf.xflp.opt.construction

import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.base.container.GroundContactRule
import xf.xflp.opt.XFLPOptType
import xf.xflp.opt.construction.strategy.Strategy
import xf.xflp.report.LoadType

/**
 * Reproduces a bug related to the immersiveDepthCache.
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
 * Observed bug:
 *  The first stack is stacked 3 times, the second stack only 2 times.
 *  So XFLP plans 3 stacks instead of 2.
 */
class ImmersiveDepthCacheBugSpec extends Specification {

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

        // Helpful debug output when the assertion fails
        println "Number of stacks: " + stacks.size()
        stacks.each { key, events ->
            println "Stack at x=${key[0]}, y=${key[1]} -> height ${events.size()} : " +
                    events.collect { "z=${it.z()},h=${it.h()}" }
        }

        stacks.size() == 2
        stacks.values().every { it.size() == 3 }
    }
}



