package StepperEngine.Flow.execute.context;

import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.StepStatus;
import javafx.util.Pair;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class StepExecutionContextClassNewImpl implements StepExecutionContext2{

    Map<DataDefinitionsDeclaration, Class<?>> dataTypes = new HashMap<>();// for fast access to each data's actual type
    Map<DataDefinitionsDeclaration, Object> dataValues = new HashMap<>();// stores the actual values of all data.
    Map<DataDefinitionsDeclaration, DataDefinitionsDeclaration> customMapping = new HashMap<>(); // maps the custom mapping of data
                                                                                                // if data a is custom mapped to data b, then
                                                                                                // we can get b by customMapping.get(a);
    Map<StepUsageDecleration, StepExecuteData> stepExecuteDataMap=new HashMap<>();//


    public StepExecutionContextClassNewImpl(FlowExecution flowExecution){
        setDataType(flowExecution);
        storeFreeInputs(flowExecution);
    }

    private void storeFreeInputs(FlowExecution flowExecution) {
        flowExecution.getFreeInputsValue().keySet().forEach(key->{
            storeValue(key, flowExecution.getFreeInputsValue().get(key));
        });
    }

    private void setDataType(FlowExecution flowExecution) {
        flowExecution.getFlowDefinition().getSteps()
                .forEach(step -> {
                    step.getStepDefinition().getInputs()
                            .forEach(input -> dataTypes.put(input, input.dataDefinition().getType()));
                    step.getStepDefinition().getOutputs()
                            .forEach(output -> dataTypes.put(output, output.dataDefinition().getType()));
                });
    }

    @Override
    public <T> T getDataValue(DataDefinitionsDeclaration data, Class<T> dataType) {
        Class<?> expectedDataType = dataTypes.get(data);
        if(expectedDataType != null){
            if(expectedDataType.isAssignableFrom(dataType)){ // types are matched
                Object aValue = dataValues.get(data);
                if(aValue == null){ // the data might be custom mapped from an earlier step.
                    DataDefinitionsDeclaration dd = customMapping.get(data);
                    aValue = dataValues.get(dd);
                }
                return dataType.cast(aValue);
            }
        }
        return null;
    }

    @Override
    public boolean storeValue(DataDefinitionsDeclaration data, Object value) {
        Class<?> expectedDataType = dataTypes.get(data);
        if(expectedDataType != null){
            if(expectedDataType.isAssignableFrom(value.getClass())){
                dataValues.put(data, value);
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateCustomMap(StepUsageDecleration currStep) {
        currStep.getDataMap().keySet().forEach((target) -> customMapping.put(target, currStep.getDataMap().get(target).getValue()));
    }

    @Override
    public Map<DataDefinitionsDeclaration, Object> getAllData() {
        return null;
    }

    /***
     * Update's formal outputs value of the flow
     */
    @Override
    public void addFormalOutput(FlowExecution flowExecution) {
        for (String name :flowExecution.getFlowDefinition().getFormalOuputs().keySet()){
            flowExecution.addOutput(name,dataValues.get(name));
        }
    }

    @Override
    public void addStepData(StepUsageDecleration step) {
        stepExecuteDataMap.put(step,new StepExecuteData(step));
    }



    @Override
    public Map<StepUsageDecleration,StepExecuteData> getStepsData() {
        return stepExecuteDataMap;
    }

    @Override
    public void addDataToStepData(StepUsageDecleration step, DataDefinitionsDeclaration data,boolean isOutput){
        stepExecuteDataMap.get(step).addStepData(data.getAliasName(), dataValues.get(data),isOutput);
    }

    @Override
    public void addLog(StepUsageDecleration step, String log) {
        stepExecuteDataMap.get(step).addLog(log);
    }

    @Override
    public void setInvokeSummery(StepUsageDecleration step, String summery) {
        stepExecuteDataMap.get(step).setInvokeSummery(summery);
    }

    @Override
    public void setStepStatus(StepUsageDecleration step, StepStatus stepStatus) {
        stepExecuteDataMap.get(step).setStepStatus(stepStatus);
    }

    @Override
    public void setTotalTime(StepUsageDecleration step, Duration totalTime) {
        stepExecuteDataMap.get(step).setTotalTime(totalTime);
    }

    @Override
    public void setStartStep(StepUsageDecleration step) {
        stepExecuteDataMap.get(step).setStartTime();
    }

    @Override
    public void setEndStep(StepUsageDecleration step) {
        stepExecuteDataMap.get(step).setEndTime();
    }

    @Override
    public StepStatus getStepStatus(StepUsageDecleration step) {
        return stepExecuteDataMap.get(step).getStepStatus();
    }
}
