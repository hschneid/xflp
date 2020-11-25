package benchmarks

import spock.lang.Ignore
import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType

import java.util.stream.IntStream

class BigNumberSpec extends Specification {

    def service = new XFLP()
    def random = new Random(1234)


    def "1000 boxes"() {
        service.addContainer().setWidth(41).setLength(20).setHeight(7).setMaxWeight(10000)
        service.setTypeOfOptimization(XFLPOptType.SINGLE_CONTAINER_OPTIMIZER)

        long boxVol = 0
        for (int i = 0; i < 1000; i++) {
            int w = random.nextInt(4) + 1
            int l = random.nextInt(2) + 1
            int h = random.nextInt(2) + 1
            createItem(i, l, w, h)

            boxVol += w * l * h
        }

        when:
        service.executeLoadPlanning()
        def result = service.getReport()
        println result.getSummary().getUtilization()
        then:

        // Vol of boxes <16000
        // Vol of container 15000
        result != null
    }

    private createItem(int bi, int w, int h, int d) {
        service
                .addItem()
                .setExternID(bi+"")
                .setWidth(w)
                .setHeight(h)
                .setLength(d)
                .setWeight(1)
                .setStackingWeightLimit(1000)
    }


}
