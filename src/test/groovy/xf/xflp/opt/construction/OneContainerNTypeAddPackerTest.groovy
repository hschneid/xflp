package xf.xflp.opt.construction

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.problem.Container
import xf.xflp.base.problem.Item
import xf.xflp.opt.construction.multitype.OneContainerNTypeAddPacker

class OneContainerNTypeAddPackerTest extends Specification {

    def service = new OneContainerNTypeAddPacker()

    def "test simple adding - all items fit"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 39; i++)
            items.add(Helper.getItem(1,1,1,1,1000,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(3,3,3), Helper.getContainer(2,2,3)] as Container[],
                new XFLPParameter()
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
                [Helper.getContainer(3,3,3), Helper.getContainer(2,2,3)] as Container[],
                new XFLPParameter()
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
