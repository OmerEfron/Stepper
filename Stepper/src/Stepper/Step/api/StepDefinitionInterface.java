package Stepper.Step.api;

import Stepper.Flow.execute.context.StepExecutionContext;

import java.util.List;
import java.util.Map;

public interface StepDefinitionInterface {
    StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias, String stepName);
    String getName();
    Boolean isReadOnly();
    void addInput(DataDefinitionsDeclaration newInput);
    void addOutput(DataDefinitionsDeclaration newOutput);

    List<DataDefinitionsDeclaration> getInputs();

    List<DataDefinitionsDeclaration> getOutputs();
}
