package xf.xflp.opt.construction.strategy

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.position.PositionCandidate
import xf.xflp.base.position.PositionService
import xf.xflp.exception.XFLPException

import java.util.function.Function

class StrategySpec extends Specification {

    def serviceHLL = new HighestLowerLeft()
    def serviceTP = new TouchingPerimeter()

    def "HLL chooses higher ground"() {
        def con = Helper.getContainer(3,4,2)
        def i1 = Helper.getItem(1,1,1,1,111,0)
        def i2 = Helper.getItem(1,1,1,1,111,0)
        def i3 = Helper.getItem(1,1,1,1,111,0)
        def i4 = Helper.getItem(1,1,1,1,111,0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)

        when:
        def result = serviceHLL.choose(i4, con, PositionService.findPositionCandidates(con, i4))
        then:
        Helper.findCand([result], 0, 0, 1) != null
    }

    def "HLL chooses next stack"() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)
        def i2 = Helper.getItem(1,1,1,1,111,0)
        def i3 = Helper.getItem(1,1,1,1,111,0)
        def i4 = Helper.getItem(1,1,1,1,111,0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 1, 0, 0)

        when:
        def result = serviceHLL.choose(i4, con, PositionService.findPositionCandidates(con, i4))
        then:
        Helper.findCand([result], 1, 1, 0) != null
    }

    def "HLL with positionList = null "() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)

        when:
        serviceHLL.choose(i1, con, null)
        then:
        thrown(XFLPException)
    }

    def "HLL with positionList = empty "() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)

        when:
        serviceHLL.choose(i1, con, [])
        then:
        thrown(XFLPException)
    }

    def "TP chooses corner"() {
        def con = Helper.getContainer(3,4,3)
        def i1 = Helper.getItem(1,1,1,1,111,0)
        def i2 = Helper.getItem(1,1,1,1,111,0)
        def i3 = Helper.getItem(1,1,1,1,111,0)
        def i4 = Helper.getItem(1,1,1,1,111,0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)

        when:
        def result = serviceTP.choose(i4, con, PositionService.findPositionCandidates(con, i4))
        then:
        Helper.findCand([result], 2, 0, 0) != null
    }

    def "TP chooses HLL if all equal"() {
        def con = Helper.getContainer(3,4,3)
        def i1 = Helper.getItem(1,1,1,1,111,0)
        def i2 = Helper.getItem(1,1,1,1,111,0)
        def i3 = Helper.getItem(1,1,1,1,111,0)
        def i4 = Helper.getItem(1,1,1,1,111,0)
        def i5 = Helper.getItem(1,1,1,1,111,0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 2, 0, 0)
        Helper.add(con, i4, 0, 1, 0)

        when:
        def result = serviceTP.choose(i5, con, PositionService.findPositionCandidates(con, i5))
        then:
        Helper.findCand([result], 0, 0, 1) != null
    }

    def "getPositionWithMinValue - only one value"() {
        def func = {
            "1234"
        } as Function<PositionCandidate, Float>
        def pos = Mock PositionCandidate
        def posList = [pos]
        def emptyList = []
        when:
        def result = serviceHLL.getPositionWithMinValue(posList, func)
        def emptyResult = serviceHLL.getPositionWithMinValue(emptyList, func)
        def nullResult = serviceHLL.getPositionWithMinValue(null, func)
        then:
        0 * func.apply(_)
        result == posList
        emptyResult == emptyList
        nullResult.size() == 0
    }

    def "TP with positionList = null "() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)

        when:
        serviceTP.choose(i1, con, null)
        then:
        thrown(XFLPException)
    }

    def "TP with positionList = empty "() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)

        when:
        serviceTP.choose(i1, con, [])
        then:
        thrown(XFLPException)
    }

}
