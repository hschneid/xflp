package xf.xflp.report

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container

class ContainerReportTest extends Specification {

    static def e = new LPPackageEvent(
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

    static def ee = new LPPackageEvent(
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
            LoadType.UNLOAD,
            1,
            222,
            333
    )

    def "Create an empty report"() {
        def con = getAddSpaceContainer2(2,2,3)
        def containerTypeName = "C1"

        when:
        def rep = new ContainerReport(containerTypeName, con)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 0
        rep.getContainer() == con
        rep.getContainerTypeName() == containerTypeName
        sum != null
        sum.getUtilization() == 0
        sum.getMaxUsedVolume() == 0
        sum.getMaxUsedWeight() == 0
        sum.getMaxVolume() == 2 * 2 * 3
        sum.getNbrOfLoadedPackages() == 0
        sum.getNbrOfUnLoadedPackages() == 0
    }

    def "Add a package event - load"() {
        def con = getAddSpaceContainer2(2,2,3)
        def rep = new ContainerReport('C1', con)

        when:
        rep.add(e)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 1
        rep.getPackageEvents().find {f -> f == e} != null
        Math.abs(sum.getUtilization() - e.usedVolumeInContainer() / (2 * 2 * 3)) < 0.1
        sum.getMaxUsedVolume() == e.usedVolumeInContainer()
        sum.getMaxUsedWeight() == e.usedWeightInContainer()
        sum.getNbrOfLoadedPackages() == 1
        sum.getNbrOfUnLoadedPackages() == 0
    }

    def "Add a package event - load then unload"() {
        def con = getAddSpaceContainer2(2,2,3)
        def rep = new ContainerReport('C1', con)

        rep.add(e)

        when:
        rep.add(ee)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 2
        rep.getPackageEvents().find {f -> f == ee} != null
        Math.abs(sum.getUtilization() - e.usedVolumeInContainer() / (2 * 2 * 3)) < 0.1
        sum.getMaxUsedVolume() == e.usedVolumeInContainer()
        sum.getMaxUsedWeight() == e.usedWeightInContainer()
        sum.getNbrOfLoadedPackages() == 1
        sum.getNbrOfUnLoadedPackages() == 1
    }

    def "Add a package event - only unload (virtual)"() {
        def con = getAddSpaceContainer2(2,2,3)
        def rep = new ContainerReport('C1', con)

        when:
        rep.add(ee)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 1
        rep.getPackageEvents().find {f -> f == ee} != null
        sum.getUtilization() == 0
        sum.getMaxUsedVolume() == 0
        sum.getMaxUsedWeight() == 0
        sum.getNbrOfLoadedPackages() == 0
        sum.getNbrOfUnLoadedPackages() == 1
    }

    def "Add a package event - multiple loads"() {
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

        when:
        rep.add(e)
        rep.add(e2)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 2
        rep.getPackageEvents().find {f -> f == e} != null
        rep.getPackageEvents().find {f -> f == e2} != null
        Math.abs(sum.getUtilization() - (e.usedVolumeInContainer() + e2.usedVolumeInContainer()) / (2 * 2 * 3)) < 0.1
        sum.getMaxUsedVolume() == (e.usedVolumeInContainer() + e2.usedVolumeInContainer())
        sum.getMaxUsedWeight() == e.usedWeightInContainer() + e2.usedWeightInContainer()
        sum.getNbrOfLoadedPackages() == 2
        sum.getNbrOfUnLoadedPackages() == 0
        rep.iterator().size() == 2
    }

    static Container getAddSpaceContainer2(int width, int length, int height) {
        return Helper.getAddSpaceContainer2(width, length, height, 999999999)
    }
}
