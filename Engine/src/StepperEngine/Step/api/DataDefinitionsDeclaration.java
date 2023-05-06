package StepperEngine.Step.api;

import StepperEngine.DataDefinitions.api.DataDefinition;

public interface DataDefinitionsDeclaration {
    String getName();
    String getAliasName();

    String userString();

    DataNecessity necessity();

    DataDefinition dataDefinition();

    boolean equals(Object obj);

    void setAliasName(String alias);
}
