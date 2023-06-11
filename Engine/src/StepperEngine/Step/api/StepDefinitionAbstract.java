package StepperEngine.Step.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * The base class of the various steps
 */
public abstract class StepDefinitionAbstract implements StepDefinition, Serializable {
    private final String name;
    private final boolean isReadOnly;

    private final List<DataDefinitionsDeclaration> inputs = new ArrayList<>();
    private final List<DataDefinitionsDeclaration> outputs = new ArrayList<>();

    private final Map<String,DataDefinitionsDeclaration> inputsMap = new HashMap<>();
    private final Map<String,DataDefinitionsDeclaration> outputsMap = new HashMap<>();
    private final Map<String, DataDefinitionsDeclaration> dataMap = new HashMap<>();

    protected StepDefinitionAbstract(String name, boolean isReadOnly){
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

    @Override
    public void addInput(DataDefinitionsDeclaration newInput){
        inputs.add(newInput);
        inputsMap.put(newInput.getAliasName(), newInput);
        dataMap.put(newInput.getAliasName(), newInput);
    }

    @Override
    public void addOutput(DataDefinitionsDeclaration newOutput)
    {
        outputs.add(newOutput);
        outputsMap.put(newOutput.getAliasName(), newOutput);
        dataMap.put(newOutput.getAliasName(), newOutput);
    }

    @Override
    public DataDefinitionsDeclaration getData(String name) {
        return dataMap.get(name);
    }
}
