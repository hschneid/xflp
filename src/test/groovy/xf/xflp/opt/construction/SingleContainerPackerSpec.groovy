package xf.xflp.opt.construction

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.problem.ComplexContainer
import xf.xflp.base.problem.Item
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker

class SingleContainerPackerSpec extends Specification {

    def service = new OneContainerOneTypePacker()

    def "test only adding - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 27; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(3,3,3)] as ComplexContainer[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test only adding - one to many"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 28; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(3,3,3)] as ComplexContainer[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.findAll {i -> i.x == -1 || i.y == -1 || i.z == -1}.size() == 1
    }

    def "test with rotation - successfully"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 18; i++)
            items.add(Helper.getItem(2,1,1,1,3,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(4,3,3)] as ComplexContainer[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test with rotation and different sizes - too hard"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(2,1,1,1,4,0))
        for (int i = 0; i < 18; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        Collections.shuffle(items, new Random(1234))
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(4,3,3)] as ComplexContainer[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} != null
    }

    def "test with rotation and different sizes - sorted by size - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(2,1,1,1,4,0))
        for (int i = 0; i < 18; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        items.sort({i,j -> (j.w*j.l) - (i.w*i.l)})
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(4,3,3)] as ComplexContainer[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test with distinct stacking groups - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(1,1,1,1,3,1))
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(1,1,1,1,3,2))
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(1,1,1,1,3,4))

        Collections.shuffle(items, new Random(1234))
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(3,3,3)] as ComplexContainer[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }
}
