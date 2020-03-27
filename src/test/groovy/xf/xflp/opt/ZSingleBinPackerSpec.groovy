package xf.xflp.opt

import spock.lang.Specification
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.problem.Container
import xf.xflp.base.problem.Item
import xf.xflp.opt.construction.ZSingleBinPacker

class ZSingleBinPackerSpec extends Specification {

    def itemIdx = 0

    def service = new ZSingleBinPacker()

    def "test only adding - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 27; i++)
            items.add(getItem(1,1,1,1,3,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(3,3,3)] as Container[],
                new XFLPParameter()
        )


        when:
        service.execute(model)
        then:
        model.containers.size() == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test only adding - one to many"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 28; i++)
            items.add(getItem(1,1,1,1,3,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(3,3,3)] as Container[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.size() == 1
        items.findAll {i -> i.x == -1 || i.y == -1 || i.z == -1}.size() == 1
    }

    def "test with rotation - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 18; i++)
            items.add(getItem(2,1,1,1,3,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(4,3,3)] as Container[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.size() == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test with rotation and different sizes - too hard"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(getItem(2,1,1,1,4,0))
        for (int i = 0; i < 18; i++)
            items.add(getItem(1,1,1,1,3,0))

        Collections.shuffle(items, new Random(1234))
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(4,3,3)] as Container[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.size() == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} != null
    }

    def "test with rotation and different sizes - sorted by size - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(getItem(2,1,1,1,4,0))
        for (int i = 0; i < 18; i++)
            items.add(getItem(1,1,1,1,3,0))

        items.sort({i,j -> (j.w*j.l) - (i.w*i.l)})
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(4,3,3)] as Container[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.size() == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "test with distinct stacking groups - sucessfull"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(getItem(1,1,1,1,3,1))
        for (int i = 0; i < 9; i++)
            items.add(getItem(1,1,1,1,3,2))
        for (int i = 0; i < 9; i++)
            items.add(getItem(1,1,1,1,3,4))

        Collections.shuffle(items, new Random(1234))
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(3,3,3)] as Container[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.size() == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    private Container getContainer(int width, int length, int height) {
        return new Container(width, length, height, 999999999, 0, 0);
    }
    private Container getContainer(int width, int length, int height, float maxWeight) {
        return new Container(width, length, height, maxWeight, 0, 0);
    }

    private Item getItem(int w, int l, int h, int ww, long wC, int sG) {
        return getItem(w, l, h, ww, wC, sG, Math.max(1,sG));
    }
    private Item getItem(int w, int l, int h, int ww, long wC, int sG, int allowedSG) {
        Set<Integer> set = new HashSet<>();
        set.add(0);

        Item i = new Item(
                itemIdx,
                itemIdx,
                1,
                2,
                w,
                l,
                h,
                ww,
                wC,
                set,
                Math.max(1,sG),
                allowedSG,
                true,
                true
        );
        itemIdx++;

        return i;
    }
}
