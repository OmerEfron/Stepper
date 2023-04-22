package Stepper.Step.api;

import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;
import Stepper.DataDefinitions.api.DataDefinitionInterface;

public interface DataDefinitionsDeclaration {
    String getName();
    String getAliasName();

    String userString();

    DataNecessity necessity();

    DataDefinitionInterface dataDefinition();

    boolean equals(Object obj);

    void setAliasName(String alias);
}
