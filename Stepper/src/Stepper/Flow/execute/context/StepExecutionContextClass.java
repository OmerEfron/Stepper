package Stepper.Flow.execute.context;

import Stepper.DataDefinitions.api.DataDefinitionInterface;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Step.api.DataDefinitionsDeclaration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StepExecutionContextClass implements StepExecutionContext {

    Map<String, DataDefinitionInterface> dataTypes = new HashMap<>();
    Map<String, Object> dataValues = new HashMap<>();
    Map<String, String> customMapping = new HashMap<>();


    public StepExecutionContextClass(FlowDefinitionInterface flow) {
        getDataTypes(flow);
    }

    private void getDataTypes(FlowDefinitionInterface flow) {
        for (StepUsageDeclerationInterface currStep : flow.getSteps()) {
            for (DataDefinitionsDeclaration dd : currStep.getStepDefinition().getInputs()) {
                dataTypes.put(dd.getAliasName(), dd.dataDefinition());
            }
            for (DataDefinitionsDeclaration dd : currStep.getStepDefinition().getOutputs()) {
                dataTypes.put(dd.getAliasName(), dd.dataDefinition());
            }
        }

    }

    public void updateCustomMap(StepUsageDeclerationInterface currStep) {
        if (!currStep.getDataMap().isEmpty()) {
            currStep.getDataMap().forEach((s, stringStringPair) -> customMapping.put(s, stringStringPair.getValue()));
        }
    }


    @Override
    public <T> T getOutput(String dataName, Class<T> exceptedDataType) {
        DataDefinitionInterface theExeptedDataType = dataTypes.get(dataName);

        if (exceptedDataType.isAssignableFrom(theExeptedDataType.getType())) {
            Object aValue = dataValues.get(dataName);
            return exceptedDataType.cast(aValue);
        }
        return null;

    }

    @Override
    public <T> T getDataValue(String dataName, Class<T> exceptedDataType) {
        DataDefinitionInterface theExeptedDataType = dataTypes.get(dataName);

        if (exceptedDataType.isAssignableFrom(theExeptedDataType.getType())) {
            Object aValue = dataValues.get(dataName);
            if(aValue==null) {
                String name=customMapping.get(dataName);
                aValue= dataValues.get(name);
            }
            return exceptedDataType.cast(aValue);
        }
        return null;
    }


    @Override
    public boolean storeValue(String dataName, Object value) {
        DataDefinitionInterface theExeptedDataType = dataTypes.get(dataName);
        if (theExeptedDataType == null){
            theExeptedDataType=dataTypes.get(customMapping.get(dataName));
        }
        if (theExeptedDataType.getType().isAssignableFrom(value.getClass())) {
            dataValues.put(dataName, value);
            return true;
        }
        return false;

    }
}
