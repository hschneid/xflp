package xf.xflp

import spock.lang.Specification
import xf.xflp.opt.XFLPOptType

class XFLPSpec extends Specification {

    def service = new XFLP()

    def "error when no items inserted"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

        when:
        service.executeLoadPlanning()
        then:
        thrown(XFLPException)
    }

    def "error when no container inserted"() {
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_ADD_REMOVE_PACKER)

        when:
        service.executeLoadPlanning()
        def rep = service.getReport()
        then:
        rep.getContainerReports().size() == 1
        rep.getContainerReports().get(0).getPackageEvents().size() == 3
    }
}
