package xf.xflp.opt.construction

import spock.lang.Specification
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.problem.Container
import xf.xflp.base.problem.Item

class DoubleContainerAddPackerTest extends Specification {

    def itemIdx = 0

    def service = new DoubleContainerAddPacker()

    def "test simple adding - all items fit"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 39; i++)
            items.add(getItem(1,1,1,1,1000,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(3,3,3), getContainer(2,2,3)] as Container[],
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
            items.add(getItem(1,1,1,1,1000,0))

        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(3,3,3), getContainer(2,2,3)] as Container[],
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
