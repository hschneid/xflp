package benchmarks

import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType

import java.util.stream.IntStream

class RatcliffSpec extends Specification {

    def service = new XFLP()

    def "BR1 - 1 test"() {
        service.addContainer().setWidth(223).setLength(587).setHeight(220).setMaxWeight(10000)
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_RANDOM_SEARCH)

        IntStream.range(0, 62).forEach({i -> service
                .addItem()
                .setExternID("1-"+i)
                .setWidth(42)
                .setHeight(26)
                .setLength(64)
                .setWeight(1)
                .setStackingWeightLimit(1000)
        })
        IntStream.range(0, 53).forEach({i -> service
                .addItem()
                .setExternID("2-"+i)
                .setWidth(58)
                .setHeight(52)
                .setLength(92)
                .setWeight(1)
                .setStackingWeightLimit(1000)
        })
        IntStream.range(0, 67).forEach({i -> service
                .addItem()
                .setExternID("3-"+i)
                .setWidth(85)
                .setHeight(21)
                .setLength(92)
                .setWeight(1)
                .setStackingWeightLimit(1000)
        })

        when:
        service.executeLoadPlanning()
        def result = service.getReport()
        // Opt sol is > 82%, curr XFLP is 82%
        println result.getSummary().getUtilization()
        then:
        result != null
    }

}
