package xf.xflp.base.container

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.constraints.AxleLoadParameter
import xf.xflp.base.position.PositionService

class ContainerAxleLoadExtendedSpec extends Specification {

    // When no axle load parameter is set, all positions should be valid.
    def "no axle load parameter - all positions valid"() {
        def con = Helper.getAddSpaceContainer(1,10,1)
        def i1 = Helper.getItem(1, 5, 1, 1000, 10, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)
        def found = Helper.findCand(pList, 0, 0, 0)

        then:
        found != null
    }

    // When axle distance is 0, the check should be skipped and the position should be valid.
    def "axle distance is zero - check is skipped"() {
        def con = Helper.getAddSpaceContainer(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                100, 100, 0
        ))
        def i1 = Helper.getItem(1, 5, 1, 1000, 10, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)
        def found = Helper.findCand(pList, 0, 0, 0)

        then:
        found != null
    }

    // Load is placed exactly at the limit of the first axle, which should still be valid.
    def "axle load exactly at first axle limit"() {
        def con = Helper.getAddSpaceContainer(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                500, 500, 10
        ))
        def i1 = Helper.getItem(1, 10, 1, 1000, 10, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i1)
        def found = Helper.findCand(pList, 0, 0, 0)

        then:
        found != null
    }

    // Axle load check with AddRemoveContainer should also work correctly.
    def "axle load with AddRemoveContainer - valid"() {
        def con = Helper.getAddSpaceContainer2(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                1000, 2000, 8
        ))
        def i1 = Helper.getItem(1, 1, 1, 300, 2, 0)
        Helper.add(con, i1, 0, 0, 0)
        def i2 = Helper.getItem(1, 1, 1, 300, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i2)
        def found = Helper.findCand(pList, 0, 1, 0)

        then:
        found != null
    }

    // Axle load with AddRemoveContainer where second axle would be overloaded.
    def "axle load with AddRemoveContainer - invalid"() {
        def con = Helper.getAddSpaceContainer2(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                1000, 2000, 8
        ))
        def i1 = Helper.getItem(1, 1, 1, 600, 2, 0)
        Helper.add(con, i1, 0, 0, 0)
        def i2 = Helper.getItem(1, 1, 1, 300, 1, 0)
        Helper.add(con, i2, 0, 1, 0)
        def i3 = Helper.getItem(1, 2, 1, 300, 1, 0)
        Helper.add(con, i3, 0, 2, 0)
        def i4 = Helper.getItem(1, 6, 1, 1300, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i4)
        def found = Helper.findCand(pList, 0, 4, 0)

        then:
        found == null
    }

    // Two items are loaded successfully, but the third item would overload the second axle and must be rejected.
    def "axle load exceeded by third item on AddContainer"() {
        def con = Helper.getAddSpaceContainer(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                500, 500, 10
        ))
        def i1 = Helper.getItem(1, 1, 1, 300, 10, 0)
        def i2 = Helper.getItem(1, 1, 1, 300, 10, 0)
        def i3 = Helper.getItem(1, 1, 1, 500, 10, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)

        when:
        def pList = PositionService.findPositionCandidates(con, i3)
        def found = Helper.findCand(pList, 0, 2, 0)

        then:
        found == null
    }
}
