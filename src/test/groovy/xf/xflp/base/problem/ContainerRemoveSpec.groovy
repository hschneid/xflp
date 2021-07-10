package xf.xflp.base.problem

import helper.Helper
import spock.lang.Specification

class ContainerRemoveSpec extends Specification {

    def "clear means clear - full container"() {
        ComplexContainer con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i5 = Helper.getItem(1, 1, 1, 1, 111, 0)

        def i6 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i7 = Helper.getItem(1, 3, 1, 1, 111, 0)
        def i8 = Helper.getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 2,1,0))
        con.add(i4, Helper.findPos(con.getPossibleInsertPositionList(i4), 0,2,0))
        con.add(i5, Helper.findPos(con.getPossibleInsertPositionList(i5), 1,2,0))
        con.add(i6, Helper.findPos(con.getPossibleInsertPositionList(i6), 0,0,1))
        con.add(i7, Helper.findPos(con.getPossibleInsertPositionList(i7), 2,0,1))
        con.add(i8, Helper.findPos(con.getPossibleInsertPositionList(i8), 0,2,1))

        when:
        con.clear()
        then:
        con.getLoadedWeight() == 0
        Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getNbrOfLoadedThings() == 0
    }

    def "clear means clear - empty container"() {
        ComplexContainer con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)

        when:
        con.clear()
        then:
        con.getLoadedWeight() == 0
        Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getNbrOfLoadedThings() == 0
    }

    def "remove an item - a valid item"() {
        ComplexContainer con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)

        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 1,1,0))
        con.add(i4, Helper.findPos(con.getPossibleInsertPositionList(i4), 2,1,0))

        when:
        con.remove(i3)
        then:
        Helper.findPos(con.getPossibleInsertPositionList(i3), 1,1,0) != null
        con.getLoadedVolume() == 7
        con.getNbrOfLoadedThings() == 3
    }

    def "remove an item - an invalid item"() {
        ComplexContainer con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)

        def i6 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i7 = Helper.getItem(1, 3, 1, 1, 111, 0)
        def i8 = Helper.getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 1,1,0))
        con.add(i4, Helper.findPos(con.getPossibleInsertPositionList(i4), 2,1,0))
        con.add(i6, Helper.findPos(con.getPossibleInsertPositionList(i6), 0,0,1))
        con.add(i7, Helper.findPos(con.getPossibleInsertPositionList(i7), 2,0,1))
        con.add(i8, Helper.findPos(con.getPossibleInsertPositionList(i8), 0,2,1))

        when:
        con.remove(i3)
        then:
        Helper.findPos(con.getPossibleInsertPositionList(i3), 1,1,0) != null
        con.getLoadedVolume() == 16
        con.getNbrOfLoadedThings() == 6
    }

    def "remove everything one by one"() {
        ComplexContainer con = Helper.getContainer(3,3,2)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i5 = Helper.getItem(1, 1, 1, 1, 111, 0)

        def i6 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i7 = Helper.getItem(1, 3, 1, 1, 111, 0)
        def i8 = Helper.getItem(2, 1, 1, 1, 111, 0)
        con.add(i1, Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0))
        con.add(i2, Helper.findPos(con.getPossibleInsertPositionList(i2), 0,1,0))
        con.add(i3, Helper.findPos(con.getPossibleInsertPositionList(i3), 2,1,0))
        con.add(i4, Helper.findPos(con.getPossibleInsertPositionList(i4), 0,2,0))
        con.add(i5, Helper.findPos(con.getPossibleInsertPositionList(i5), 1,2,0))
        con.add(i6, Helper.findPos(con.getPossibleInsertPositionList(i6), 0,0,1))
        con.add(i7, Helper.findPos(con.getPossibleInsertPositionList(i7), 2,0,1))
        con.add(i8, Helper.findPos(con.getPossibleInsertPositionList(i8), 0,2,1))

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
        Helper.findPos(con.getPossibleInsertPositionList(i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getNbrOfLoadedThings() == 0
    }
}
