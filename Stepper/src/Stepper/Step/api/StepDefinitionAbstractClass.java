package Stepper.Step.api;

import java.util.ArrayList;
import java.util.List;

public abstract class StepDefinitionAbstractClass implements StepDefinitionInterface {
    private final String name;
    private final boolean isReadOnly;

    private final List<DataDefinitionsDeclaration> inputs = new ArrayList<>();
    private final List<DataDefinitionsDeclaration> outputs = new ArrayList<>();

    protected StepDefinitionAbstractClass(String name, boolean isReadOnly){
        this.name = name;
        this.isReadOnly = isReadOnly;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean isReadOnly() {
        return isReadOnly;
    }

    @Override
    public List<DataDefinitionsDeclaration> getInputs() {
        return inputs;
    }

    @Override
    public List<DataDefinitionsDeclaration> getOutputs() {
        return outputs;
    }

    protected void getInput(DataDefinitionsDeclaration dataDefDec){
        inputs.add(dataDefDec);
    }
    @Override
    public void addInput(DataDefinitionsDeclaration newInput){
        inputs.add(newInput);
    }

    @Override
    public void addOutput(DataDefinitionsDeclaration newOutput)
    {
        outputs.add(newOutput);
    }
}
