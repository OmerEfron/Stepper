package Stepper.Step.api;

import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;
import Stepper.Flow.execute.context.StepExecutionContext;

import java.util.List;

public interface StepDefinitionInterface {
    StepStatus invoke(StepExecutionContext context);
    String getName();
    Boolean isReadOnly();
    void addInput(DataDefinitionsDeclaration newInput);
    void addOutput(DataDefinitionsDeclaration newOutput);

    List<DataDefinitionsDeclaration> getInputs();

    List<DataDefinitionsDeclaration> getOutputs();
}
