package xf.xflp.base.container.addremove

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.GroundContactRule
import xf.xflp.base.container.ParameterType
import xf.xflp.base.position.PositionService

class ContainerBaseSpec extends Specification {

    def "find first position in empty container"() {
        when:
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i)
        def pp = Helper.findCand(pList, 0, 0, 0)

        then:
        pp != null
        pList.size() == 1
        con.getItems().size() == 0
    }

    def "add item into empty container"() {
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i)

        when:
        Helper.add(con, pList.get(0))

        then:
        con.getItems().size() == 1
        i.x == 0 && i.y == 0 && i.z == 0
    }

    def "find second position in container"() {
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        Helper.add(con, PositionService.findPositionCandidates(con, i).get(0))

        when:
        def pList = PositionService.findPositionCandidates(con, i2)

        then:
        pList.size() == 3
        Helper.findCand(pList, 0, 0, 0) == null
        Helper.findCand(pList, 1, 0, 0) != null
        Helper.findCand(pList, 0, 1, 0) != null
        Helper.findCand(pList, 0, 0, 1) != null
    }

    def "add second item to container"() {
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        Helper.add(con, PositionService.findPositionCandidates(con, i).get(0))
        def pList = PositionService.findPositionCandidates(con, i2)

        when:
        Helper.add(con, Helper.findCand(pList, 1, 0, 0))

        then:
        con.getItems().size() == 2
        i2.x == 1 && i2.y == 0 && i2.z == 0
    }

    def "find and add third item in container"() {
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        Helper.add(con, PositionService.findPositionCandidates(con, i).get(0))
        def pList = PositionService.findPositionCandidates(con, i2)
        Helper.add(con, Helper.findCand(pList, 1, 0, 0))

        when:
        pList = PositionService.findPositionCandidates(con, i3)
        def foundPos = Helper.findCand(pList, 0, 1, 0)
        def found2 = Helper.findCand(pList, 0,0,1) != null
        Helper.add(con, foundPos)

        then:
        con.getItems().size() == 3
        foundPos != null
        found2
    }

    def "find and add forth item in container"() {
        def con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        Helper.add(con, PositionService.findPositionCandidates(con, i1).get(0))
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def foundPos = Helper.findCand(pList, 0,0,1)
        def found2 = Helper.findCand(pList, 1, 0, 1) != null
        def found3 = Helper.findCand(pList, 0,1,1) != null
        Helper.add(con, foundPos)

        then:
        con.getItems().size() == 4
        foundPos != null
        found2
        found3
    }

    def "find and add fifth item in container"() {
        def con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i5 = Helper.getItem(1, 2, 1, 1, 10, 0)
        Helper.add(con, PositionService.findPositionCandidates(con, i1).get(0))
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)
        Helper.add(con, i4, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i5)
        def foundPos = Helper.findCand(pList, 1,0,1)
        Helper.add(con, foundPos)

        then:
        con.getItems().size() == 5
        foundPos != null
    }

    def "find and add sixth item in container"() {
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i5 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i6 = Helper.getItem(1, 1, 1, 1, 10, 0)
        Helper.add(con, PositionService.findPositionCandidates(con, i).get(0))
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)
        Helper.add(con, i4, 0, 0, 1)
        Helper.add(con, i5, 1, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i6)
        def foundPos = Helper.findCand(pList, 0, 1, 1)
        def hasOtherPos = pList.findAll {p -> !(p.position.x == 0 && p.position.y == 1 && p.position.z == 1)}.size() > 0
        Helper.add(con, foundPos)

        then:
        con.getItems().size() == 6
        foundPos != null
        !hasOtherPos
    }

    def "find rotated insert position"() {
        def con = Helper.getContainer(2,2,2)
        def i = Helper.getItem(2, 1, 1, 1, 10, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i)

        then:
        pList.size() == 2
        pList.find {p -> p.position.x == 0 && p.position.y == 0 && p.position.z == 0 && p.isRotated} != null
        pList.find {p -> p.position.x == 0 && p.position.y == 0 && p.position.z == 0 && !p.isRotated} != null
    }

    def "do not find insert positions for too big items"() {
        def con = Helper.getContainer(2,2,2, 10)
        def i1 = Helper.getItem(3, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 3, 1, 1, 10, 0)
        def i3 = Helper.getItem(1, 1, 3, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 11, 10, 0)
        def i5 = Helper.getItem(1, 1, 1, 9, 10, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i1)
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pList5 = PositionService.findPositionCandidates(con, i5)

        then:
        pList1.size() == 0
        pList2.size() == 0
        pList3.size() == 0
        pList4.size() == 0
        pList5.size() > 0
    }

    def "add and remove items from container"() {
        def con = Helper.getContainer(2,2,5)
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(1, 1, 1, 1, 100, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 0, 1)
        Helper.add(con, i3, 0, 0, 2)

        def apList1 = new ArrayList<>(con.getActivePositions())
        con.remove(i2)
        def apList2 = new ArrayList<>(con.getActivePositions())
        con.remove(i1)
        def apList3 = new ArrayList<>(con.getActivePositions())
        con.remove(i3)
        def apList4 = new ArrayList<>(con.getActivePositions())

        then:
        Helper.findPos(apList1, 1, 0, 0) != null
        Helper.findPos(apList1, 0, 1, 0) != null
        Helper.findPos(apList1, 1, 0, 1) != null
        Helper.findPos(apList1, 0, 1, 1) != null
        Helper.findPos(apList1, 1, 0, 2) != null
        Helper.findPos(apList1, 0, 1, 2) != null
        Helper.findPos(apList1, 0, 0, 3) != null
        Helper.findPos(apList2, 0, 0, 1) != null
        Helper.findPos(apList3, 0, 0, 0) != null
        con.getActivePositions().size() == 1
        Helper.findPos(apList4, 0, 0, 0) != null
    }

    def "test covered positions (X axis)"(){
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 1, 0)
        def pos2 = Helper.findPos(con.inactivePosList, 1, 1, 0)
        def pos3 = Helper.findPos(con.coveredPosList, 1, 1, 0)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Y axis)"(){
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 1, 0, 0)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 1, 0)
        def pos2 = Helper.findPos(con.inactivePosList, 1, 1, 0)
        def pos3 = Helper.findPos(con.coveredPosList, 1, 1, 0)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-X axis)"(){
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 0, 1)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 0, 1)
        def pos2 = Helper.findPos(con.inactivePosList, 1, 0, 1)
        def pos3 = Helper.findPos(con.coveredPosList, 1, 0, 1)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-Y axis)"(){
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 0, 0, 1)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 0, 1, 1)
        def pos2 = Helper.findPos(con.inactivePosList, 0, 1, 1)
        def pos3 = Helper.findPos(con.coveredPosList, 0, 1, 1)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test full coverage of item ground"() {
        def con = Helper.getContainer(5,1,3)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def i1 = Helper.getItem(1, 1, 2, 1, 100, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 2, 1, 100, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 3, 0, 0)

        when:
        def i4 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def pos4 = Helper.findCand(PositionService.findPositionCandidates(con, i4), 0, 0, 2, false)
        def i5 = Helper.getItem(3, 1, 1, 1, 100, 0)
        def pos5 = Helper.findCand(PositionService.findPositionCandidates(con, i5), 0, 0, 2, false)
        def i6 = Helper.getItem(4, 1, 1, 1, 100, 0)
        def pos6 = Helper.findCand(PositionService.findPositionCandidates(con, i6), 0, 0, 2, false)

        then:
        pos4 == null
        pos5 == null
        pos6 != null
    }

    def "test rotation"() {
        def con = Helper.getContainer(2,2,1)
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        i3.spinable = false

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pList3 = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList2, 1, 0, 0, true) != null
        Helper.findCand(pList2, 0, 1, 0, false) != null
        Helper.findCand(pList3, 1, 0, 0, true) == null
        Helper.findCand(pList3, 0, 1, 0, false) != null
    }

    def "test horizontal projection of insert position"() {
        def con = Helper.getContainer(3, 5,2)
        def i1 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 3, 1, 1, 100, 0)
        def i3 = Helper.getItem(3, 2, 1, 1, 100, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 2, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        then:
        Helper.findCand(pList, 0, 3, 0) != null
    }

    def "test vertical projection of insert position"() {
        def con = Helper.getContainer(3, 5,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(1, 3, 1, 1, 100, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        then:
        Helper.findCand(pList, 2, 0, 0) != null
    }

    def "test vertical projection of insert position at other box"() {
        def con = Helper.getContainer(4, 6,2)
        def i1 = Helper.getItem(3, 2, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getItem(2, 3, 1, 1, 100, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 4, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def pos = Helper.findCand(pList, 2, 2, 0)
        then:
        pos != null
    }

    def "big test projected insert positions"() {
        def con = Helper.getContainer(4, 6,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 100, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 1, 2, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        then:
        // Normal pos
        Helper.findCand(pList, 0, 1, 0) != null
        Helper.findCand(pList, 1, 3, 0) != null
        Helper.findCand(pList, 3, 2, 0) != null
        Helper.findCand(pList, 2, 0, 0) != null
        // Horizontal projected pos
        Helper.findCand(pList, 0, 2, 0) != null
        Helper.findCand(pList, 0, 3, 0) != null
        // Vertical projected pos
        Helper.findCand(pList, 3, 0, 0) != null
    }


}
