package xf.xflp.opt.construction

import helper.Helper
import spock.lang.Ignore
import spock.lang.Specification
import xf.xflp.base.XFLPModel
import xf.xflp.base.XFLPParameter
import xf.xflp.base.container.Container
import xf.xflp.base.container.GroundContactRule
import xf.xflp.base.container.ParameterType
import xf.xflp.base.item.Item
import xf.xflp.opt.grasp.SingleBinOptimizedPacker

class SingleBinOptimizedPackerSpec extends Specification {

    def service = new SingleBinOptimizedPacker()

    def "hard but solvable"() {
        def items = new ArrayList<Item>()
        for (int i = 0; i < 9; i++)
            items.add(Helper.getItem(2,1,1,1,4,0))
        for (int i = 0; i < 18; i++)
            items.add(Helper.getItem(1,1,1,1,3,0))

        Collections.shuffle(items, new Random(1234))
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(4,3,3)] as Container[],
                new XFLPParameter(),
                Helper.getStatusManager()
        )
        model.containerTypes[0].parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.COVERED)

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    @Ignore
    def "scenario with item bearing capacity and stacking groups"() {
        def items = getHardScenario()
        Collections.shuffle(items, new Random(1234))
        XFLPModel model = new XFLPModel(
                items.toArray(new Item[0]),
                [Helper.getContainer(4, 9, 3, 1000)] as Container[],
                new XFLPParameter(),
                Helper.getStatusManager()
        )

        when:
        service.execute(model)
        then:
        model.containers.length == 1
        items.find {i -> i.x == -1 || i.y == -1 || i.z == -1} == null
    }

    static List<Item> getHardScenario() {
        return [
                Helper.getItem(2, 2, 1, 3, 3, 1),
                Helper.getItem(2, 2, 1, 3, 3, 1),
                Helper.getItem(2, 2, 1, 1, 1, 1),
                Helper.getItem(2, 1, 1, 3, 3, 2),
                Helper.getItem(2, 1, 1, 3, 3, 2),
                Helper.getItem(2, 1, 1, 2, 2, 1),
                Helper.getItem(2, 1, 1, 2, 2, 2),
                Helper.getItem(2, 1, 1, 1, 1, 1),
                Helper.getItem(2, 3, 1, 3, 3, 2),
                Helper.getItem(2, 3, 1, 2, 2, 2),
                Helper.getItem(2, 3, 1, 1, 1, 2),
                Helper.getItem(2, 4, 1, 1, 1, 1),
                Helper.getItem(1, 4, 1, 1, 1, 2),

                Helper.getItem(2, 4, 2, 3, 3, 1),
                Helper.getItem(1, 4, 2, 3, 3, 1),
                Helper.getItem(2, 1, 2, 3, 3, 2),
                Helper.getItem(2, 1, 2, 2, 2, 2),
                Helper.getItem(2, 3, 2, 2, 2, 1),

                // Monster-Scenario
                // getItem(1, 4, 3, 3, 3, 2)
        ]

    }
}
