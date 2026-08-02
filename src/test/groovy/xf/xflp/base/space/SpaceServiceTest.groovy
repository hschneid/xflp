package xf.xflp.base.space

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.item.Position
import xf.xflp.base.item.Space

class SpaceServiceTest extends Specification {

    def service = new SpaceService()

    // An item that does not overlap with the space should return the original space unchanged.
    def "createSpacesAtPosition - item not in space returns original space"() {
        def pos = Position.of(0, 0, 0)
        def space = Space.of(5, 5, 5)
        def item = Helper.getPlacedItemAtPos(6, 6, 0, 1, 1, 1)

        when:
        def result = service.createSpacesAtPosition(pos, space, item)

        then:
        result.size() == 1
        result[0] == space
    }

    // An item directly over the position (hovering) should create a reduced height space.
    def "createSpacesAtPosition - item hovering above position creates height-limited space"() {
        def pos = Position.of(0, 0, 0)
        def space = Space.of(5, 5, 5)
        def item = Helper.getPlacedItemAtPos(0, 0, 2, 1, 1, 1)

        when:
        def result = service.createSpacesAtPosition(pos, space, item)

        then:
        result.size() == 1
        result[0].h() == 2
    }

    // An item completely outside the space along the x axis should be detected as not in space.
    def "isItemNotInSpace - item completely outside x returns true"() {
        def pos = Position.of(0, 0, 0)
        def space = Space.of(3, 3, 3)
        def item = Helper.getPlacedItemAtPos(4, 0, 0, 1, 1, 1)

        when:
        def result = service.isItemNotInSpace(pos, space, item)

        then:
        result
    }

    // An item completely inside the space should be detected as in space.
    def "isItemNotInSpace - item inside space returns false"() {
        def pos = Position.of(0, 0, 0)
        def space = Space.of(3, 3, 3)
        def item = Helper.getPlacedItemAtPos(1, 1, 1, 1, 1, 1)

        when:
        def result = service.isItemNotInSpace(pos, space, item)

        then:
        !result
    }

    // getDominatingSpaces with one space should return that single space.
    def "getDominatingSpaces - single space returns itself"() {
        def spaces = [Space.of(2, 3, 4)]

        when:
        def result = service.getDominatingSpaces(spaces)

        then:
        result.size() == 1
        result[0] == spaces[0]
    }

    // When one space dominates another (same 2 dimensions, strictly greater in 1), the dominated space is removed.
    def "getDominatingSpaces - dominated space is removed"() {
        def s1 = Space.of(3, 3, 4)
        def s2 = Space.of(3, 3, 3)

        when:
        def result = service.getDominatingSpaces([s1, s2])

        then:
        result.size() == 1
        result[0] == s1
    }

    // Two incomparable spaces (none dominates the other) should both be kept.
    def "getDominatingSpaces - incomparable spaces both kept"() {
        def s1 = Space.of(4, 3, 3)
        def s2 = Space.of(3, 4, 3)

        when:
        def result = service.getDominatingSpaces([s1, s2])

        then:
        result.size() == 2
    }

    // An empty collection should return an empty list.
    def "getDominatingSpaces - empty input returns empty"() {
        when:
        def result = service.getDominatingSpaces([])

        then:
        result.size() == 0
    }
}








