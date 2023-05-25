package xf.xflp.report

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container

class LPReportTest extends Specification {

    static def e1 = new LPPackageEvent(
            "BLUB",
            123,
            456,
            789,
            135,
            246,
            357,
            88,
            99,
            66,
            true,
            LoadType.LOAD,
            1,
            222,
            333
    )

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
        def con = getAddSpaceContainer2(2,2,3)
        def rep = new ContainerReport('C1', con)

        def e2 = new LPPackageEvent(
                "BLUB",
                123,
                456,
                789,
                135,
                246,
                357,
                88,
                99,
                66,
                true,
                LoadType.LOAD,
                3,
                444,
                333
        )

        rep.add(e1)
        rep.add(e2)
        rep.add(e1)

        return rep
    }

    static Container getAddSpaceContainer2(int width, int length, int height) {
        return Helper.getAddSpaceContainer2(width, length, height, 999999999)
    }

}
