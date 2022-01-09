package benchmarks

import spock.lang.Ignore
import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType

import java.util.stream.IntStream

class RatcliffSpec extends Specification {

    def service = new XFLP()

    @Ignore
    def "BR1 - 1 test"() {
        service.addContainer().setWidth(223).setLength(587).setHeight(220).setMaxWeight(10000)
        service.setTypeOfOptimization(XFLPOptType.BEST_FIXED_CONTAINER_PACKER)

        createItem(1, 42, 26, 64, 62)
        createItem(2, 58, 52, 92, 53)
        createItem(3, 85, 21, 92, 67)

        when:
        service.executeLoadPlanning()
        def result = service.getReport()
        // Opt sol is > 82%, curr XFLP is 82%
        println result.getSummary().getUtilization()
        then:
        result != null
    }

    @Ignore
    def "BR7 - 1 test"() {
        service.addContainer().setWidth(223).setLength(587).setHeight(220).setMaxWeight(10000)
        service.setTypeOfOptimization(XFLPOptType.BEST_FIXED_CONTAINER_PACKER)

        createItem(1, 76, 30, 108, 10)
        createItem(2, 43, 25, 110, 6)
        createItem(3, 81, 55, 92, 5)
        createItem(4, 33, 28, 81, 5)
        createItem(5, 99, 73, 120, 6)
        createItem(6, 70, 48, 111, 4)
        createItem(7, 72, 46, 98, 7)
        createItem(8, 66, 31, 95, 6)
        createItem(9, 84, 30, 85, 5)
        createItem(10, 32, 25, 71, 5)
        createItem(11, 34, 25, 36, 4)
        createItem(12, 67, 62, 97, 8)
        createItem(13, 25, 23, 33, 2)
        createItem(14, 27, 26, 95, 5)
        createItem(15, 81, 44, 94, 8)
        createItem(16, 39, 38, 41, 4)
        createItem(17, 74, 65, 104, 4)
        createItem(18, 41, 36, 52, 4)
        createItem(19, 78, 34, 104, 6)
        createItem(20, 77, 46, 83, 6)

        when:
        service.executeLoadPlanning()
        def result = service.getReport()
        // Opt sol is > 82%, curr XFLP is 82%
        println result.getSummary().getUtilization()
        then:
        result != null
    }

    private createItem(int bi, int w, int h, int d, int n) {
        IntStream.range(0, n).forEach({ i ->
            service
                    .addItem()
                    .setExternID(bi+"-" + i)
                    .setWidth(w)
                    .setHeight(h)
                    .setLength(d)
                    .setWeight(1)
                    .setStackingWeightLimit(1000)
        })
    }


}
