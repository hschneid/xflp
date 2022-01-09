package xf.xflp.base.position

import helper.Helper
import spock.lang.Specification

class TouchingPerimeterServiceTest extends Specification {

    def "test touching perimeter (only one wall)"() {
        def con = Helper.getContainer(10,10,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 48
    }

    def "test touching perimeter (only two walls on the width)"() {
        def con = Helper.getContainer(4,10,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the length)"() {
        def con = Helper.getContainer(10,4,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only two walls on the height)"() {
        def con = Helper.getContainer(10,10,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 64
    }

    def "test touching perimeter (only three walls on the width, length)"() {
        def con = Helper.getContainer(4,4,10)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the width, height)"() {
        def con = Helper.getContainer(4,10,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only three walls on the length, height)"() {
        def con = Helper.getContainer(10,4,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 80
    }

    def "test touching perimeter (only all walls)"() {
        def con = Helper.getContainer(4,4,4)
        def i = Helper.getItem(4, 4, 4, 1, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)
        def pos = Helper.findCand(pList, 0, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos, 1, true, true)

        then:
        pos != null
        f == 96
    }

    def "test touching perimeter (mixed wall and box)"() {
        def con = Helper.getContainer(4,4,4)
        def i1 = Helper.getItem(1, 4, 4, 1, 1, 0)
        def i2 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = Helper.findCand(PositionService.findPositionCandidates(con, i1), 0, 0, 0)
        Helper.add(con, pos)
        def pos2 = Helper.findCand(PositionService.findPositionCandidates(con, i2), 1, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos2, 1, true, true)

        then:
        pos != null
        pos2 != null
        f == 12
    }

    def "test touching perimeter (mixed wall and half-height box) AddRemoveContainer"() {
        def con = Helper.getContainer(4,4,4)
        def i1 = Helper.getItem(1, 4, 1, 1, 1, 0)
        def i2 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = Helper.findCand(PositionService.findPositionCandidates(con, i1), 0, 0, 0)
        Helper.add(con, pos)
        def pos2 = Helper.findCand(PositionService.findPositionCandidates(con, i2), 1, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos2, 1, true, true)

        then:
        pos != null
        pos2 != null
        f == 10
    }

    def "test touching perimeter (mixed wall and half-height box) AddContainer"() {
        def con = Helper.getAddSpaceContainer(4,4,4)
        def i1 = Helper.getItem(1, 4, 1, 1, 1, 0)
        def i2 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        def pos2 = Helper.findCand(PositionService.findPositionCandidates(con, i2), 1, 0, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos2, 1, true, true)

        then:
        pos2 != null
        f == 10
    }

    def "test touching perimeter (only box)"() {
        def con = Helper.getContainer(4,4,4)
        def i1 = Helper.getItem(1, 4, 4, 1, 1, 0)
        def i2 = Helper.getItem(3, 1, 4, 1, 1, 0)
        def i3 = Helper.getItem(2, 2, 2, 1, 1, 0)

        when:
        def pos = Helper.findCand(PositionService.findPositionCandidates(con, i1), 0, 0, 0)
        Helper.add(con, pos)
        def pos2 = Helper.findCand(PositionService.findPositionCandidates(con, i2), 1, 0, 0)
        Helper.add(con, pos2)
        def pos3 = Helper.findCand(PositionService.findPositionCandidates(con, i3), 1, 1, 0)
        float f = TouchingPerimeterService.getTouchingPerimeter(con, pos3, 1, true, true)

        then:
        pos != null
        pos2 != null
        pos3 != null
        f == 12
    }
}
