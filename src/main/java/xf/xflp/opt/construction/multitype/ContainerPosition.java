package xf.xflp.opt.construction.multitype;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Position;

public class ContainerPosition {

    private final Container container;
    private final Position position;

    public ContainerPosition(Container container, Position position) {
        this.container = container;
        this.position = position;
    }

    public Container getContainer() {
        return container;
    }

    public Position getPosition() {
        return position;
    }
}
