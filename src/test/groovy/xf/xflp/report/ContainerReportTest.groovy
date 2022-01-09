package xf.xflp.report

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.Container

class ContainerReportTest extends Specification {

    def "Create an empty report"() {
        def con = getContainer(2,2,3)
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
        def con = getContainer(2,2,3)
        def rep = new ContainerReport('C1', con)

        def e = new LPPackageEvent()
        e.setId("BLUB")
        e.setX(123)
        e.setY(456)
        e.setZ(789)
        e.setWidth(135)
        e.setLength(246)
        e.setHeight(357)
        e.setStackingGrp(88)
        e.setWeight(99)
        e.setWeightLimit(66)
        e.setInvalid(true)
        e.setType(LoadType.LOAD)
        e.setUsedVolumeInContainer(1)
        e.setUsedWeightInContainer(222)
        e.setNbrStacksInContainer(333)

        when:
        rep.add(e)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 1
        rep.getPackageEvents().find {f -> f == e} != null
        Math.abs(sum.getUtilization() - e.getUsedVolumeInContainer() / (2 * 2 * 3)) < 0.1
        sum.getMaxUsedVolume() == e.getUsedVolumeInContainer()
        sum.getMaxUsedWeight() == e.getUsedWeightInContainer()
        sum.getNbrOfLoadedPackages() == 1
        sum.getNbrOfUnLoadedPackages() == 0
    }

    def "Add a package event - load then unload"() {
        def con = getContainer(2,2,3)
        def rep = new ContainerReport('C1', con)

        def eLoad = new LPPackageEvent()
        eLoad.setId("BLUB")
        eLoad.setX(123)
        eLoad.setY(456)
        eLoad.setZ(789)
        eLoad.setWidth(135)
        eLoad.setLength(246)
        eLoad.setHeight(357)
        eLoad.setStackingGrp(88)
        eLoad.setWeight(99)
        eLoad.setWeightLimit(66)
        eLoad.setInvalid(true)
        eLoad.setType(LoadType.LOAD)
        eLoad.setUsedVolumeInContainer(1)
        eLoad.setUsedWeightInContainer(222)
        eLoad.setNbrStacksInContainer(333)
        rep.add(eLoad)

        def ee = new LPPackageEvent()
        ee.setId("BLUB")
        ee.setX(123)
        ee.setY(456)
        ee.setZ(789)
        ee.setWidth(135)
        ee.setLength(246)
        ee.setHeight(357)
        ee.setStackingGrp(88)
        ee.setWeight(99)
        ee.setWeightLimit(66)
        ee.setInvalid(true)
        ee.setType(LoadType.UNLOAD)
        ee.setUsedVolumeInContainer(1)
        ee.setUsedWeightInContainer(222)
        ee.setNbrStacksInContainer(333)

        when:
        rep.add(ee)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 2
        rep.getPackageEvents().find {f -> f == ee} != null
        Math.abs(sum.getUtilization() - eLoad.getUsedVolumeInContainer() / (2 * 2 * 3)) < 0.1
        sum.getMaxUsedVolume() == eLoad.getUsedVolumeInContainer()
        sum.getMaxUsedWeight() == eLoad.getUsedWeightInContainer()
        sum.getNbrOfLoadedPackages() == 1
        sum.getNbrOfUnLoadedPackages() == 1
    }

    def "Add a package event - only unload (virtual)"() {
        def con = getContainer(2,2,3)
        def rep = new ContainerReport('C1', con)

        def ee = new LPPackageEvent()
        ee.setId("BLUB")
        ee.setX(123)
        ee.setY(456)
        ee.setZ(789)
        ee.setWidth(135)
        ee.setLength(246)
        ee.setHeight(357)
        ee.setStackingGrp(88)
        ee.setWeight(99)
        ee.setWeightLimit(66)
        ee.setInvalid(true)
        ee.setType(LoadType.UNLOAD)
        ee.setUsedVolumeInContainer(1)
        ee.setUsedWeightInContainer(222)
        ee.setNbrStacksInContainer(333)

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

        when:
        rep.add(e1)
        rep.add(e2)
        def sum = rep.getSummary()
        then:
        rep.getPackageEvents().size() == 2
        rep.getPackageEvents().find {f -> f == e1} != null
        rep.getPackageEvents().find {f -> f == e2} != null
        Math.abs(sum.getUtilization() - (e1.getUsedVolumeInContainer() + e2.getUsedVolumeInContainer()) / (2 * 2 * 3)) < 0.1
        sum.getMaxUsedVolume() == (e1.getUsedVolumeInContainer() + e2.getUsedVolumeInContainer())
        sum.getMaxUsedWeight() == e2.getUsedWeightInContainer()
        sum.getNbrOfLoadedPackages() == 2
        sum.getNbrOfUnLoadedPackages() == 0
        rep.iterator().size() == 2
    }

    static Container getContainer(int width, int length, int height) {
        return Helper.getContainer(width, length, height, 999999999)
    }
}
