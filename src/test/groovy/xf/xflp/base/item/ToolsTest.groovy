package xf.xflp.base.item

import helper.Helper
import spock.lang.Specification

class ToolsTest extends Specification {

    // Two items with full overlap should return a cut ratio of 1.0.
    def "getCutRatio - full overlap returns 1.0"() {
        def root = Helper.getItemAtPosition(0, 0, 2, 2)
        def cut = Helper.getItemAtPosition(0, 0, 2, 2)

        when:
        def ratio = Tools.getCutRatio(root, cut)

        then:
        Math.abs(ratio - 1.0f) < 0.001f
    }

    // Two items with half overlap in the x direction should return 0.5.
    def "getCutRatio - half overlap in x direction returns 0.5"() {
        def root = Helper.getItemAtPosition(0, 0, 2, 2)
        def cut = Helper.getItemAtPosition(1, 0, 2, 2)

        when:
        def ratio = Tools.getCutRatio(root, cut)

        then:
        Math.abs(ratio - 0.5f) < 0.001f
    }

    // Two items with quarter overlap should return 0.25.
    def "getCutRatio - quarter overlap returns 0.25"() {
        def root = Helper.getItemAtPosition(0, 0, 2, 2)
        def cut = Helper.getItemAtPosition(1, 1, 2, 2)

        when:
        def ratio = Tools.getCutRatio(root, cut)

        then:
        Math.abs(ratio - 0.25f) < 0.001f
    }

    // Overloaded getCutRatio with explicit coordinates should produce the same result.
    def "getCutRatio with explicit coordinates - half overlap"() {
        def cut = Helper.getItemAtPosition(1, 0, 2, 2)

        when:
        def ratio = Tools.getCutRatio(0, 0, 2, 2, cut)

        then:
        Math.abs(ratio - 0.5f) < 0.001f
    }
}






