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

    def "test max number of containers"() {
        def xflp = new XFLP()
        xflp.addContainer().setWidth(3).setLength(3).setHeight(3)
        for (i in 0..<100) {
            xflp.addItem().setExternID("P"+i).setWidth(1).setLength(1).setHeight(1).setWeight(1)
        }
        xflp.setTypeOfOptimization(XFLPOptType.FAST_MIN_CONTAINER_PACKER)
        xflp.getParameter().setMaxNbrOfContainer(2)

        when:
        xflp.executeLoadPlanning()
        def rep = xflp.getReport()

        then:
        rep.getContainerReports().size() == 2
        rep.getContainerReports().count {r -> r.getSummary().getUtilization() > 0.99} == 2
        rep.getUnplannedPackages().size() == 46
    }

    def "test unfitting items"() {
        def xflp = new XFLP()
        xflp.addContainer().setWidth(3).setLength(3).setHeight(3)
        for (i in 0..<100) {
            xflp.addItem().setExternID("P"+i).setWidth(1).setLength(1).setHeight(1).setWeight(1)
        }
        xflp.addItem().setExternID("PX").setWidth(4).setLength(1).setHeight(1).setWeight(1)
        xflp.setTypeOfOptimization(XFLPOptType.FAST_MIN_CONTAINER_PACKER)
        xflp.getParameter().setMaxNbrOfContainer(Integer.MAX_VALUE)

        when:
        def time = System.currentTimeMillis()
        xflp.executeLoadPlanning()
        def rep = xflp.getReport()
        time = System.currentTimeMillis() - time

        then:
        rep.getContainerReports().size() == 4
        rep.getUnplannedPackages().size() == 1
        rep.getUnplannedPackages()[0].id == 'PX'
        // Even with lots of possible containers it is quite fast,
        // as it breaks after finding the unfitting item.
        time < 1000
    }
}
