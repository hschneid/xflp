package xf.xflp.opt.construction


import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType

class FastFixedContainerPackerSpec extends Specification {

    private XFLP init(int width, int length, int height) {
        def service = new XFLP()

        service.addContainer().setWidth(width).setLength(length).setHeight(height).setMaxWeight(999999)
        service.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)

        return service
    }

    def "test only adding - successful"() {
        def xflp = init(3, 3, 3)
        for (int i = 0; i < 27; i++)
            createItem(xflp, i, 1,1,1,1,99999,99999,0,"",0)

        when:
        xflp.executeLoadPlanning()
        def report = xflp.getReport()
        then:
        report.getContainerReports().size() == 1
        report.getUnplannedPackages().size() == 0
    }

    def "test only adding - one item is too many, rst fits"() {
        def xflp = init(3, 3, 3)
        for (int i = 0; i < 28; i++)
            createItem(xflp, i, 1,1,1,1,99999,99999,0,"",0)

        when:
        xflp.executeLoadPlanning()
        def report = xflp.getReport()
        then:
        report.getContainerReports().size() == 1
        report.getUnplannedPackages().size() == 1
    }

    def "test with rotation - successfully"() {
        def xflp = init(4, 3, 3)
        for (int i = 0; i < 18; i++)
            createItem(xflp, i, 2,1,1,1,99999,99999,0,"",0)

        when:
        xflp.executeLoadPlanning()
        def report = xflp.getReport()
        then:
        report.getContainerReports().size() == 1
        report.getUnplannedPackages().size() == 0
    }

    def "test with rotation and different sizes - too hard"() {
        // Build item definitions and shuffle them like in SingleContainerPackerSpec
        def itemDefs = []
        for (int i = 0; i < 9; i++)
            itemDefs.add([i, 2,1,1,1])
        for (int i = 9; i < 27; i++)
            itemDefs.add([i, 1,1,1,1])
        Collections.shuffle(itemDefs, new Random(1234))

        def xflp = init(4, 3, 3)
        itemDefs.each { d -> createItem(xflp, d[0], d[1],d[2],d[3],d[4],99999,99999,0,"",0) }

        when:
        xflp.executeLoadPlanning()
        def report = xflp.getReport()
        then:
        report.getContainerReports().size() == 1
        report.getUnplannedPackages().size() > 0
    }

    def "test with rotation and different sizes - sorted by size - sucessfull"() {
        def xflp = init(4, 3, 3)
        for (int i = 0; i < 9; i++)
            createItem(xflp, i, 2,1,1,1,99999,99999,0,"",0)
        for (int i = 9; i < 27; i++)
            createItem(xflp, i, 1,1,1,1,99999,99999,0,"",0)

        when:
        xflp.executeLoadPlanning()
        def report = xflp.getReport()
        then:
        report.getContainerReports().size() == 1
        report.getUnplannedPackages().size() == 0
    }

    def "test with distinct stacking groups - successful"() {
        def xflp = init(3, 3, 3)
        for (int i = 0; i < 9; i++)
            createItem(xflp, i, 1,1,1,1,99999,99999,1,"1",0)
        for (int i = 9; i < 18; i++)
            createItem(xflp, i, 1,1,1,1,99999,99999,2,"2",0)
        for (int i = 18; i < 27; i++)
            createItem(xflp, i, 1,1,1,1,99999,99999,4,"4",0)

        when:
        xflp.executeLoadPlanning()
        def report = xflp.getReport()
        then:
        report.getContainerReports().size() == 1
        report.getUnplannedPackages().size() == 0
    }

    private static createItem(XFLP service,
                              int bi,
                              int length,
                              int w,
                              int h,
                              int weight,
                              int bearingWeight,
                              int nbrOfItemsToStacked,
                              int stackingGroup,
                              String stackingGroups,
                              int immersiveDepth
    ) {
        def item = service
                .addItem()
                .setExternID(bi+"")
                .setWidth(w)
                .setHeight(h)
                .setLength(length)
                .setWeight(weight)
                .setStackingWeightLimit(bearingWeight)
                .setNbrOfAllowedStackedItems(nbrOfItemsToStacked)
                .setImmersiveDepth(immersiveDepth)

        if (stackingGroup > 0) {
            item.setStackingGroup(stackingGroup + " ")
            item.setAllowedStackingGroups(stackingGroups)
        }
    }

}
