package xf.xflp.base.problem;

public enum RotationType {

    FIX(0),
    SPINNABLE(1);

    public int getRotationType() {
        return rotationType;
    }

    private int rotationType;

    RotationType(int rotationType) {
        this.rotationType = rotationType;
    }
}
