package Stepper.Flow.execute;

import Stepper.DataDefinitions.api.DataDefinitionInterface;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.execute.StepData.StepExecuteData;

import java.time.Duration;
import java.util.*;

public class FlowExecution {

    private final FlowDefinitionInterface flowDefinition;
    private final String id;
    private static Integer idCounter = 1;
    private Duration totalTime;
    private FlowStatus flowStatus;
    private String formattedStartTime;
    private Map<String, Object> freeInputsValue = new HashMap<>();
    private String uuidAsString;
    private Map<String, Object> formalOutputs = new HashMap<>();
    private List<StepExecuteData> stepsData = new ArrayList<>();

    public FlowExecution(FlowDefinitionInterface flowDefinition) {
        this.flowDefinition = flowDefinition;
        this.id = idCounter.toString();
        idCounter++;
    }

    public void setStepsData(List<StepExecuteData> stepsData) {
        this.stepsData = stepsData;
    }

    public void addStepExecuteData(StepExecuteData stepExecuteData) {
        stepsData.add(stepExecuteData);
    }

    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    public void setFormattedStartTime(String formattedStartTime) {
        this.formattedStartTime = formattedStartTime;
    }


    public Duration getTotalTime() {
        return totalTime;
    }

    public FlowDefinitionInterface getFlowDefinition() {
        return flowDefinition;
    }

    public String getId() {
        return id;
    }

    public FlowStatus getFlowExecutionResult() {
        return flowStatus;
    }

    public void setFlowStatus(FlowStatus flowStatus) {
        this.flowStatus = flowStatus;
    }

    public void setTotalTime(Duration totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalTimeInFormat() {
        return String.format("%,d", totalTime.getNano() / 1_000_000);
    }

    public void createUUID() {
        UUID uuid = UUID.randomUUID();
        this.uuidAsString = uuid.toString();
    }

    public String getUUID() {
        return uuidAsString;
    }

    public void addFreeInput(String dataName, Object value) {
        freeInputsValue.put(dataName, value);
    }

    public Map<String, Object> getFreeInputsValue() {
        return freeInputsValue;
    }

    public void addOutput(String dataName, Object value) {
        formalOutputs.put(dataName, value);
    }

    public Map<String, Object> getOutput() {
        return formalOutputs;
    }


    public <T> T getOneOutput(String dataName, Class<T> exceptedDataType) {
        return formalOutputs.containsKey(dataName) ? exceptedDataType.cast(formalOutputs.get(dataName)) : null;
    }
}
