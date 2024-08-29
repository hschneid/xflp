package xf.xflp

import spock.lang.Specification
import xf.xflp.exception.XFLPException
import xf.xflp.opt.XFLPOptType

class XFLPSpec extends Specification {

    def service = new XFLP()

    def setup() {
        service.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
    }

    def "error when no items inserted"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)

        when:
        service.executeLoadPlanning()
        then:
        thrown(XFLPException)
    }

    def "error when no container inserted"() {
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)

        when:
        service.executeLoadPlanning()
        then:
        thrown(XFLPException)
    }

    def "error when no items because its cleared"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)

        when:
        service.clearItems()
        service.executeLoadPlanning()
        then:
        thrown(XFLPException)
    }

    def "error when no container because its cleared"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)

        when:
        service.clearContainers()
        service.executeLoadPlanning()
        then:
        thrown(XFLPException)
    }

    def "null report when no optimization"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)

        when:
        def report = service.getReport()
        then:
        report == null;
    }

    def "has unplanned items after planning - nope"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(1).setHeight(1).setWeight(1)

        when:
        service.executeLoadPlanning()
        def result = service.hasUnplannedItems()
        then:
        !result
    }

    def "has unplanned items without planning - nope"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(1).setHeight(1).setWeight(1)

        when:
        def result = service.hasUnplannedItems()
        then:
        !result
    }

    def "has unplanned items - yes"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(4).setLength(1).setHeight(1).setWeight(1)

        when:
        service.executeLoadPlanning()
        def result = service.hasUnplannedItems()
        then:
        result
    }

    def "Simple test - all fits"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)

        when:
        service.executeLoadPlanning()
        def rep = service.getReport()
        then:
        rep.getContainerReports().size() == 1
        rep.getContainerReports().get(0).getPackageEvents().size() == 3
    }

    def "test with container type - should work"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10).setContainerType("ANY")
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)

        when:
        service.executeLoadPlanning()
        def rep = service.getReport()
        then:
        rep.getContainerReports().size() == 1
        rep.getContainerReports().get(0).getPackageEvents().size() == 3
    }

    def "invalid item data"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10).setContainerType("ANY")
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(8).setContainerType("ANY2")
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(w).setLength(l).setHeight(h).setWeight(kg).setImmersiveDepth(id)

        when:
        service.executeLoadPlanning()
        then:
        def error = thrown(expectedException)
        error.message.contains(expectedMessage)
        where:
        w | l | h | id | kg || expectedException | expectedMessage
        0 | 1 | 1 | 1  | 1  || XFLPException | 'Width of item must be greater 0'
        1 | 0 | 1 | 1  | 1  || XFLPException | 'Length of item must be greater 0'
        1 | 1 | 0 | 1  | 1  || XFLPException | 'Height of item must be greater 0'
        1 | 1 | 1 | -1 | 1  || XFLPException | 'Immersive depth must be >= 0'
        1 | 1 | 1 | 1  | 1  || XFLPException | 'Immersive depth must not lead to negative height'
        1 | 1 | 1 | 0  | 11 || XFLPException | 'Item is too heavy for any container'
    }

    def "Parameter maxNbrOfItems is checked"() {
        service.addContainer().setWidth(10).setLength(10).setHeight(10).setMaxWeight(1000)
        for (i in 1..<101) {
            service.addItem().setExternID("P"+i).setWidth(1).setLength(1).setHeight(1).setWeight(1)
        }

        // Restrict number of packages
        when:
        service.getParameter().setMaxNbrOfItems(23)
        service.executeLoadPlanning()
        def rep = service.getReport()

        then:
        rep.getContainerReports()[0].getPackageEvents().size() == 23
        rep.getUnplannedPackages().size() == 77

        // Check - all fit tightly
        when:
        service.getParameter().setMaxNbrOfItems(100)
        service.executeLoadPlanning()
        rep = service.getReport()

        then:
        rep.getContainerReports()[0].getPackageEvents().size() == 100
        rep.getUnplannedPackages().size() == 0

        // Wrong parameter value
        when:
        service.getParameter().setMaxNbrOfItems(0)
        service.executeLoadPlanning()
        rep = service.getReport()

        then:
        def error = thrown(XFLPException)
        error.message.contains("Number of allowed items must be greater than 0")
    }

}
