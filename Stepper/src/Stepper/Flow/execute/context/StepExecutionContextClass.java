package Stepper.Flow.execute.context;

import Stepper.DataDefinitions.api.DataDefinitionInterface;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.StepData.StepExecuteData;
import Stepper.Step.api.DataDefinitionsDeclaration;
import Stepper.Step.api.StepStatus;

import java.time.Duration;
import java.util.*;

public class StepExecutionContextClass implements StepExecutionContext {

    Map<String, DataDefinitionInterface> dataTypes = new HashMap<>();
    Map<String, Object> dataValues = new HashMap<>();
    Map<String, String> customMapping = new HashMap<>();
    Map<String, StepExecuteData> stepExecuteDataMap=new HashMap<>();


    public StepExecutionContextClass(FlowExecution flow) {
        getDataTypes(flow);
    }

    private void getDataTypes(FlowExecution flowExecution) {
        updateDataTypes(flowExecution);
        storeFreeInputs(flowExecution.getFreeInputsValue());
    }

    private void storeFreeInputs(Map<String,Object> freeInputsValue){
        for(String dataname:freeInputsValue.keySet())
        {
            storeValue(dataname,freeInputsValue.get(dataname));
        }
    }

    private void updateDataTypes(FlowExecution flowExecution) {
        FlowDefinitionInterface flow= flowExecution.getFlowDefinition();
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

    @Override
    public void addFormalOutput(FlowExecution flowExecution) {
        for (String name :flowExecution.getFlowDefinition().getFormalOuputs().keySet()){
            flowExecution.addOutput(name,dataValues.get(name));
        }
    }

    @Override
    public void addStepData(StepUsageDeclerationInterface step) {
        stepExecuteDataMap.put(step.getStepFinalName(),new StepExecuteData(step));
    }

    @Override
    public void addLog(String stepName, String log) {

    }

    @Override
    public void setInvokeSummery(String stepName, String summery) {

    }

    @Override
    public void setStepStatus(String stepName, StepStatus stepStatus) {

    }

    @Override
    public void setTotalTime(String stepName, Duration totalTime) {

    }

    @Override
    public StepStatus getStepStatus(String stepName) {
        return stepExecuteDataMap.get(stepName).getStepStatus();
    }

    @Override
    public List<StepExecuteData> getStepsData() {
        return new ArrayList<>(stepExecuteDataMap.values());
    }
}
