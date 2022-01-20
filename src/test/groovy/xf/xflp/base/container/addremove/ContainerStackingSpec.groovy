package xf.xflp.base.container.addremove

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container
import xf.xflp.base.container.GroundContactRule
import xf.xflp.base.container.ParameterType
import xf.xflp.base.position.PositionService

class ContainerStackingSpec extends Specification {

    def "add to stack simple"() {
        Container con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList2,0,0,1)

        then:
        pList.size() > 0
        found != null
    }

    def "stacking is not possible"() {
        Container con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 2, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList2,0,0,1)

        then:
        pList.size() > 0
        found == null
    }

    def "add to a stack with two stack"() {
        Container con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found2 = Helper.findCand(pList2,1,0,0)
        Helper.add(con, found2)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def found3 = Helper.findCand(pList3,0,0,1)

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 != null
    }

    def "do not add to a stack with two stack (too heavy/bearing capacity)"() {
        Container con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 3, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found2 = Helper.findCand(pList2,1,0,0)
        Helper.add(con, found2)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def found3 = Helper.findCand(pList3,0,0,1)

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 == null
    }

    def "placing one item over two stacks - stacking groups not fitting"() {
        Container con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 1)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList3 = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList3, 0,0, 1) == null
    }

    def "placing one item over two stacks - stacking groups are fitting"() {
        Container con = Helper.getContainer(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 2)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList3 = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList3, 0,0, 1) != null
    }


    def "do not add to a double stack (too heavy/bearing capacity)"() {
        def con = Helper.getContainer(2,2,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList1.get(0))

        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pos2 = Helper.findCand(pList2, 1, 0, 0)
        Helper.add(con, pos2)

        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pos3 = Helper.findCand(pList3, 0, 0, 1)
        Helper.add(con, pos3)

        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 == null
    }

    def "do not add to a double stack (nearly too heavy/bearing capacity)"() {
        def con = Helper.getContainer(2,2,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(2, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 1, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList1.get(0))

        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pos2 = Helper.findCand(pList2, 1, 0, 0)
        Helper.add(con, pos2)

        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pos3 = Helper.findCand(pList3, 0, 0, 1)
        Helper.add(con, pos3)

        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 == null
    }

    def "add to a double stack (not too heavy/bearing capacity)"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(2, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(3, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 0.999, 1, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList1.get(0))

        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pos2 = Helper.findCand(pList2, 2, 0, 0)
        Helper.add(con, pos2)

        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pos3 = Helper.findCand(pList3, 0, 0, 1)
        Helper.add(con, pos3)

        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 != null
    }

    def "add to a double stack with bigger size (not too heavy/bearing capacity)"() {
        def con = Helper.getContainer(6,3,3)
        def i1 = Helper.getItem(4, 1, 1, 1, 2, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 1, 0)
        def i3 = Helper.getItem(6, 1, 1, 2, 1, 0)
        def i4 = Helper.getItem(1, 1, 1, 0.9999, 1, 0)

        when:
        def pList1 = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList1.get(0))

        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pos2 = Helper.findCand(pList2, 4, 0, 0)
        Helper.add(con, pos2)

        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pos3 = Helper.findCand(pList3, 0, 0, 1)
        Helper.add(con, pos3)

        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 0, 2)

        then:
        pList1.size() > 0
        pList2.size() > 0
        pos2 != null
        pos3 != null
        pos4 != null
    }

    def "All items in same stacking group"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) != null
    }

    def "2 items not in same stacking group"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) == null
    }

    def "1 item not in same stacking group"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 1)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 2)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) == null
    }

    def "Items can bear multiple stacking groups"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 1)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) != null
    }

    def "Items cannot bear multiple stacking groups"() {
        def con = Helper.getContainer(3,3,3)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 2)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) == null
    }

    def "check overlapping items"() {
        def con = Helper.getContainer(3,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def i1 = Helper.getItem(2, 2, 1, 1, 111,0)
        def i2 = Helper.getItem(3, 1, 1, 1, 111,0 )
        def i3 = Helper.getItem(2, 1, 1, 1, 111,0 )

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pos2 = Helper.findCand(pList2, 0, 0, 1)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pos3 = Helper.findCand(pList3, 0, 0, 1)
        then:
        pos2 == null
        pos3 != null
    }

}
