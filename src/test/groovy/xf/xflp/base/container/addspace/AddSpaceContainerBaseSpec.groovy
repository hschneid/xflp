package xf.xflp.base.container.addspace

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.GroundContactRule
import xf.xflp.base.container.ParameterType
import xf.xflp.base.item.RotatedPosition
import xf.xflp.base.position.PositionService

class AddSpaceContainerBaseSpec extends Specification {

    def "find first position in empty container"() {
        when:
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def pList = PositionService.getPossibleInsertPositionList(con, i)
        def pp = Helper.findPos(pList, 0, 0, 0)

        then:
        pp != null
        pList.size() == 1
        con.getItems().size() == 0
    }

    def "add item into empty container"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def pList = PositionService.getPossibleInsertPositionList(con, i)

        when:
        con.add(i, pList.get(0))

        then:
        con.getItems().size() == 1
        i.x == 0 && i.y == 0 && i.z == 0
    }

    def "find second position in container"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, PositionService.getPossibleInsertPositionList(con, i).get(0))

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i2)

        then:
        pList.size() == 3
        Helper.findPos(pList, 0, 0, 0) == null
        Helper.findPos(pList, 1, 0, 0) != null
        Helper.findPos(pList, 0, 1, 0) != null
        Helper.findPos(pList, 0, 0, 1) != null
    }

    def "add second item to container"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, PositionService.getPossibleInsertPositionList(con, i).get(0))
        def pList = PositionService.getPossibleInsertPositionList(con, i2)

        when:
        con.add(i2, Helper.findPos(pList, 1, 0, 0))

        then:
        con.getItems().size() == 2
        i2.x == 1 && i2.y == 0 && i2.z == 0
    }

    def "find and add third item in container"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        con.add(i, PositionService.getPossibleInsertPositionList(con, i).get(0))
        def pList = PositionService.getPossibleInsertPositionList(con, i2)
        con.add(i2, Helper.findPos(pList, 1, 0, 0))

        when:
        pList = PositionService.getPossibleInsertPositionList(con, i3)
        def foundPos = Helper.findPos(pList, 0, 1, 0)
        def found2 = Helper.findPos(pList, 0,0,1) != null
        con.add(i3, foundPos)

        then:
        con.getItems().size() == 3
        foundPos != null
        found2
    }

    def "find and add forth item in container"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i1, PositionService.getPossibleInsertPositionList(con, i1).get(0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        con.add(i3, PositionService.getPossibleInsertPositionList(con, i3)
                .find {p -> p.getX() == 0 && p.getY() == 1 && p.getZ() == 0})
        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i4)
        def foundPos = Helper.findPos(pList, 0,0,1)
        def found2 = Helper.findPos(pList, 1, 0, 1) != null
        def found3 = Helper.findPos(pList, 0,1,1) != null
        con.add(i4, foundPos)

        then:
        con.getItems().size() == 4
        foundPos != null
        found2
        found3
    }

    def "find and add fifth item in container"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i5 = Helper.getItem(1, 2, 1, 1, 10, 0)
        con.add(i1, PositionService.getPossibleInsertPositionList(con, i1).get(0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0, 1, 0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 0, 0, 1))

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i5)
        def foundPos = Helper.findPos(pList, 1,0,1)
        con.add(i5, foundPos)

        then:
        con.getItems().size() == 5
        foundPos != null
    }

    def "find and add sixth item in container"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def i5 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i6 = Helper.getItem(1, 1, 1, 1, 10, 0)
        con.add(i, PositionService.getPossibleInsertPositionList(con, i).get(0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0, 1, 0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 0, 0, 1))
        con.add(i5, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i5), 1, 0, 1))

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i6)
        def foundPos = Helper.findPos(pList, 0, 1, 1)
        def hasOtherPos = pList.findAll {p -> !(p.getX() == 0 && p.getY() == 1 && p.getZ() == 1)}.size() > 0
        con.add(i6, foundPos)

        then:
        con.getItems().size() == 6
        foundPos != null
        !hasOtherPos
    }

    def "find rotated insert position"() {
        def con = Helper.getAddSpaceContainer(2,2,2)
        def i = Helper.getItem(2, 1, 1, 1, 10, 0)

        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i)

        then:
        pList.size() == 2
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && p instanceof RotatedPosition} != null
        pList.find {p -> p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && !(p instanceof RotatedPosition)} != null
    }

    def "do not find insert positions for too big items"() {
        def con = Helper.getAddSpaceContainer(2,2,2, 10)
        def i1 = Helper.getItem(3, 1, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 3, 1, 1, 10, 0)
        def i3 = Helper.getItem(1, 1, 3, 1, 10, 0)
        def i4 = Helper.getItem(1, 1, 1, 11, 10, 0)
        def i5 = Helper.getItem(1, 1, 1, 9, 10, 0)

        when:
        def pList1 = PositionService.getPossibleInsertPositionList(con, i1)
        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)
        def pList4 = PositionService.getPossibleInsertPositionList(con, i4)
        def pList5 = PositionService.getPossibleInsertPositionList(con, i5)

        then:
        pList1.size() == 0
        pList2.size() == 0
        pList3.size() == 0
        pList4.size() == 0
        pList5.size() > 0
    }

    def "test covered positions (X axis)"(){
        def con = Helper.getAddSpaceContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 0)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0, 1, 0, false))
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 1, 0)

        then:
        pos1 == null
    }

    def "test covered positions (Y axis)"(){
        def con = Helper.getAddSpaceContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 1, 0)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0, 1, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 1, 0, 0, false))
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 1, 0)

        then:
        pos1 == null
    }

    def "test covered positions (Z-X axis)"(){
        def con = Helper.getAddSpaceContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 0)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0, 0, 1, false))
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 0, 1)

        then:
        pos1 == null
    }

    def "test covered positions (Z-Y axis)"(){
        def con = Helper.getAddSpaceContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 1, 0)

        when:
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0, 1, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0, 0, 1, false))
        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 0, 1, 1)

        then:
        pos1 == null
    }

    def "test full coverage of item ground"() {
        def con = Helper.getAddSpaceContainer(5,1,3)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def i1 = Helper.getItem(1, 1, 2, 1, 100, 0)
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0)
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        def i3 = Helper.getItem(2, 1, 2, 1, 100, 0)
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 3, 0, 0, false))

        when:
        def i4 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def pos4 = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 0, 0, 2, false)
        def i5 = Helper.getItem(3, 1, 1, 1, 100, 0)
        def pos5 = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i5), 0, 0, 2, false)
        def i6 = Helper.getItem(4, 1, 1, 1, 100, 0)
        def pos6 = Helper.findPos(PositionService.getPossibleInsertPositionList(con, i6), 0, 0, 2, false)

        then:
        pos4 == null
        pos5 == null
        pos6 != null
    }

    def "test rotation"() {
        def con = Helper.getAddSpaceContainer(2,2,1)
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        i3.spinable = false
        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))

        when:
        def pList2 = PositionService.getPossibleInsertPositionList(con, i2)
        def pList3 = PositionService.getPossibleInsertPositionList(con, i3)

        then:
        Helper.findPos(pList2, 1, 0, 0, true) != null
        Helper.findPos(pList2, 0, 1, 0, false) != null
        Helper.findPos(pList3, 1, 0, 0, true) == null
        Helper.findPos(pList3, 0, 1, 0, false) != null
    }

    def "test horizontal projection of insert position"() {
        def con = Helper.getAddSpaceContainer(3, 5,2)
        def i1 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 3, 1, 1, 100, 0)
        def i3 = Helper.getItem(3, 2, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 2, 0, 0))
        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i3)
        then:
        Helper.findPos(pList, 0, 3, 0) != null
    }

    def "test horizontal projection of insert position with space limiting box"() {
        def con = Helper.getAddSpaceContainer(4, 6,2)
        con.getParameter().add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getItem(1, 3, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getItem(2, 2, 1, 1, 100, 0)
        def i5 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i6 = Helper.getItem(1, 3, 1, 1, 100, 0)
        def i7 = Helper.getItem(3, 1, 1, 1, 100, 0)
        def i8 = Helper.getItem(3, 2, 1, 1, 100, 0)
        def i9 = Helper.getItem(3, 1, 2, 1, 100, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0, 3, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0, 4, 0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 0, 3, 1))
        con.add(i5, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i5), 1, 0, 0))
        con.add(i6, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i6), 3, 0, 0))
        when:
        def pList1 = PositionService.getPossibleInsertPositionList(con, i7)
        def pList2 = PositionService.getPossibleInsertPositionList(con, i8)
        def pList3 = PositionService.getPossibleInsertPositionList(con, i9)
        then:
        Helper.findPos(pList1, 1, 3, 0) != null
        Helper.findPos(pList2, 1, 3, 0) == null
        Helper.findPos(pList3, 1, 3, 0) == null
    }

    def "test vertical projection of insert position"() {
        def con = Helper.getAddSpaceContainer(3, 5,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i3 = Helper.getItem(1, 3, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0, 2, 0))
        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i3)
        then:
        Helper.findPos(pList, 2, 0, 0) != null
    }

    def "test vertical projection of insert position at other box"() {
        def con = Helper.getAddSpaceContainer(4, 6,2)
        def i1 = Helper.getItem(3, 2, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getItem(2, 3, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 0, 2, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 0, 4, 0))
        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i4)
        def pos = Helper.findPos(pList, 2, 2, 0)
        then:
        pos != null
    }

    def "test vertical projection of insert position at other box with space limiting box"() {
        def con = Helper.getAddSpaceContainer(6, 4,2)
        con.getParameter().add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getItem(3,1,1, 1, 100, 0)
        def i2 = Helper.getItem(1,1,1, 1, 100, 0)
        def i3 = Helper.getItem(1,2,1, 1, 100, 0)
        def i4 = Helper.getItem(2,2,1, 1, 100, 0)
        def i5 = Helper.getItem(1,2,1, 1, 100, 0)
        def i6 = Helper.getItem(3,1,1, 1, 100, 0)
        def i7 = Helper.getItem(1,3,1, 1, 100, 0)
        def i8 = Helper.getItem(2,3,1, 1, 100, 0)
        def i9 = Helper.getItem(1,3,2, 1, 100, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 3, 0, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 4, 0, 0))
        con.add(i4, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i4), 3, 0, 1))
        con.add(i5, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i5), 0, 1, 0))
        con.add(i6, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i6), 0, 3, 0))
        when:
        def pList1 = PositionService.getPossibleInsertPositionList(con, i7)
        def pList2 = PositionService.getPossibleInsertPositionList(con, i8)
        def pList3 = PositionService.getPossibleInsertPositionList(con, i9)
        then:
        Helper.findPos(pList1, 3, 1,0) != null
        Helper.findPos(pList2, 3, 1,0) == null
        Helper.findPos(pList3, 3, 1,0) == null
    }

    def "big test projected insert positions"() {
        def con = Helper.getAddSpaceContainer(4, 6,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 100, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 100, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 100, 0)

        con.add(i1, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i1), 0, 0, 0))
        con.add(i2, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i2), 1, 0, 0))
        con.add(i3, Helper.findPos(PositionService.getPossibleInsertPositionList(con, i3), 1, 2, 0))
        when:
        def pList = PositionService.getPossibleInsertPositionList(con, i4)
        then:
        // Normal pos
        Helper.findPos(pList, 0, 1, 0) != null
        Helper.findPos(pList, 1, 3, 0) != null
        Helper.findPos(pList, 3, 2, 0) != null
        Helper.findPos(pList, 2, 0, 0) != null
        // Horizontal projected pos
        Helper.findPos(pList, 0, 2, 0) != null
        Helper.findPos(pList, 0, 3, 0) != null
        // Vertical projected pos
        Helper.findPos(pList, 3, 0, 0) != null
    }


}
