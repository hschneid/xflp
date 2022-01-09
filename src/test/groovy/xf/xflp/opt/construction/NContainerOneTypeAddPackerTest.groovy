package xf.xflp.opt.construction

import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType

class NContainerOneTypeAddPackerTest extends Specification {

    def "test min container for one type"() {
        def xflp = new XFLP()
        xflp.addContainer().setWidth(3).setLength(3).setHeight(3)
        for (i in 0..<100) {
            xflp.addItem().setExternID("P"+i).setWidth(1).setLength(1).setHeight(1).setWeight(1)
        }
        xflp.setTypeOfOptimization(XFLPOptType.FAST_MIN_CONTAINER_PACKER)

        when:
        xflp.executeLoadPlanning()
        def rep = xflp.getReport()

        then:
        rep.getContainerReports().size() == 4
        rep.getContainerReports().count {r -> r.getSummary().getUtilization() > 0.99} == 3
        rep.getContainerReports().count {r -> r.getSummary().getNbrOfLoadedPackages() == 19} == 1
        rep.getUnplannedPackages().size() == 0
    }
}
