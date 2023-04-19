package Stepper.DataDefinitions.impl;

import Stepper.DataDefinitions.api.DataDefinitionInterface;

public enum DataDefinitionRegistry implements DataDefinitionInterface {
    STRING(new StepperString()),
    DOUBLE(new StepperDouble()),
    FILE(new StepperFile()),
    LIST(new StepperList()),
    NUMBER(new StepperNumber()),
    MAPPING(new StepperMapping()),
    RELATION(new StepperRelation());

    DataDefinitionRegistry(DataDefinitionInterface dataDefinitionInterface){
        this.dataDefinitionInterface=dataDefinitionInterface;
    }
    private final DataDefinitionInterface dataDefinitionInterface;

    @Override
    public boolean isUserFriendly() {
        return dataDefinitionInterface.isUserFriendly();
    }

    @Override
    public String getName() {
        return dataDefinitionInterface.getName();
    }

    @Override
    public Class<?> getType() {
        return dataDefinitionInterface.getType();
    }
}
