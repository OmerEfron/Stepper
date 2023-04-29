package Stepper.DataDefinitions.api;

public interface DataDefinitionInterface {

    boolean isUserFriendly();
    String getName();

    Class<?> getType();
    boolean equals(Object o);

}
