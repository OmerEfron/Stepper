package StepperEngine.Flow.execute.context;

import StepperEngine.DataDefinitions.api.DataDefinition;
import StepperEngine.Flow.api.FlowDefinition;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.StepStatus;

import java.time.Duration;
import java.util.*;

public class StepExecutionContextClass implements StepExecutionContext {

    Map<String, DataDefinition> dataTypes = new HashMap<>();
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
        FlowDefinition flow= flowExecution.getFlowDefinition();
        for (StepUsageDecleration currStep : flow.getSteps()) {
            for (DataDefinitionsDeclaration dd : currStep.getStepDefinition().getInputs()) {
                dataTypes.put(dd.getAliasName(), dd.dataDefinition());
            }
            for (DataDefinitionsDeclaration dd : currStep.getStepDefinition().getOutputs()) {
                dataTypes.put(dd.getAliasName(), dd.dataDefinition());
            }
        }
    }

    public void updateCustomMap(StepUsageDecleration currStep) {
        if (!currStep.getDataMap().isEmpty()) {
            currStep.getDataMap().forEach((s, stringStringPair) -> customMapping.put(s, stringStringPair.getValue()));
        }
    }


    @Override
    public <T> T getOutput(String dataName, Class<T> exceptedDataType) {
        DataDefinition theExeptedDataType = dataTypes.get(dataName);

        if (exceptedDataType.isAssignableFrom(theExeptedDataType.getType())) {
            Object aValue = dataValues.get(dataName);
            return exceptedDataType.cast(aValue);
        }
        return null;

    }

    @Override
    public <T> T getDataValue(String dataName, Class<T> exceptedDataType) {
        DataDefinition theExeptedDataType = dataTypes.get(dataName);

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
        DataDefinition theExeptedDataType = dataTypes.get(dataName);
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
    public void addStepData(StepUsageDecleration step) {
        stepExecuteDataMap.put(step.getStepFinalName(),new StepExecuteData(step));
    }

    @Override
    public void addLog(String stepName, String log) {
        stepExecuteDataMap.get(stepName).addLog(log);
    }

    @Override
    public void setInvokeSummery(String stepName, String summery) {
        stepExecuteDataMap.get(stepName).setInvokeSummery(summery);
    }

    @Override
    public void setStepStatus(String stepName, StepStatus stepStatus) {
        stepExecuteDataMap.get(stepName).setStepStatus(stepStatus);
    }

    @Override
    public void setTotalTime(String stepName, Duration totalTime) {
        stepExecuteDataMap.get(stepName).setTotalTime(totalTime);
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
