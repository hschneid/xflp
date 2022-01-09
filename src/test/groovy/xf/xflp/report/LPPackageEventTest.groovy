package xf.xflp.report

import spock.lang.Specification

class LPPackageEventTest extends Specification {

    def "test get and set"() {
        def e = new LPPackageEvent()
        when:
        e.setId("BLUB");
        e.setX(123);
        e.setY(456);
        e.setZ(789);
        e.setWidth(135);
        e.setLength(246);
        e.setHeight(357);
        e.setStackingGrp(88);
        e.setWeight(99);
        e.setWeightLimit(66);
        e.setInvalid(true);
        e.setType(LoadType.LOAD);
        e.setUsedVolumeInContainer(111);
        e.setUsedWeightInContainer(222);
        e.setNbrStacksInContainer(333);

        then:
        e.getId() == "BLUB"
        e.getX() == 123
        e.getY() == 456
        e.getZ() == 789
        e.getWidth() == 135
        e.getLength() == 246
        e.getHeight() == 357
        e.getStackingGrp() == 88
        e.getWeight() == 99
        e.getWeightLimit() == 66
        e.isInvalid()
        e.getType() == LoadType.LOAD
        e.getUsedVolumeInContainer() == 111
        e.getUsedWeightInContainer() == 222
        e.getNbrStacksInContainer() == 333
    }
}
