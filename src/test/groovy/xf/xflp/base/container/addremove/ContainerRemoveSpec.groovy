package xf.xflp.base.container.addremove

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.container.AddRemoveContainer
import xf.xflp.base.container.Container
import xf.xflp.base.item.Item
import xf.xflp.base.item.Position
import xf.xflp.base.item.Space
import xf.xflp.base.position.PositionService

class ContainerRemoveSpec extends Specification {

    def rand = new Random(1234)

    def "remove an item - a valid item"() {
        Container con = Helper.getAddSpaceContainer2(3,3,2, Integer.MAX_VALUE)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 1, 1, 0)
        Helper.add(con, i4, 2, 1, 0)

        when:
        con.remove(i3)
        then:
        Helper.findCand(PositionService.findPositionCandidates(con, i3), 1,1,0) != null
        con.getLoadedVolume() == 7
        con.getItems().size() == 3
    }

    def "remove an item - create new spaces at removed item pos"() {
        Container con = Helper.getAddSpaceContainer2(3,3,2, Integer.MAX_VALUE)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i21 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i22 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i5 = Helper.getItem(2, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i21, 0, 1, 0)
        Helper.add(con, i22, 0, 2, 0)
        Helper.add(con, i3, 1, 1, 0)
        Helper.add(con, i4, 2, 1, 0)
        Helper.add(con, i5, 0, 2, 1)

        when:
        con.remove(i3)
        then:
        Helper.findCand(PositionService.findPositionCandidates(con, i3), 1,1,0) != null
        con.getLoadedVolume() == 9
        con.getItems().size() == 5
        ((AddRemoveContainer)con).spacePositions.find({ p -> comp(p.key, 1, 1, 0)}).value.size() == 2
    }

    def "remove an item - check existing spaces"() {
        Container con = Helper.getAddSpaceContainer2(3,3,2, Integer.MAX_VALUE)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i21 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i22 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i5 = Helper.getItem(2, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i21, 0, 1, 0)
        Helper.add(con, i22, 0, 2, 0)
        Helper.add(con, i3, 1, 1, 0)
        Helper.add(con, i4, 2, 1, 0)
        Helper.add(con, i5, 0, 2, 1)

        when:
        con.remove(i3)
        con.remove(i5)
        then:
        Helper.findCand(PositionService.findPositionCandidates(con, i3), 1,1,0) != null
        con.getLoadedVolume() == 7
        con.getItems().size() == 4
        ((AddRemoveContainer)con).spacePositions.find({ p -> comp(p.key, 1, 1, 0)}).value.size() == 1
        comp(((AddRemoveContainer)con).spacePositions.find({ p -> comp(p.key, 1, 1, 0)}).value[0], 1,2,2)
    }

    def "remove an item - an invalid item"() {
        Container con = Helper.getAddSpaceContainer2(3,3,2, Integer.MAX_VALUE)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 2, 1, 1, 111, 0)

        def i5 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i6 = Helper.getItem(1, 3, 1, 1, 111, 0)
        def i7 = Helper.getItem(2, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 1, 1, 0)
        Helper.add(con, i4, 2, 1, 0)
        Helper.add(con, i5, 0, 0, 1)
        Helper.add(con, i6, 2, 0, 1)
        Helper.add(con, i7, 0, 2, 1)

        when:
        con.remove(i3)
        then:
        Helper.findCand(PositionService.findPositionCandidates(con, i3), 1,1,0) != null
        con.getLoadedVolume() == 16
        con.getItems().size() == 6
    }

    def "remove everything one by one"() {
        Container con = Helper.getAddSpaceContainer2(3,3,2, Integer.MAX_VALUE)
        def i1 = Helper.getItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getItem(2, 1, 1, 1, 111, 0)
        def i3 = Helper.getItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getItem(1, 1, 1, 1, 111, 0)
        def i5 = Helper.getItem(1, 1, 1, 1, 111, 0)

        def i6 = Helper.getItem(2, 2, 1, 1, 111, 0)
        def i7 = Helper.getItem(1, 3, 1, 1, 111, 0)
        def i8 = Helper.getItem(2, 1, 1, 1, 111, 0)

        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 2, 1, 0)
        Helper.add(con, i4, 0, 2, 0)
        Helper.add(con, i5, 1, 2, 0)
        Helper.add(con, i6, 0, 0, 1)
        Helper.add(con, i7, 2, 0, 1)
        Helper.add(con, i8, 0, 2, 1)

        when:
        con.remove(i1)
        con.remove(i2)
        con.remove(i3)
        con.remove(i4)
        con.remove(i5)
        con.remove(i6)
        con.remove(i7)
        con.remove(i8)
        then:
        con.getLoadedWeight() == 0
        Helper.findCand(PositionService.findPositionCandidates(con, i1), 0,0,0) != null
        con.getLoadedVolume() == 0
        con.getItems().size() == 0
        ((AddRemoveContainer)con).itemList.size() == 0
        ((AddRemoveContainer)con).activePosList.size() == 1
        ((AddRemoveContainer)con).inactivePosList.size() == 0
        ((AddRemoveContainer)con).spacePositions.size() == 1
        comp(((AddRemoveContainer)con).spacePositions.values()[0][0], 3, 3, 2)
        !((AddRemoveContainer) con).getZGraph().lowerList.any { o -> o != null }
        !((AddRemoveContainer)con).getZGraph().upperList.any { o -> o != null }
    }

    def "randomly add and remove items"() {
        Container con = Helper.getAddSpaceContainer2(10,10,4, Integer.MAX_VALUE)

        var unloadedItems = [] as List<Item>
        var loadedItems = [] as List<Item>
        for (i in 0..< 10) {
            unloadedItems.add(Helper.getItem(2, 1, 1, 1, 111, 0))
            unloadedItems.add(Helper.getItem(1, 1, 1, 1, 111, 0))
            unloadedItems.add(Helper.getItem(1, 1, 2, 1, 111, 0))
        }
        unloadedItems.shuffle(rand)

        when:

        // Load as much as possible
        for (Item i : unloadedItems) {
            var posList = PositionService.findPositionCandidates(con, i)
            if(posList.size() > 0) {
                var pos = posList[0]
                con.add(i, pos.position(), pos.rotated)
                loadedItems.add(i)
            }
        }
        unloadedItems.removeAll(loadedItems)

        for (i in 0..<100) {
            // Remove 1 loaded item
            var unloadItemIdx = rand.nextInt(loadedItems.size())
            var unloadItem = loadedItems[unloadItemIdx]
            con.remove(unloadItem)
            
            // Try to add at least 1 item
            for (Item loadItem : unloadedItems) {
                var posList = PositionService.findPositionCandidates(con, loadItem)
                if(posList.size() > 0) {
                    var pos = posList[0]
                    con.add(loadItem, pos.position(), pos.rotated)
                    unloadedItems.remove(loadItem)
                    loadedItems.add(loadItem)
                    break;
                }
            }

            loadedItems.remove(unloadItem)
            unloadedItems.add(unloadItem)
        }

        then:
        true
    }

    boolean comp(Position pos, int x, int y, int z) {
        return pos.x() == x && pos.y() == y && pos.z() == z
    }

    boolean comp(Space space, int w, int l, int h) {
        return space.w() == w && space.l() == l && space.h() == h
    }
}
