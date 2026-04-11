package xf.xflp.base.container.addremove

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container
import xf.xflp.base.container.GroundContactRule
import xf.xflp.base.container.ParameterType
import xf.xflp.base.position.PositionService

class ContainerStackingSpec extends Specification {

    def "add to stack simple"() {
        Container con = Helper.getAddSpaceContainer2(2,2,2)
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
        Container con = Helper.getAddSpaceContainer2(2,2,2)
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
        Container con = Helper.getAddSpaceContainer2(2,2,2)
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
        Container con = Helper.getAddSpaceContainer2(2,2,2)
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
        Container con = Helper.getAddSpaceContainer2(2,2,2)
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
        Container con = Helper.getAddSpaceContainer2(2,2,2)
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
        def con = Helper.getAddSpaceContainer2(2,2,3)
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
        def con = Helper.getAddSpaceContainer2(2,2,3)
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
        def con = Helper.getAddSpaceContainer2(3,3,3)
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
        def con = Helper.getAddSpaceContainer2(6,3,3)
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
        def con = Helper.getAddSpaceContainer2(3,3,3)
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
        def con = Helper.getAddSpaceContainer2(3,3,3)
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
        def con = Helper.getAddSpaceContainer2(3,3,3)
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
        def con = Helper.getAddSpaceContainer2(3,3,3)
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
        def con = Helper.getAddSpaceContainer2(3,3,3)
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
        def con = Helper.getAddSpaceContainer2(3,3,2)
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

    // A 2x2 item is placed on a 1x1 base, so only one corner rests on the lower item while the rest hangs freely.
    def "GroundContactRule FREE - item with only one corner on lower item is allowed"() {
        def con = Helper.getAddSpaceContainer2(3,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(2, 2, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null
    }

    // Same layout (2x2 item on 1x1 base) is tested with both rules to verify that COVERED rejects and FREE accepts partial ground contact.
    def "GroundContactRule FREE allows partial ground contact but COVERED does not"() {
        def conCovered = Helper.getAddSpaceContainer2(3,3,2)
        conCovered.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def ic1 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def ic2 = Helper.getItem(2, 2, 1, 1, 111, 0)
        Helper.add(conCovered, ic1, 0, 0, 0)

        def conFree = Helper.getAddSpaceContainer2(3,3,2)
        conFree.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def if1 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def if2 = Helper.getItem(2, 2, 1, 1, 111, 0)
        Helper.add(conFree, if1, 0, 0, 0)

        when:
        def pListCovered = PositionService.findPositionCandidates(conCovered, ic2)
        def foundCovered = Helper.findCand(pListCovered, 0, 0, 1)

        def pListFree = PositionService.findPositionCandidates(conFree, if2)
        def foundFree = Helper.findCand(pListFree, 0, 0, 1)

        then:
        foundCovered == null
        foundFree != null
    }

    // A smaller 1x1 item is placed on top of a 2x2 item, so all 4 corners rest on the same single item.
    def "GroundContactRule SINGLE - item fully on one lower item is allowed"() {
        def con = Helper.getAddSpaceContainer2(3,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.SINGLE)
        def i1 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null
    }

    // A 2x1 item spans across two 1x1 items, so its corners rest on two different items which violates SINGLE.
    def "GroundContactRule SINGLE - item on two lower items is not allowed"() {
        def con = Helper.getAddSpaceContainer2(3,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.SINGLE)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found == null
    }

    // A 2x1 item is placed so that 3 corners rest on a 2x2 base item but 1 corner rests on a neighboring item.
    def "GroundContactRule SINGLE - item with 3 corners on same item and 1 corner on another is not allowed"() {
        def con = Helper.getAddSpaceContainer2(4,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.SINGLE)
        def i1 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 2, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        def found = Helper.findCand(pList, 1, 0, 1)

        then:
        found == null
    }

    // A 2x1 item is placed on two 1x1 items which fully cover its ground, satisfying the MULTIPLE rule.
    def "GroundContactRule MULTIPLE - item on multiple lower items is allowed"() {
        def con = Helper.getAddSpaceContainer2(3,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.MULTIPLE)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null
    }

    // A 2x2 item is placed on a single 1x1 item, leaving 3 corners unsupported which violates the MULTIPLE rule.
    def "GroundContactRule MULTIPLE - item without full ground contact is not allowed"() {
        def con = Helper.getAddSpaceContainer2(3,3,2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.MULTIPLE)
        def i1 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(2, 2, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found == null
    }

    // The lower item has a stacking weight limit of 1 and the upper item weighs exactly 1, so bearing capacity reaches exactly 0 which is still valid.
    def "bearing capacity exactly zero is allowed"() {
        def con = Helper.getAddSpaceContainer2(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 10, 0)

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null
    }

    // The lower item has a stacking weight limit of 1 but the upper item weighs 1.001, pushing bearing capacity just below 0 which is invalid.
    def "bearing capacity just below zero is not allowed"() {
        def con = Helper.getAddSpaceContainer2(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getItem(1, 1, 1, 1.001 as float, 10, 0)

        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found == null
    }

    // An item placed directly on the ground (z=0) bypasses all stacking checks regardless of stacking group settings.
    def "item at z=0 always passes stacking check"() {
        def con = Helper.getAddSpaceContainer2(2,2,2)
        def i1 = Helper.getItem(1, 1, 1, 1, 1, 2)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)
        def found = Helper.findCand(pList, 0, 0, 0)

        then:
        found != null
    }

    // A multi-level stack is built, then a small item is placed at an intermediate level where it only partially overlaps with the item below.
    def "GroundContactRule FREE - item partially hanging over stack is allowed"() {
        def con = Helper.getAddSpaceContainer2(3,3,4)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getItem(2, 1, 1, 1, 4, 0)
        def i2 = Helper.getItem(1, 1, 1, 1, 2, 0)
        def i3 = Helper.getItem(2, 1, 1, 1, 1, 0)
        def i4 = Helper.getItem(2, 1, 1, 1, 1, 0)
        def i5 = Helper.getItem(1, 1, 1, 1, 1, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 0, 1)
        Helper.add(con, i3, 0, 0, 2)
        Helper.add(con, i4, 0, 0, 3)

        when:
        Helper.add(con, i5, 1, 0, 1)

        then:
        con.items.size() == 5
    }

    // An item with nbrOfAllowedStackedItems=2 is placed on top of exactly 2 items below, which is still valid.
    def "nbrOfAllowedStackedItems exactly matching number of items below"() {
        def nbrOfAllowedItemsBelow = 2

        def con = Helper.getAddSpaceContainer2(1,10,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getItem(1, 1, 1, 1, 10, 0)
        // The critical item - sits on exactly 2 items below
        def i4 = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def found = Helper.findCand(pList, 0, 1, 1)

        then:
        found != null
    }

    // An item with nbrOfAllowedStackedItems=1 is placed on top of 2 items below, exceeding the limit by one.
    def "nbrOfAllowedStackedItems exceeded by one"() {
        def nbrOfAllowedItemsBelow = 1

        def con = Helper.getAddSpaceContainer2(1,10,2)
        def i1 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getItem(1, 1, 1, 1, 10, 0)
        // The critical item - would sit on 2 items but only 1 allowed
        def i4 = Helper.getItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def found = Helper.findCand(pList, 0, 1, 1)

        then:
        found == null
    }

}
