package xf.xflp.opt.construction.strategy

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.item.Item
import xf.xflp.base.item.Position
import xf.xflp.base.item.RotatedPosition
import xf.xflp.exception.XFLPException

class WidthProportionFactorTest extends Specification {

    def service = new WidthProportionFactor()
    def container = Helper.getContainer(240, 1000, 1)

    def "choose min proportion - one pos wins"() {
        def item = new Item()
        item.l = 120
        item.w = 100
        def item2 = new Item()
        item2.l = 100
        item2.w = 80
        def posList = [
                new Position(1,1),
                new RotatedPosition(new Position(1,1))
        ]

        when:
        def result = service.choose(item, container, posList)
        def result2 = service.choose(item2, container, posList)
        then:
        result == posList[1]
        result2 == posList[0]
    }

    def "choose min proportion - one position"() {
        def item = new Item()
        def posList = [
                new Position(1,1)
        ]
        when:
        def r = service.choose(item, container, posList)
        then:
        r == posList[0]
    }

    def "choose min proportion - zero positions"() {
        def item = new Item()
        when:
        service.choose(item, container, [])
        then:
        thrown(XFLPException)
    }
}
