package xf.xflp.opt.ga;

public class Individual {

    private ItemParameter[] parameter;
    private float fitness;

    public ItemParameter[] getParameter() {
        return parameter;
    }

    public void setParameter(ItemParameter[] parameter) {
        this.parameter = parameter;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
}
