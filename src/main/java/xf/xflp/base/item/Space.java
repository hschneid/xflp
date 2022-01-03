package xf.xflp.base.item;

public class Space {

    public final int l;
    public final int w;
    public final int h;

    private Space(int l, int w, int h) {
        this.l = l;
        this.w = w;
        this.h = h;
    }

    public static Space of(int l, int w, int h) {
        return new Space(l, w, h);
    }

    @Override
    public String toString() {
        return "Space{" +
                "l=" + l +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}
