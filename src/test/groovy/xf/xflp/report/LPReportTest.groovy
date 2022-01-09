package xf.xflp.report

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container

class LPReportTest extends Specification {

    def "Create empty report"() {
        when:
        def report = new LPReport()
        def sum = report.getSummary()
        then:
        report.getContainerReports().size() == 0
        report.getUnplannedPackages().size() == 0
        report.iterator().size() == 0
        sum.getNbrOfUsedVehicles() == 0
        sum.getNbrOfNotLoadedPackages() == 0
        sum.getUtilization() == 0
    }

    def "Add a container report"() {
        def rep = new LPReport()

        when:
        rep.add(getContainerReport())
        def sum = rep.getSummary()
        then:
        rep.getContainerReports().size() == 1
        rep.getUnplannedPackages().size() == 0
        rep.iterator().size() == 1
        sum.getNbrOfUsedVehicles() == 1
        sum.getNbrOfNotLoadedPackages() == 0
        Math.abs(sum.getUtilization() - 4/12) < 0.1
    }

    def "Add unplanned items"() {
        def rep = new LPReport()
        def e1 = new LPPackageEvent()
        e1.setId("BLUB")
        e1.setX(123)
        e1.setY(456)
        e1.setZ(789)
        e1.setWidth(135)
        e1.setLength(246)
        e1.setHeight(357)
        e1.setStackingGrp(88)
        e1.setWeight(99)
        e1.setWeightLimit(66)
        e1.setInvalid(true)
        e1.setType(LoadType.LOAD)
        e1.setUsedVolumeInContainer(1)
        e1.setUsedWeightInContainer(222)
        e1.setNbrStacksInContainer(333)

        when:
        rep.addUnplannedPackages(e1)
        def sum = rep.getSummary()
        then:
        rep.getContainerReports().size() == 0
        rep.getUnplannedPackages().size() == 1
        rep.getUnplannedPackages().find{f -> f == e1} != null
        rep.iterator().size() == 0
        sum.getNbrOfUsedVehicles() == 0
        sum.getNbrOfNotLoadedPackages() == 1
        sum.getUtilization() == 0
    }

    def "Import from other report"() {
        def rep = new LPReport()
        def e1 = new LPPackageEvent()
        e1.setId("BLUB")
        e1.setX(123)
        e1.setY(456)
        e1.setZ(789)
        e1.setWidth(135)
        e1.setLength(246)
        e1.setHeight(357)
        e1.setStackingGrp(88)
        e1.setWeight(99)
        e1.setWeightLimit(66)
        e1.setInvalid(true)
        e1.setType(LoadType.LOAD)
        e1.setUsedVolumeInContainer(1)
        e1.setUsedWeightInContainer(222)
        e1.setNbrStacksInContainer(333)
        rep.addUnplannedPackages(e1)
        rep.add(getContainerReport())
        when:
        def newRep = new LPReport()
        newRep.importReport(rep)
        def sum = newRep.getSummary()
        then:
        newRep.getContainerReports().size() == 1
        newRep.getUnplannedPackages().size() == 1
        newRep.iterator().size() == 1
        sum.getNbrOfUsedVehicles() == 1
        sum.getNbrOfNotLoadedPackages() == 1
        Math.abs(sum.getUtilization() - 4/12) < 0.1
    }

    static ContainerReport getContainerReport() {
        def con = getContainer(2,2,3)
        def rep = new ContainerReport('C1', con)

        def e1 = new LPPackageEvent()
        e1.setId("BLUB")
        e1.setX(123)
        e1.setY(456)
        e1.setZ(789)
        e1.setWidth(135)
        e1.setLength(246)
        e1.setHeight(357)
        e1.setStackingGrp(88)
        e1.setWeight(99)
        e1.setWeightLimit(66)
        e1.setInvalid(true)
        e1.setType(LoadType.LOAD)
        e1.setUsedVolumeInContainer(1)
        e1.setUsedWeightInContainer(222)
        e1.setNbrStacksInContainer(333)

        def e2 = new LPPackageEvent()
        e2.setId("BLUB")
        e2.setX(123)
        e2.setY(456)
        e2.setZ(789)
        e2.setWidth(135)
        e2.setLength(246)
        e2.setHeight(357)
        e2.setStackingGrp(88)
        e2.setWeight(99)
        e2.setWeightLimit(66)
        e2.setInvalid(true)
        e2.setType(LoadType.LOAD)
        e2.setUsedVolumeInContainer(3)
        e2.setUsedWeightInContainer(444)
        e2.setNbrStacksInContainer(333)

        def e3 = new LPPackageEvent()
        e3.setId("BLUB")
        e3.setX(123)
        e3.setY(456)
        e3.setZ(789)
        e3.setWidth(135)
        e3.setLength(246)
        e3.setHeight(357)
        e3.setStackingGrp(88)
        e3.setWeight(99)
        e3.setWeightLimit(66)
        e3.setInvalid(true)
        e3.setType(LoadType.UNLOAD)
        e3.setUsedVolumeInContainer(1)
        e3.setUsedWeightInContainer(222)
        e3.setNbrStacksInContainer(333)

        rep.add(e1)
        rep.add(e2)
        rep.add(e3)

        return rep
    }

    static Container getContainer(int width, int length, int height) {
        return Helper.getContainer(width, length, height, 999999999)
    }

}
