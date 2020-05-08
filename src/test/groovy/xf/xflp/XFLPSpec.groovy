package xf.xflp

import spock.lang.Specification
import xf.xflp.opt.XFLPOptType

class XFLPSpec extends Specification {

    def service = new XFLP()

    def "Simple test - all fits"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID("P1").setWidth(3).setLength(3).setHeight(1).setWeight(1)
        service.addItem().setExternID("P2").setWidth(3).setLength(2).setHeight(1).setWeight(1)
        service.addItem().setExternID("P3").setWidth(1).setLength(3).setHeight(1).setWeight(1)
        service.setTypeOfOptimization(XFLPOptType.SINGLE_PACKER_ADD_REMOVE)

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
        service.setTypeOfOptimization(XFLPOptType.SINGLE_PACKER_ADD_REMOVE)

        when:
        service.executeLoadPlanning()
        def rep = service.getReport()
        then:
        rep.getContainerReports().size() == 1
        rep.getContainerReports().get(0).getPackageEvents().size() == 3
    }
}
