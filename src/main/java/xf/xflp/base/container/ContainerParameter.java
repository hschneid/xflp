package xf.xflp.base.container;

public interface ContainerParameter {

    void add(ParameterType type, Object value);

    Object get(ParameterType type);
}
