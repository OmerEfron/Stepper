package Stepper.DataDefinitions.api;

public abstract class DataDefinitionAbstractClass implements DataDefinitionInterface{

    String name;
    boolean isUserFriendly;
    Class<?> type;

    public DataDefinitionAbstractClass(String name, Boolean isUserFriendly, Class<?> type){
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
}
