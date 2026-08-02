package xf.xflp.opt.construction.strategy

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.position.PositionService
import xf.xflp.exception.XFLPException

class SameBaseStrategySpec extends Specification {

    def service = new SameBaseStrategy()

    // When two items of the same base are stacked, the strategy should prefer the position on top of the matching base.
    def "same base item is placed on matching stack"() {
        def con = Helper.getAddSpaceContainer2(4,4,3)
        def i1 = Helper.getPlacedItem(2,2,1,1,111,0)
        def i2 = Helper.getPlacedItem(1,1,1,1,111,0)
        def i3 = Helper.getPlacedItem(2,2,1,1,111,0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 2, 0, 0)

        when:
        def candidates = PositionService.findPositionCandidates(con, i3)
        def result = service.choose(i3, con, candidates)

        then:
        result != null
        result.position().z() == 1
    }

    // When no matching base exists, the strategy falls back to the default (HLL/widthProportion) which chooses a ground position instead of stacking.
    def "no matching base - falls back to default strategy"() {
        def con = Helper.getAddSpaceContainer2(4,4,3)
        // Place a 1x1 item at ground - the new 2x2 item has a different base
        def i1 = Helper.getPlacedItem(1,1,1,1,111,0)
        def i2 = Helper.getPlacedItem(2,2,1,1,111,0)

        Helper.add(con, i1, 0, 0, 0)

        when:
        def candidates = PositionService.findPositionCandidates(con, i2)
        def result = service.choose(i2, con, candidates)

        then:
        result != null
        // The fallback strategy places at z=0 because no base matches for stacking
        result.position().z() == 0
    }

    // An empty position list should throw an exception.
    def "empty position list throws exception"() {
        def con = Helper.getAddSpaceContainer2(4,4,3)
        def i1 = Helper.getPlacedItem(1,1,1,1,111,0)

        when:
        service.choose(i1, con, [])

        then:
        thrown(XFLPException)
    }

    // A null position list should throw an exception.
    def "null position list throws exception"() {
        def con = Helper.getAddSpaceContainer2(4,4,3)
        def i1 = Helper.getPlacedItem(1,1,1,1,111,0)

        when:
        service.choose(i1, con, null)

        then:
        thrown(XFLPException)
    }
}


