package xf.xflp.base.container

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.constraints.AxleLoadParameter
import xf.xflp.base.position.PositionService

class ContainerAxleLoadSpec extends Specification {

    def "check axle load - valid"() {
        def con = Helper.getAddSpaceContainer(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                1000, 2000, 8
        ))
        def i1 = Helper.getItem(1, 1, 1, 300    , 2, 0)
        Helper.add(con, i1, 0,0,0)
        def i2 = Helper.getItem(1, 1, 1, 300    , 1, 0)
        Helper.add(con, i2, 0,1,0)
        def i3 = Helper.getItem(1, 4, 1, 600    , 1, 0)
        Helper.add(con, i3, 0,2,0)
        def i4 = Helper.getItem(1, 4, 1, 1300, 1, 0)

        when:
        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 6, 0)

        then:
        pos4 != null
    }

    def "check axle load - valid, but only because of centered load check"() {
        def con = Helper.getAddSpaceContainer(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                800, 800, 8
        ))
        def i1 = Helper.getItem(1, 1, 1, 100    , 2, 0)
        Helper.add(con, i1, 0,0,0)
        def i2 = Helper.getItem(1, 1, 1, 900    , 1, 0)
        Helper.add(con, i2, 0,1,0)
        def i4 = Helper.getItem(1, 1, 1, 100, 1, 0)

        when:
        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 2, 0)

        then:
        pos4 != null
    }

    def "check axle load - invalid"() {
        def con = Helper.getAddSpaceContainer(1,10,1)
        con.parameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
                1000, 2000, 8
        ))
        def i1 = Helper.getItem(1, 1, 1, 600    , 2, 0)
        Helper.add(con, i1, 0,0,0)
        def i2 = Helper.getItem(1, 1, 1, 300    , 1, 0)
        Helper.add(con, i2, 0,1,0)
        def i3 = Helper.getItem(1, 2, 1, 300    , 1, 0)
        Helper.add(con, i3, 0,2,0)
        def i4 = Helper.getItem(1, 6, 1, 1300, 1, 0)

        when:
        def pList4 = PositionService.findPositionCandidates(con, i4)
        def pos4 = Helper.findCand(pList4, 0, 4, 0)

        then:
        pos4 == null
    }
}
