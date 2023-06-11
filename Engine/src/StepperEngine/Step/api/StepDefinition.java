package StepperEngine.Step.api;

import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.context.StepExecutionContext2;

import java.util.List;
import java.util.Map;

public interface StepDefinition {
    StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step);
    String getName();
    Boolean isReadOnly();
    void addInput(DataDefinitionsDeclaration newInput);
    void addOutput(DataDefinitionsDeclaration newOutput);

    List<DataDefinitionsDeclaration> getInputs();

    List<DataDefinitionsDeclaration> getOutputs();

    DataDefinitionsDeclaration getData(String name);
}
