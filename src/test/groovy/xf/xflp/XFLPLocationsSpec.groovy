package xf.xflp


import spock.lang.Specification
import xf.xflp.opt.XFLPOptType

class XFLPLocationsSpec extends Specification {

    def "With locations - infeasiable solution"() {
        XFLP xflp = new XFLP()
        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
        xflp.getParameter().setLifoImportance(1)
        xflp.addContainer().setLength(13500).setWidth(25).setHeight(30).setMaxWeight(25000000)

        xflp.addItem().setExternID("Packet1").setLength(135).setWidth(25).setHeight(30).setWeight(25000).setLoadingLocation("1").setUnloadingLocation("5")
        xflp.addItem().setExternID("Packet2").setLength(135).setWidth(25).setHeight(30).setWeight(25000).setLoadingLocation("2").setUnloadingLocation("3")
        xflp.addItem().setExternID("Packet3").setLength(135).setWidth(25).setHeight(30).setWeight(25000).setLoadingLocation("4").setUnloadingLocation("6")

        when:
        xflp.executeLoadPlanning()
        var report = xflp.getReport()

        then:
        report.getUnplannedPackages().size() > 0
    }

    def "With locations - valid solution"() {
        XFLP xflp = new XFLP()
        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
        xflp.getParameter().setLifoImportance(1)
        xflp.addContainer().setLength(13500).setWidth(25).setHeight(30).setMaxWeight(25000000)

        xflp.addItem().setExternID("Packet1").setLength(135).setWidth(25).setHeight(30).setWeight(25000).setLoadingLocation("1").setUnloadingLocation("5")
        xflp.addItem().setExternID("Packet2").setLength(135).setWidth(25).setHeight(30).setWeight(25000).setLoadingLocation("2").setUnloadingLocation("3")
        xflp.addItem().setExternID("Packet3").setLength(135).setWidth(25).setHeight(30).setWeight(25000).setLoadingLocation("5").setUnloadingLocation("7")

        when:
        xflp.executeLoadPlanning()
        var report = xflp.getReport()

        then:
        report.getUnplannedPackages().size() == 0
    }

}
