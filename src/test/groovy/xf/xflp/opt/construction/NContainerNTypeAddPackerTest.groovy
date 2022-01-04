package xf.xflp.opt.construction

import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType

class NContainerNTypeAddPackerTest extends Specification {

    def "test min container for multiple type"() {
        def xflp = new XFLP()
        xflp.addContainer().setContainerType("C1").setWidth(3).setLength(3).setHeight(3)
        xflp.addContainer().setContainerType("C2").setWidth(2).setLength(2).setHeight(2)
        for (i in 0..<95) {
            xflp.addItem().setExternID("P"+i).setWidth(1).setLength(1).setHeight(1).setWeight(1)
        }
        xflp.setTypeOfOptimization(XFLPOptType.FAST_MIN_CONTAINER_PACKER)

        when:
        xflp.executeLoadPlanning()
        def rep = xflp.getReport()

        then:
        rep.getContainerReports().size() == 5
        rep.getContainerReports().count {r -> r.getSummary().getUtilization() > 0.99 && r.getContainerTypeName() == "C1"} == 2
        rep.getContainerReports().count {r -> r.getSummary().getUtilization() > 0.99 && r.getContainerTypeName() == "C2"} == 2
        rep.getContainerReports().count {r -> r.getSummary().getNbrOfLoadedPackages() == 25 && r.getContainerTypeName() == "C1"} == 1
        rep.getUnplannedPackages().size() == 0
    }
}
