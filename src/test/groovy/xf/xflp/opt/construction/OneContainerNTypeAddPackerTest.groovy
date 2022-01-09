package xf.xflp.opt.construction

import helper.Helper
import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.container.Container
import xf.xflp.base.item.Item
import xf.xflp.opt.XFLPOptType
import xf.xflp.opt.construction.multitype.OneContainerNTypeAddPacker

class OneContainerNTypeAddPackerTest extends Specification {

    def service = new OneContainerNTypeAddPacker()

    def "test one container set for multiple types"() {
        def xflp = new XFLP()
        xflp.addContainer().setContainerType("C1").setWidth(3).setLength(3).setHeight(3)
        xflp.addContainer().setContainerType("C2").setWidth(2).setLength(2).setHeight(2)
        for (i in 0..<50) {
            xflp.addItem().setExternID("P"+i).setWidth(1).setLength(1).setHeight(1).setWeight(1)
        }
        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)

        when:
        xflp.executeLoadPlanning()
        def rep = xflp.getReport()

        then:
        rep.getContainerReports().size() == 2
        rep.getContainerReports().count {r -> r.getContainerTypeName() == "C1"} == 1
        rep.getContainerReports().count {r -> r.getContainerTypeName() == "C2"} == 1
        rep.getContainerReports().count {r -> r.getSummary().getUtilization() > 0.99} == 2
        rep.getUnplannedPackages().size() == 15
    }

    def "test simple adding - all items fit"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 39; i++)
            items.add(Helper.getItem(1,1,1,1,1000,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(3,3,3),
                 Helper.getContainer(2,2,3)] as Container[],
                new XFLPParameter(),
                Helper.getStatusManager()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 2
        model.containers[0].history.size() == 27
        model.containers[1].history.size() == 12
        // all have a position
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test simple adding - one item is not fitting"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 40; i++)
            items.add(Helper.getItem(1,1,1,1,1000,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(3,3,3),
                 Helper.getContainer(2,2,3)] as Container[],
                new XFLPParameter(),
                Helper.getStatusManager()
        )


        when:
        service.execute(model)
        then:
        model.containers.length == 2
        model.containers[0].history.size() == 27
        model.containers[1].history.size() == 12
        // all have a position
        items.findAll {i -> i.x == -1 || i.y == -1 || i.z == -1}.size() == 1
    }

}
