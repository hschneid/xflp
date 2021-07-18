package xf.xflp.base.container;

public class DirectContainerParameter implements ContainerParameter {

    private float lifoImportance;
    private GroundContactRule groundContactRule;

    @Override
    public void add(ParameterType type, Object value) {
        switch (type) {
            case LIFO_IMPORTANCE:
                lifoImportance = (Float)value;
                break;
            case GROUND_CONTACT_RULE:
                groundContactRule = (GroundContactRule) value;
                break;
        }
    }

    @Override
    public Object get(ParameterType type) {
        switch (type) {
            case LIFO_IMPORTANCE:
                return lifoImportance;
            case GROUND_CONTACT_RULE:
                return groundContactRule;
        }

        return null;
    }
}
