package xf.xflp.base.container

import helper.Helper
import spock.lang.Specification
import xf.xflp.base.item.PlacedItem
import xf.xflp.base.item.Position
import xf.xflp.base.item.Space
import xf.xflp.base.position.PositionService

class ContainerRemoveSpec extends Specification {

    def rand = new Random(1234)

    def "remove an item - a valid item"() {
        Container con = Helper.getAddSpaceContainer2(3,3,2, Integer.MAX_VALUE)
        def i1 = Helper.getPlacedItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)

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
        def i1 = Helper.getPlacedItem(3, 1, 1, 1, 111, 0)
        def i21 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i22 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i5 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)

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
        def i1 = Helper.getPlacedItem(3, 1, 1, 1, 111, 0)
        def i21 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i22 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i5 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)

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
        def i1 = Helper.getPlacedItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)

        def i5 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        def i6 = Helper.getPlacedItem(1, 3, 1, 1, 111, 0)
        def i7 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)

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
        def i1 = Helper.getPlacedItem(3, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 111, 0)
        def i4 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i5 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)

        def i6 = Helper.getPlacedItem(2, 2, 1, 1, 111, 0)
        def i7 = Helper.getPlacedItem(1, 3, 1, 1, 111, 0)
        def i8 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)

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

        var unloadedItems = [] as List<PlacedItem>
        var loadedItems = [] as List<PlacedItem>
        for (i in 0..< 10) {
            unloadedItems.add(Helper.getPlacedItem(2, 1, 1, 1, 111, 0))
            unloadedItems.add(Helper.getPlacedItem(1, 1, 1, 1, 111, 0))
            unloadedItems.add(Helper.getPlacedItem(1, 1, 2, 1, 111, 0))
        }
        unloadedItems.shuffle(rand)

        when:

        // Load as much as possible
        for (PlacedItem i : unloadedItems) {
            var posList = PositionService.findPositionCandidates(con, i)
            if(posList.size() > 0) {
                var pos = posList[0]
                con.add(i, pos.position(), pos.rotated)
                loadedItems.add(i)
            }
        }
        unloadedItems.removeAll(loadedItems)

        for (i in 0..<100) {
            println 'AAA '+i
            // Remove 1 loaded item
            var unloadItemIdx = rand.nextInt(loadedItems.size())
            var unloadItem = loadedItems[unloadItemIdx]
            con.remove(unloadItem)
            
            // Try to add at least 1 item
            for (PlacedItem loadItem : unloadedItems) {
                var posList = PositionService.findPositionCandidates(con, loadItem)
                if(posList.size() > 0) {
                    var pos = posList[0]
                    con.add(loadItem, pos.position(), pos.rotated)
                    unloadedItems.remove(loadItem)
                    loadedItems.add(loadItem)
                    break
                }
            }

            loadedItems.remove(unloadItem)
            unloadedItems.add(unloadItem)
        }

        then:
        true
    }

    def "add and remove items from container"() {
        def con = Helper.getAddSpaceContainer2(2,2,5)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)
        def i3 = Helper.getPlacedItem(1, 1, 1, 1, 100, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 0, 1)
        Helper.add(con, i3, 0, 0, 2)

        def apList1 = new ArrayList<>(con.getActivePositions())
        con.remove(i2)
        def apList2 = new ArrayList<>(con.getActivePositions())
        con.remove(i1)
        def apList3 = new ArrayList<>(con.getActivePositions())
        con.remove(i3)
        def apList4 = new ArrayList<>(con.getActivePositions())

        then:
        Helper.findPos(apList1, 1, 0, 0) != null
        Helper.findPos(apList1, 0, 1, 0) != null
        Helper.findPos(apList1, 1, 0, 1) != null
        Helper.findPos(apList1, 0, 1, 1) != null
        Helper.findPos(apList1, 1, 0, 2) != null
        Helper.findPos(apList1, 0, 1, 2) != null
        Helper.findPos(apList1, 0, 0, 3) != null
        Helper.findPos(apList2, 0, 0, 1) != null
        Helper.findPos(apList3, 0, 0, 0) != null
        con.getActivePositions().size() == 1
        Helper.findPos(apList4, 0, 0, 0) != null
    }

    def "test covered positions (X axis)"(){
        def con = Helper.getAddSpaceContainer2(3,3,3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 1, 0)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 1, 0)
        def pos2 = Helper.findPos(con.inactivePosList, 1, 1, 0)
        def pos3 = Helper.findPos(con.coveredPosList, 1, 1, 0)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Y axis)"(){
        def con = Helper.getAddSpaceContainer2(3,3,3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 1, 0, 0)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 1, 0)
        def pos2 = Helper.findPos(con.inactivePosList, 1, 1, 0)
        def pos3 = Helper.findPos(con.coveredPosList, 1, 1, 0)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-X axis)"(){
        def con = Helper.getAddSpaceContainer2(3,3,3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)
        Helper.add(con, i3, 0, 0, 1)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 1, 0, 1)
        def pos2 = Helper.findPos(con.inactivePosList, 1, 0, 1)
        def pos3 = Helper.findPos(con.coveredPosList, 1, 0, 1)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test covered positions (Z-Y axis)"(){
        def con = Helper.getAddSpaceContainer2(3,3,3)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 1, 0)
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 1, 0)

        when:
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 0, 1, 0)
        Helper.add(con, i3, 0, 0, 1)

        // Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
        def pos1 = Helper.findPos(con.getActivePositions(), 0, 1, 1)
        def pos2 = Helper.findPos(con.inactivePosList, 0, 1, 1)
        def pos3 = Helper.findPos(con.coveredPosList, 0, 1, 1)

        then:
        pos1 == null
        pos2 == null
        pos3 != null
    }

    def "test add and remove restores original state"() {
        def con = Helper.getAddSpaceContainer2(3,3,3)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getPlacedItem(1, 2, 2, 1, 10, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        when:
        def descBefore = con.getStateDescription()
        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        Helper.add(con, i3, 0, 0, 1)
        con.remove(i3)
        def descAfter = con.getStateDescription()

        then:
        def before = removeHistory(descBefore)
        def after = removeHistory(descAfter)
        before == after
    }

    private static String removeHistory(String desc) {
        return desc.replaceAll(/(?s) {2}history:\n( {4}- .*\n)*/, "")
    }

    def "test remove cleans up all internal data structures"() {
        def con = Helper.getAddSpaceContainer2(3,3,3)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)
        def i1 = Helper.getPlacedItem(1, 1, 1, 1, 10, 0)
        def i2 = Helper.getPlacedItem(1, 2, 2, 1, 10, 0)
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 1, 0, 0)

        def i3 = Helper.getPlacedItem(1, 2, 1, 1, 10, 0)
        Helper.add(con, i3, 0, 0, 1)
        def i3Index = i3.index

        when:
        con.remove(i3)

        then:
        // Item darf nicht mehr in der itemList sein
        con.getItems().get(i3Index) == null

        // Item darf nicht mehr in der itemPositionMap sein
        !con.itemPositionMap.containsKey(i3)

        // Item-Index darf nicht mehr in xMap, yMap, zMap vorkommen
        !containsValue(con.xMap, i3Index)
        !containsValue(con.yMap, i3Index)
        !containsValue(con.zMap, i3Index)

        // Item darf nicht mehr im zGraph sein
        con.baseData.zGraph.getItemsAbove(i1).every { it != i3 }

        // Gewicht muss wieder dem Zustand vor dem Add entsprechen
        con.weight == i1.item.weight + i2.item.weight
    }

    def "test add-remove-add cycle with index reuse and findPositionCandidates"() {
        given: "Ein Container mit gestapelten Items"
        def con = Helper.getAddSpaceContainer2(10, 10, 4)
        con.parameter.add(ParameterType.GROUND_CONTACT_RULE, GroundContactRule.FREE)

        def i1 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)
        def i2 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def i3 = Helper.getPlacedItem(2, 1, 1, 1, 111, 0)
        def i4 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)

        // Füge Items hinzu, auch gestapelt
        Helper.add(con, i1, 0, 0, 0)
        Helper.add(con, i2, 2, 0, 0)
        Helper.add(con, i3, 0, 0, 1)  // auf i1
        Helper.add(con, i4, 2, 0, 1)  // auf i2

        when: "Entferne ein unteres Item, füge ein neues hinzu, suche Positionen"
        con.remove(i4)
        con.remove(i2)

        // Neues Item hinzufügen an die frei gewordene Position
        def i5 = Helper.getPlacedItem(1, 1, 2, 1, 111, 0)
        def posList = PositionService.findPositionCandidates(con, i5)
        def pos = posList.size() > 0 ? posList[0] : null
        if (pos != null) {
            con.add(i5, pos.position, pos.isRotated)
        }

        // Noch ein Item entfernen und dann Positionen suchen
        con.remove(i3)

        def i6 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def posList2 = PositionService.findPositionCandidates(con, i6)

        // Füge i6 hinzu und suche erneut
        if (posList2.size() > 0) {
            con.add(i6, posList2[0].position, posList2[0].isRotated)
        }

        def i7 = Helper.getPlacedItem(1, 1, 1, 1, 111, 0)
        def posList3 = PositionService.findPositionCandidates(con, i7)

        then: "Keine NPE - alle findPositionCandidates Aufrufe funktionieren korrekt"
        noExceptionThrown()
        posList2 != null
        posList3 != null
    }

    private static boolean containsValue(def lpListMap, int value) {
        for (def key : lpListMap.keySet()) {
            def list = lpListMap.get(key)
            if (list != null && list.contains(value)) return true
        }
        return false
    }

    boolean comp(Position pos, int x, int y, int z) {
        return pos.x() == x && pos.y() == y && pos.z() == z
    }

    boolean comp(Space space, int w, int l, int h) {
        return space.w() == w && space.l() == l && space.h() == h
    }
}
