package xf.xflp.base.position

import helper.Helper
import spock.lang.Specification

class MaxWidthAreaServiceTest extends Specification {

    def service = new MaxWidthAreaService()

    // An empty container should have an area equal to width * length.
    def "empty container - max area equals container area"() {
        def con = Helper.getAddSpaceContainer2(5, 10, 3)

        when:
        def area = service.getMaxEmptyArea(con)

        then:
        area == 5 * 10 as float
    }

    // After placing items, the remaining area estimate should be less than for the empty container.
    def "items placed - area is reduced compared to empty container"() {
        def con = Helper.getAddSpaceContainer2(3, 3, 1)
        def emptyArea = service.getMaxEmptyArea(con)

        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def area = service.getMaxEmptyArea(con)

        then:
        emptyArea == 3 * 3 as float
        area < emptyArea
        area > 0
    }

    // A completely filled container has no active positions left, so the area should be 0.
    def "full container - no active positions returns zero"() {
        def con = Helper.getAddSpaceContainer2(2, 2, 1)
        def i1 = Helper.getPlacedItem(2, 2, 1, 1, 100, 0)
        Helper.add(con, i1, 0, 0, 0)

        when:
        def area = service.getMaxEmptyArea(con)

        then:
        area == 0
    }
}




