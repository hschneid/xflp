package xf.xflp.base.container.addremove

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container
import xf.xflp.base.position.PositionService

class ContainerRemoveSpec extends Specification {

    def "remove an item - a valid item"() {
        Container con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0,0,0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0,1,0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 1,1,0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 2,1,0))

        when:
        con.remove(i3)
        then:
        Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 1,1,0) != null
        con.getLoadedVolume() == 7
        con.getItems().size() == 3
    }

    def "remove an item - an invalid item"() {
        Container con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)

        def i6 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i7 = Helper.getItem(1, 3, 1, 1, 111, 0)
        def i8 = Helper.getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0,0,0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0,1,0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 1,1,0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 2,1,0))
        con.add(i6, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i6), 0,0,1))
        con.add(i7, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i7), 2,0,1))
        con.add(i8, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i8), 0,2,1))

        when:
        con.remove(i3)
        then:
        Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 1,1,0) != null
        con.getLoadedVolume() == 16
        con.getItems().size() == 6
    }

    def "remove everything one by one"() {
        Container con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i5 = Helper.getItem(1, 1, 1, 1, 111, 0)

        def i6 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i7 = Helper.getItem(1, 3, 1, 1, 111, 0)
        def i8 = Helper.getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0,0,0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0,1,0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 2,1,0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 0,2,0))
        con.add(i5, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i5), 1,2,0))
        con.add(i6, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i6), 0,0,1))
        con.add(i7, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i7), 2,0,1))
        con.add(i8, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i8), 0,2,1))

        when:
        con.remove(i1)
        con.remove(i2)
        con.remove(i3)
        con.remove(i4)
        con.remove(i5)
        con.remove(i6)
        con.remove(i7)
        con.remove(i8)
        then:
        con.getLoadedWeight() == 0
        Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getItems().size() == 0
    }
}
