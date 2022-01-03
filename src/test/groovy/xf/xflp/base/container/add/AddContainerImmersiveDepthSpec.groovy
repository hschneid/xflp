package xf.xflp.base.container.add

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container
import xf.xflp.base.item.Item
import xf.xflp.base.position.PositionService

class AddContainerImmersiveDepthSpec extends Specification {

    def "only immersive depth, item is fitting, just adding"() {
        Container con = Helper.getAddContainer(2,2,20)
        def i1 = Helper.getItem(1, 1, 10, 1, 10, 0)
        i1.immersiveDepth = 2
        con.add(i1, PositionService.getPossibleInsertPositionList(con, i1).get(0))

        def i2 = Helper.getItem(1, 1, 12, 1, 10, 0)

        when:
        def found = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0,0, 10)
        con.add(i2, found)
        then:
        found != null
        check(i2, 10, 10, 20, 12)
    }

    def "even with immersive depth, item is not fitting"() {
        Container con = Helper.getAddContainer(2,2,19)
        def i1 = Helper.getItem(1, 1, 10, 1, 10, 0)
        i1.immersiveDepth = 2
        con.add(i1, PositionService.getPossibleInsertPositionList(con, i1).get(0))

        def i2 = Helper.getItem(1, 1, 12, 1, 10, 0)

        when:
        def found = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0,0, 10)
        then:
        found == null
    }

    def "stack 3 items with immersive depth"() {
        Container con = Helper.getAddContainer(2,2,30)
        def i1 = Helper.getItem(1, 1, 10, 1, 10, 0)
        i1.immersiveDepth = 2
        con.add(i1, PositionService.getPossibleInsertPositionList(con, i1).get(0))
        def i2 = Helper.getItem(1, 1, 12, 1, 10, 0)
        i2.immersiveDepth = 1
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0,0, 10))

        def i3 = Helper.getItem(1, 1, 11, 1, 10, 0)

        when:
        def found = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0,0, 20)
        con.add(i3, found)
        then:
        found != null
        check(i2, 10, 10, 20, 12)
        check(i3, 10, 20, 30, 11)
    }

    def "stack item on multiple items, with different immersive depth"() {
        Container con = Helper.getAddContainer(3,3,30)
        def i1 = Helper.getItem(1, 1, 10, 1, 10, 0)
        i1.immersiveDepth = 2
        def i2 = Helper.getItem(1, 1, 10, 1, 10, 0)
        i2.immersiveDepth = 1
        def i3 = Helper.getItem(2, 1, 13, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 10, 1, 10, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0,0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1,0, 0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 1,1, 0))

        when:
        def found = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0,0, 10)
        con.add(i3, found)

        then:
        found != null
        check(i3, 12, 10, 22, 13)
    }

    static void check(Item item, int h, int z, int zh, int origH) {
        assert item.h == h
        assert item.z == z
        assert item.zh == zh
        assert item.origH == origH
    }

}
