package xf.xflp.opt.construction.strategy

import spock.lang.Specification
import xf.xflp.base.problem.Container
import xf.xflp.base.problem.Item
import xf.xflp.base.problem.Position
import xf.xflp.base.problem.RotatedPosition

class StrategySpec extends Specification {

    def itemIdx = 0

    def serviceHLL = new HighestLowerLeft()
    def serviceTP = new TouchingPerimeter()

    def "HLL chooses higher ground"() {
        def con = getContainer(3,4,2)
        def i1 = getItem(1,1,1,1,111,0)
        def i2 = getItem(1,1,1,1,111,0)
        def i3 = getItem(1,1,1,1,111,0)
        def i4 = getItem(1,1,1,1,111,0)

        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1,0,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 0,1,0))

        when:
        def result = serviceHLL.choose(i4, con, con.getPossibleInsertPositionList(i4))
        then:
        result.x == 0 && result.y == 0 && result.z == 1
    }

    def "HLL chooses next stack"() {
        def con = getContainer(3,3,1)
        def i1 = getItem(1,1,1,1,111,0)
        def i2 = getItem(1,1,1,1,111,0)
        def i3 = getItem(1,1,1,1,111,0)
        def i4 = getItem(1,1,1,1,111,0)

        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 1,0,0))

        when:
        def result = serviceHLL.choose(i4, con, con.getPossibleInsertPositionList(i4))
        then:
        result.x == 1 && result.y == 1 && result.z == 0
    }

    def "HLL with positionList = null "() {
        def con = getContainer(3,3,1)
        def i1 = getItem(1,1,1,1,111,0)

        when:
        serviceHLL.choose(i1, con, null)
        then:
        thrown(IllegalStateException)
    }

    def "HLL with positionList = empty "() {
        def con = getContainer(3,3,1)
        def i1 = getItem(1,1,1,1,111,0)

        when:
        serviceHLL.choose(i1, con, [])
        then:
        thrown(IllegalStateException)
    }

    def "TP chooses corner"() {
        def con = getContainer(3,4,3)
        def i1 = getItem(1,1,1,1,111,0)
        def i2 = getItem(1,1,1,1,111,0)
        def i3 = getItem(1,1,1,1,111,0)
        def i4 = getItem(1,1,1,1,111,0)

        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1,0,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 0,1,0))

        when:
        def result = serviceTP.choose(i4, con, con.getPossibleInsertPositionList(i4))
        then:
        result.x == 2 && result.y == 0 && result.z == 0
    }

    def "TP chooses HLL if all equal"() {
        def con = getContainer(3,4,3)
        def i1 = getItem(1,1,1,1,111,0)
        def i2 = getItem(1,1,1,1,111,0)
        def i3 = getItem(1,1,1,1,111,0)
        def i4 = getItem(1,1,1,1,111,0)
        def i5 = getItem(1,1,1,1,111,0)

        con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 1,0,0))
        con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 2,0,0))
        con.add(i4, findPos(con.getPossibleInsertPositionList(i4), 0,1,0))

        when:
        def result = serviceTP.choose(i5, con, con.getPossibleInsertPositionList(i5))
        then:
        result.x == 0 && result.y == 0 && result.z == 1
    }

    def "TP with positionList = null "() {
        def con = getContainer(3,3,1)
        def i1 = getItem(1,1,1,1,111,0)

        when:
        serviceTP.choose(i1, con, null)
        then:
        thrown(IllegalStateException)
    }

    def "TP with positionList = empty "() {
        def con = getContainer(3,3,1)
        def i1 = getItem(1,1,1,1,111,0)

        when:
        serviceTP.choose(i1, con, [])
        then:
        thrown(IllegalStateException)
    }

    private Container getContainer(int width, int length, int height) {
        return new Container(width, length, height, 999999999, 0, 0);
    }
    private Container getContainer(int width, int length, int height, float maxWeight) {
        return new Container(width, length, height, maxWeight, 0, 0);
    }

    private Item getItem(int w, int l, int h, int ww, long wC, int sG) {
        return getItem(w, l, h, ww, wC, sG, Math.max(1,sG));
    }
    private Item getItem(int w, int l, int h, int ww, long wC, int sG, int allowedSG) {
        Set<Integer> set = new HashSet<>();
        set.add(0);

        Item i = new Item(
                itemIdx,
                itemIdx,
                1,
                2,
                w,
                l,
                h,
                ww,
                wC,
                set,
                Math.max(1,sG),
                allowedSG,
                true,
                true
        );
        itemIdx++;

        return i;
    }

    private Position findPos(List<Position> pList, int x, int y, int z) {
        return findPos(pList, x, y, z, false);
    }

    private Position findPos(List<Position> pList, int x, int y, int z, boolean rotated) {
        for (Position position : pList) {
            if(position.getX() == x && position.getY() == y && position.getZ() == z)
                if((rotated && position instanceof RotatedPosition)
                        || (!rotated && !(position instanceof RotatedPosition)))
                    return position;
        }

        return null;
    }
}
