package xf.xflp.opt.construction

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.container.Container
import xf.xflp.base.item.Item
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker

import java.util.stream.Collectors

class SingleContainerPackerSpec extends Specification {

    def service = new OneContainerOneTypePacker()

    def "test only adding - successful"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 27; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        XFLPModel model = getModel(items, 3, 3, 3)

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        model.containers[0].getItems().find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test only adding - one item is too many, rst fits"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 28; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        XFLPModel model = getModel(items, 3, 3, 3)

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        def plannedIds = model.containers[0].getItems().stream()
            .map {i -> i.item.externalIndex}
            .collect(Collectors.toSet()) as Set<Integer>
        items.count{i -> !plannedIds.contains(i.externalIndex)} == 1
    }

    def "test with rotation - successfully"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 18; i++)
            items.add(Helper.getItem(2,1,1,1,3,0))

        XFLPModel model = getModel(items, 4, 3, 3)

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        model.containers[0].getItems().find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test with rotation and different sizes - too hard"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(2,1,1,1,4,0))
        for (int i = 0; i < 18; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        Collections.shuffle(items, new Random(1234))
        XFLPModel model = getModel(items, 4, 3, 3)

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        model.containers[0].getItems().size() < items.size()
    }

    def "test with rotation and different sizes - sorted by size - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(2,1,1,1,4,0))
        for (int i = 0; i < 18; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        items.sort({i,j -> (j.origW * j.origL) - (i.origW * i.origL)})
        XFLPModel model = getModel(items, 4, 3, 3)

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        model.containers[0].getItems().find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test with distinct stacking groups - successful"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(1,1,1,1,3,1))
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(1,1,1,1,3,2))
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(1,1,1,1,3,4))

        Collections.shuffle(items, new Random(1234))
        XFLPModel model = getModel(items, 3, 3, 3)

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        model.containers[0].getItems().find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    static XFLPModel getModel(ArrayList<Item> items, width, length, height) {
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getAddSpaceContainer2(width, length, height)] as Container[],
                new XFLPParameter(),
                Helper.getStatusManager()
        )
        return model
    }
}
