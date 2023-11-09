package xf.xflp

import spock.lang.Specification
import xf.xflp.opt.XFLPOptType

class XFLPVirtualItemsSpec extends Specification {

    def xflp = new XFLP()

    def "plan with virtual item in OneContainer"() {
        xflp.addContainer().setWidth(10).setLength(10).setHeight(3).setMaxWeight(10000)
        xflp.addItem().setExternID('1').setWeight(1).setLength(2).setWidth(1).setHeight(1)
        xflp.addItem().setExternID('2').setWeight(1).setLength(2).setWidth(1).setHeight(1)
        xflp.addItem().setExternID('3').setWeight(1).setLength(2).setWidth(1).setHeight(1)

        for (i in 1..<9000) {
            xflp.addItem().setExternID('VI' + i).setWeight(1).setLength(0).setWidth(0).setHeight(0)
        }
        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)

        when:
        xflp.executeLoadPlanning()
        def result = xflp.getReport()

        then:
        result.getContainerReports()[0].getSummary().getMaxUsedWeight() < 10000
        result.getContainerReports()[0].getPackageEvents().size() == 9002
    }

    def "plan with virtual item in ManyContainer"() {
        xflp.addContainer().setWidth(10).setLength(10).setHeight(3).setMaxWeight(5000)
        xflp.addContainer().setWidth(10).setLength(10).setHeight(3).setMaxWeight(5000)
        xflp.addItem().setExternID('1').setWeight(1).setLength(2).setWidth(1).setHeight(1)
        xflp.addItem().setExternID('2').setWeight(1).setLength(2).setWidth(1).setHeight(1)
        xflp.addItem().setExternID('3').setWeight(1).setLength(2).setWidth(1).setHeight(1)

        for (i in 1..<9000) {
            xflp.addItem().setExternID('VI' + i).setWeight(1).setLength(0).setWidth(0).setHeight(0)
        }
        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)

        when:
        xflp.executeLoadPlanning()
        def result = xflp.getReport()

        then:
        result.getContainerReports()[0].getSummary().getMaxUsedWeight() <= 5000
        result.getContainerReports()[1].getSummary().getMaxUsedWeight() <= 5000
        result.getContainerReports()[0].getPackageEvents().size() == 5000
        result.getContainerReports()[1].getPackageEvents().size() == 4002
    }
}
