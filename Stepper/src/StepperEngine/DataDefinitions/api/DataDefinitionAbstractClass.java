package StepperEngine.DataDefinitions.api;

import java.util.Objects;

public abstract class DataDefinitionAbstractClass implements DataDefinitionInterface {

    String name;
    boolean isUserFriendly;
    Class<?> type;

    public DataDefinitionAbstractClass(String name, Boolean isUserFriendly, Class<?> type) {
        this.name = name;
        this.isUserFriendly = isUserFriendly;
        this.type = type;
    }

    @Override
    public boolean isUserFriendly() {
        return isUserFriendly;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !DataDefinitionAbstractClass.class.isAssignableFrom(o.getClass())) return false;
        DataDefinitionAbstractClass other = (DataDefinitionAbstractClass)o;
        return this.type.isAssignableFrom(other.type) || other.type.isAssignableFrom(this.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
