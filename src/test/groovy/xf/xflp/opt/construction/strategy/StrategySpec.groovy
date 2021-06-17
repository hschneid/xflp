package xf.xflp.opt.construction.strategy

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.problem.Position

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

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1,0,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 0,1,0))

        when:
        def result = serviceHLL.choose(i4, con, con.getPossibleInsertPositionList(i4))
        then:
        result.x == 0 && result.y == 0 && result.z == 1
    }

    def "HLL chooses next stack"() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)
        def i2 = Helper.getItem(1,1,1,1,111,0)
        def i3 = Helper.getItem(1,1,1,1,111,0)
        def i4 = Helper.getItem(1,1,1,1,111,0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 1,0,0))

        when:
        def result = serviceHLL.choose(i4, con, con.getPossibleInsertPositionList(i4))
        then:
        result.x == 1 && result.y == 1 && result.z == 0
    }

    def "HLL with positionList = null "() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)

        when:
        serviceHLL.choose(i1, con, null)
        then:
        thrown(IllegalStateException)
    }

    def "HLL with positionList = empty "() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)

        when:
        serviceHLL.choose(i1, con, [])
        then:
        thrown(IllegalStateException)
    }

    def "TP chooses corner"() {
        def con = Helper.getContainer(3,4,3)
        def i1 = Helper.getItem(1,1,1,1,111,0)
        def i2 = Helper.getItem(1,1,1,1,111,0)
        def i3 = Helper.getItem(1,1,1,1,111,0)
        def i4 = Helper.getItem(1,1,1,1,111,0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1,0,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 0,1,0))

        when:
        def result = serviceTP.choose(i4, con, con.getPossibleInsertPositionList(i4))
        then:
        result.x == 2 && result.y == 0 && result.z == 0
    }

    def "TP chooses HLL if all equal"() {
        def con = Helper.getContainer(3,4,3)
        def i1 = Helper.getItem(1,1,1,1,111,0)
        def i2 = Helper.getItem(1,1,1,1,111,0)
        def i3 = Helper.getItem(1,1,1,1,111,0)
        def i4 = Helper.getItem(1,1,1,1,111,0)
        def i5 = Helper.getItem(1,1,1,1,111,0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 1,0,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 2,0,0))
        con.add(i4, Helper.findPos(con.getPossibleInsertPositionList(i4), 0,1,0))

        when:
        def result = serviceTP.choose(i5, con, con.getPossibleInsertPositionList(i5))
        then:
        result.x == 0 && result.y == 0 && result.z == 1
    }

    def "getPositionWithMinValue - only one value"() {
        def func = {
            "1234"
        } as Function<Position, Float>
        def pos = Mock Position
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
        thrown(IllegalStateException)
    }

    def "TP with positionList = empty "() {
        def con = Helper.getContainer(3,3,1)
        def i1 = Helper.getItem(1,1,1,1,111,0)

        when:
        serviceTP.choose(i1, con, [])
        then:
        thrown(IllegalStateException)
    }

}
