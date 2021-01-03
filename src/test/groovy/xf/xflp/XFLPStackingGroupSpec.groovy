package xf.xflp

import spock.lang.Specification

class XFLPStackingGroupSpec extends Specification {

    def service = new XFLP()

    def "no stacking groups at all"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID('1').setLength(1).setWidth(1).setHeight(1)

        when:
        service.importer.finishImport()
        def result = service.init()
        then:
        result.items[0].stackingGroup == 1
        result.items[0].allowedStackingGroups == 1
    }

    def "with stacking group data"() {
        service.addContainer().setWidth(3).setLength(3).setHeight(2).setMaxWeight(10)
        service.addItem().setExternID('1').setLength(1).setWidth(1).setHeight(1)
                .setStackingGroup('A')
                .setAllowedStackingGroups('A')
        service.addItem().setExternID('2').setLength(2).setWidth(1).setHeight(1)
                .setStackingGroup('B')
                .setAllowedStackingGroups('B')
        service.addItem().setExternID('3').setLength(3).setWidth(1).setHeight(1)
                .setStackingGroup('A')
                .setAllowedStackingGroups('A,B')

        when:
        service.importer.finishImport()
        def result = service.init()
        then:
        result.items.find {i -> i.l == 1 && i.stackingGroup == 2} != null
        result.items.find {i -> i.l == 1 && i.allowedStackingGroups == 2} != null
        result.items.find {i -> i.l == 2 && i.stackingGroup == 4} != null
        result.items.find {i -> i.l == 2 && i.allowedStackingGroups == 4} != null
        result.items.find {i -> i.l == 3 && i.stackingGroup == 2} != null
        result.items.find {i -> i.l == 3 && i.allowedStackingGroups == 6} != null
    }

}
