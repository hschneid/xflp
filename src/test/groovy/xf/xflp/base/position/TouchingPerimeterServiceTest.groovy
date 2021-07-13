package xf.xflp.base.position

import helper.Helper
import spock.lang.Specification

class TouchingPerimeterServiceTest extends Specification {

    def "test touching perimeter (only one wall)"() {
        def con = Helper.getContainer2(10,10,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 48
    }

    def "test touching perimeter (only two walls on the width)"() {
        def con = Helper.getContainer2(4,10,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the length)"() {
        def con = Helper.getContainer2(10,4,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the height)"() {
        def con = Helper.getContainer2(10,10,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only three walls on the width, length)"() {
        def con = Helper.getContainer2(4,4,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the width, height)"() {
        def con = Helper.getContainer2(4,10,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the length, height)"() {
        def con = Helper.getContainer2(10,4,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only all walls)"() {
        def con = Helper.getContainer2(4,4,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pos = Helper.findPos(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i, pos, 1, true, true)

        then:
        pos != null
        f == 96
    }

    def "test touching perimeter (mixed wall and box)"() {
        def con = Helper.getContainer2(4,4,4)
        def i1 = Helper.getItem(1, 4, 4, 1, 1, 0)
        def i2 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i2, pos2, 1, true, true)

        then:
        pos != null
        pos2 != null
        f == 12
    }

    def "test touching perimeter (mixed wall and half-height box)"() {
        def con = Helper.getContainer2(4,4,4)
        def i1 = Helper.getItem(1, 4, 1, 1, 1, 0)
        def i2 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i2, pos2, 1, true, true)

        then:
        pos != null
        pos2 != null
        f == 10
    }

    def "test touching perimeter (only box)"() {
        def con = Helper.getContainer2(4,4,4)
        def i1 = Helper.getItem(1, 4, 4, 1, 1, 0)
        def i2 = Helper.getItem(3, 1, 4, 1, 1, 0)
        def i3 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0)
        con.add(i1, pos)
        def pos2 = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0)
        con.add(i2, pos2)
        def pos3 = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 1, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, i3, pos3, 1, true, true)

        then:
        pos != null
        pos2 != null
        pos3 != null
        f == 12
    }
}
