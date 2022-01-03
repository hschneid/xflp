package benchmarks

import spock.lang.Specification
import xf.xflp.XFLP
import xf.xflp.opt.XFLPOptType
import xf.xflp.opt.construction.strategy.Strategy

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import java.util.stream.IntStream

class RealTruckPlanningSpec extends Specification {

    def service = new XFLP()

    def "Mega trailer"() {
        service.addContainer().setWidth(248).setLength(1360).setHeight(300).setMaxWeight(25000)
        service.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
        service.getParameter().setPreferredPackingStrategy(Strategy.WIDTH_PROPORTION)

        createItem(1, 120, 100, 100, 60,'G')
        createItem(2, 80, 100, 50, 70,'K')

        when:
        service.executeLoadPlanning()
        def result = service.getReport()

        then:
        result != null
        // This is theoretical max
        result.getSummary().getUtilization() > 0.92
    }

    def "business test 2"() {
        def xflp = new XFLP()
        xflp.setTypeOfOptimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
        xflp.getParameter().setPreferredPackingStrategy(Strategy.SAME_BASE)

        def tokens = Files.lines(Path.of("./src/test/resources/test2.data"))
                .map({f -> f.split("#")})
                .collect(Collectors.toList())
        tokens.stream()
                .filter({f -> f[0].equals("CON")})
                .forEach({t ->
                    xflp
                            .addContainer()
                            .setContainerType(t[1])
                            .setWidth(Integer.parseInt(t[2]))
                            .setLength(Integer.parseInt(t[3]))
                            .setHeight(Integer.parseInt(t[4]))
                            .setMaxWeight(Float.parseFloat(t[5]))
                })
        tokens.stream()
                .filter({f -> f[0].equals("ITM")})
                .sorted {i2, i1 -> toString(i1) <=> toString(i2)}
                .forEach({t ->
                    xflp
                            .addItem()
                            .setExternID(t[1])
                            .setLength(Integer.parseInt(t[2]))
                            .setWidth(Integer.parseInt(t[3]))
                            .setHeight(Integer.parseInt(t[4]))
                            .setWeight(Float.parseFloat(t[5]))
                            .setStackingWeightLimit(Float.MAX_VALUE)
                            .setStackingGroup(t[7])
                            .setAllowedStackingGroups(String.join(",", t[8]))
                            .setNbrOfAllowedStackedItems(1)
                })

        when:
        xflp.executeLoadPlanning()
        def result = xflp.getReport()
        then:
        result != null
    }

    private createItem(int bi, int l, int w, int h , int n, String stackGroup) {
        IntStream.range(0, n).forEach({ i ->
            service
                    .addItem()
                    .setExternID(bi+"-" + i)
                    .setWidth(w)
                    .setHeight(h)
                    .setLength(l)
                    .setWeight(1)
                    .setStackingWeightLimit(1000)
                    .setStackingGroup(stackGroup)
                    .setAllowedStackingGroups(stackGroup)
        })
    }

    String toString(String[] t) {
        String length = (Integer.parseInt(t[2]) < Integer.parseInt(t[3])) ? t[3] : t[2]
        String width = (Integer.parseInt(t[2]) < Integer.parseInt(t[3])) ? t[2] : t[3]
        int l = Integer.parseInt(length)
        int w = Integer.parseInt(width)

        String s = String.format("%04d/%04d/%04d", l, w, Integer.parseInt(t[4]))
        return s
    }

}
