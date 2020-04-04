package xf.xflp.opt.construction

import spock.lang.Specification
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.problem.Container
import xf.xflp.base.problem.Item
import xf.xflp.opt.grasp.SingleBinOptimizedPacker

class SingleBinOptimizedPackerSpec extends Specification {

    def itemIdx = 0

    def service = new SingleBinOptimizedPacker()

    def "hard but solveable"() {
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
        long t = System.nanoTime()
        service.execute(model)
        println 'XXX '+(System.nanoTime() - t)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    def "scenario with item bearing capacity and stacking groups"() {
        def items = getHardScenario()
        Collections.shuffle(items, new Random(1234))
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [getContainer(4, 9, 3, 1000)] as Container[],
                new XFLPParameter()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    private List<Item> getHardScenario() {
        return [
                getItem(2, 2, 1, 3, 3, 1),
                getItem(2, 2, 1, 3, 3, 1),
                getItem(2, 2, 1, 1, 1, 1),
                getItem(2, 1, 1, 3, 3, 2),
                getItem(2, 1, 1, 3, 3, 2),
                getItem(2, 1, 1, 2, 2, 1),
                getItem(2, 1, 1, 2, 2, 2),
                getItem(2, 1, 1, 1, 1, 1),
                getItem(2, 3, 1, 3, 3, 2),
                getItem(2, 3, 1, 2, 2, 2),
                getItem(2, 3, 1, 1, 1, 2),
                getItem(2, 4, 1, 1, 1, 1),
                getItem(1, 4, 1, 1, 1, 2),

                getItem(2, 4, 2, 3, 3, 1),
                getItem(1, 4, 2, 3, 3, 1),
                getItem(2, 1, 2, 3, 3, 2),
                getItem(2, 1, 2, 2, 2, 2),
                getItem(2, 3, 2, 2, 2, 1),

                // Monster-Scenario
                // getItem(1, 4, 3, 3, 3, 2)
        ]

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
