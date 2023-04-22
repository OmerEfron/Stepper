package Stepper.Flow.execute.context;

import Stepper.DataDefinitions.api.DataDefinitionInterface;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Step.api.DataDefinitionsDeclaration;

import java.util.HashMap;
import java.util.Map;

public class StepExecutionContextClass implements StepExecutionContext{

    Map<String, DataDefinitionInterface> dataTypes = new HashMap<>();
    Map<String, Object> inputs = new HashMap<>();

    Map<String, Object> outputs = new HashMap<>();

    public StepExecutionContextClass(FlowDefinitionInterface flow){
        getDataTypes(flow);

    }

    private void getDataTypes(FlowDefinitionInterface flow){
        StepUsageDeclerationInterface curr;
        for(int i = 0; i < flow.getSteps().size();i++){
            curr = flow.getSteps().get(i);
            for(DataDefinitionsDeclaration dd: curr.getStepDefinition().getInputs()){
                dataTypes.put(dd.getName(), dd.dataDefinition());
            }
            for(DataDefinitionsDeclaration dd: curr.getStepDefinition().getOutputs()){
                dataTypes.put(dd.getName(), dd.dataDefinition());
            }
        }
    }

    public void addOutput(String name, Object val){
        outputs.put(name, val);
    }

    @Override
    public <T> T getOutput(String dataName, Class<T> exceptedDataType) {
        DataDefinitionInterface theExeptedDataType = dataTypes.get(dataName);

        if(exceptedDataType.isAssignableFrom(theExeptedDataType.getType())){
            Object aValue = outputs.get(dataName);
            return exceptedDataType.cast(aValue);
        }
        return null;

    }

    @Override
    public <T> T getDataValue(String dataName, Class<T> exceptedDataType) {
        DataDefinitionInterface theExeptedDataType = dataTypes.get(dataName);

        if(exceptedDataType.isAssignableFrom(theExeptedDataType.getType())){
            Object aValue = inputs.get(dataName);
            return exceptedDataType.cast(aValue);
        }
        return null;
    }


    @Override
    public boolean storeValue(String dataName, Object value) {
        DataDefinitionInterface theExeptedDataType = dataTypes.get(dataName);
        if (theExeptedDataType.getType().isAssignableFrom(value.getClass())) {
            inputs.put(dataName, value);
            return true;
        }
        return false;
    }
}
