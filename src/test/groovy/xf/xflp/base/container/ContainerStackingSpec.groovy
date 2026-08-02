package xf.xflp.base.container

import helper.Helper
import spock.lang.Specification
import spock.lang.Unroll
import xf.xflp.base.position.PositionService

/**
 * Consolidated stacking tests that verify both AddContainer and AddRemoveContainer
 * behave identically for common stacking operations.
 *
 * This spec replaces the duplicated tests found in:
 *   - addspace.AddContainerStackingSpec
 *   - addremove.ContainerStackingSpec
 *
 * Each test is executed for both container types via @Unroll.
 */
class ContainerStackingSpec extends Specification {

    private static Container createContainer(String type, int w, int l, int h) {
        if (type == "Add") return Helper.getAddSpaceContainer(w, l, h)
        return Helper.getAddSpaceContainer2(w, l, h)
    }

    // ------------------------------------------------------------------
    //  Simple stacking
    // ------------------------------------------------------------------

    @Unroll
    def "add to stack simple (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList2, 0, 0, 1)

        then:
        pList.size() > 0
        found != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "stacking is not possible (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 2, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList2, 0, 0, 1)

        then:
        pList.size() > 0
        found == null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Two-stack tests
    // ------------------------------------------------------------------

    @Unroll
    def "add to a stack with two stack (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 2, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found2 = Helper.findCand(pList2, 1, 0, 0)
        Helper.add(con, found2)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def found3 = Helper.findCand(pList3, 0, 0, 1)

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "do not add to a stack with two stack (too heavy/bearing capacity) (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 3, 10, 0)
        def pList = PositionService.findPositionCandidates(con, i1)
        Helper.add(con, pList.get(0))

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def found2 = Helper.findCand(pList2, 1, 0, 0)
        Helper.add(con, found2)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def found3 = Helper.findCand(pList3, 0, 0, 1)

        then:
        pList.size() > 0
        pList2.size() > 0
        pList3.size() > 0
        found2 != null
        found3 == null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Stacking groups
    // ------------------------------------------------------------------

    @Unroll
    def "placing one item over two stacks - stacking groups not fitting (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 1)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 111, 1)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList3 = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList3, 0, 0, 1) == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "placing one item over two stacks - stacking groups are fitting (#type)"() {
        given:
        Container con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 2)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 111, 2)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 111, 2)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList3 = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList3, 0, 0, 1) != null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Double stack bearing capacity
    // ------------------------------------------------------------------

    @Unroll
    def "do not add to a double stack (nearly too heavy/bearing capacity) (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 2, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 2, 1, 0)
        def i4 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)

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

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "add to a double stack with bigger size (not too heavy/bearing capacity) (#type)"() {
        given:
        def con = createContainer(type, 6, 3, 3)
        def i1 = Helper.getPlacedItem(4, 1, 1, 1, 2, 0)
        def i2 = Helper.getPlacedItem(2, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(6, 1, 1, 2, 1, 0)
        def i4 = Helper.getPlacedItem(1, 1, 1, 0.9999, 1, 0)

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

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Stacking group - multiple groups
    // ------------------------------------------------------------------

    @Unroll
    def "2 items not in same stacking group (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 2)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 2)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 1)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "Items can bear multiple stacking groups (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 1)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "Items cannot bear multiple stacking groups (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 5)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 5)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 2)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def pList = PositionService.findPositionCandidates(con, i3)

        then:
        Helper.findCand(pList, 0, 0, 1, false) == null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Overlapping / ground contact
    // ------------------------------------------------------------------

    @Unroll
    def "check overlapping items (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def i1 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(3, 1, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList2 = PositionService.findPositionCandidates(con, i2)
        def pos2 = Helper.findCand(pList2, 0, 0, 1)
        def pList3 = PositionService.findPositionCandidates(con, i3)
        def pos3 = Helper.findCand(pList3, 0, 0, 1)

        then:
        pos2 == null
        pos3 != null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  nbrOfAllowedStackedItems
    // ------------------------------------------------------------------

    @Unroll
    def "Item can be placed on 2 other items (#type)"() {
        given:
        def nbrOfAllowedItemsBelow = 2

        Container con = createContainer(type, 1, 10, 2)
        def i1 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i4 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def found = Helper.findCand(pList, 0, 1, 1)

        then:
        found != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "Item can not be placed on 2 other items (#type)"() {
        given:
        def nbrOfAllowedItemsBelow = 1

        Container con = createContainer(type, 1, 10, 2)
        def i1 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i4 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def found = Helper.findCand(pList, 0, 1, 1)

        then:
        found == null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Floor-only and bearing capacity tracking
    // ------------------------------------------------------------------

    @Unroll
    def "Item can be placed only on floor, because not stackable (#type)"() {
        given:
        def nbrOfAllowedItemsBelow = 0

        Container con = createContainer(type, 1, 10, 2)
        def i1 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        def i3 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i4 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0, 1, nbrOfAllowedItemsBelow)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 2, 0)
        Helper.add(con, i3, 0, 0, 1)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)

        then:
        pList.count {p -> p.position.z > 0} == 0
        pList.count {p -> p.position.z == 0} > 0

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "add upper item beneath another upper item (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 3)
        def i1 = Helper.getPlacedItem(2, 1, 1, 1, 2, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 0, 1)

        when:
        def pos3 = Helper.findCand(PositionService.findPositionCandidates(con, i3), 1, 0, 1)
        Helper.add(con, pos3)

        then:
        con.items.size() == 3
        con.getBaseData().getBearingCapacities().values().every {i -> i == 0}

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "add item in existing stack at lower level (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 4)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getPlacedItem(2, 1, 1, 1, 4, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 2, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 0)
        def i4 = Helper.getPlacedItem(2, 1, 1, 1, 1, 0)
        def i5 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 0, 1)
        Helper.add(con, i3, 0, 0, 2)
        Helper.add(con, i4, 0, 0, 3)

        when:
        Helper.add(con, i5, 1, 0, 1)

        then:
        con.items.size() == 5
        con.getBaseData().getBearingCapacities().values().every {i -> i == 0}

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  GroundContactRule tests
    // ------------------------------------------------------------------

    @Unroll
    def "GroundContactRule FREE - item with only one corner on lower item is allowed (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "GroundContactRule FREE allows partial ground contact but COVERED does not (#type)"() {
        given:
        def conCovered = createContainer(type, 3, 3, 2)
        conCovered.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)
        def ic1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def ic2 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        Helper.add(conCovered, ic1, 0, 0, 0)

        def conFree = createContainer(type, 3, 3, 2)
        conFree.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def if1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def if2 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        Helper.add(conFree, if1, 0, 0, 0)

        when:
        def pListCovered = PositionService.findPositionCandidates(conCovered, ic2)
        def foundCovered = Helper.findCand(pListCovered, 0, 0, 1)
        def pListFree = PositionService.findPositionCandidates(conFree, if2)
        def foundFree = Helper.findCand(pListFree, 0, 0, 1)

        then:
        foundCovered == null
        foundFree != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "GroundContactRule SINGLE - item fully on one lower item is allowed (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.SINGLE)
        def i1 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "GroundContactRule SINGLE - item on two lower items is not allowed (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.SINGLE)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "GroundContactRule SINGLE - item with 3 corners on same item and 1 corner on another is not allowed (#type)"() {
        given:
        def con = createContainer(type, 4, 3, 2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.SINGLE)
        def i1 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 2, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        def found = Helper.findCand(pList, 1, 0, 1)

        then:
        found == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "GroundContactRule MULTIPLE - item on multiple lower items is allowed (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.MULTIPLE)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "GroundContactRule MULTIPLE - item without full ground contact is not allowed (#type)"() {
        given:
        def con = createContainer(type, 3, 3, 2)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.MULTIPLE)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found == null

        where:
        type << ["Add", "AddRemove"]
    }

    // ------------------------------------------------------------------
    //  Bearing capacity edge cases
    // ------------------------------------------------------------------

    @Unroll
    def "bearing capacity exactly zero is allowed (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found != null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "bearing capacity just below zero is not allowed (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1.001 as float, 10, 0)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 0, 1)

        then:
        found == null

        where:
        type << ["Add", "AddRemove"]
    }

    @Unroll
    def "item at z=0 always passes stacking check (#type)"() {
        given:
        def con = createContainer(type, 2, 2, 2)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 2)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)
        def found = Helper.findCand(pList, 0, 0, 0)

        then:
        found != null

        where:
        type << ["Add", "AddRemove"]
    }
}
