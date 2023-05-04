package StepperEngine.Flow.execute;

import StepperEngine.Flow.api.FlowDefinition;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import javafx.util.Pair;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class FlowExecution {

    private final FlowDefinition flowDefinition;
    private final String id;
    private static Integer idCounter = 1;
    private Duration totalTime;
    private FlowStatus flowStatus;
    private String formattedStartTime;

    private boolean hasExecuted = false;
    private final Map<String, Object> freeInputsValue = new HashMap<>();
    private String uuidAsString;
    private final Map<String, Object> formalOutputs = new HashMap<>();
    private List<StepExecuteData> stepsData = new ArrayList<>();

    private final Set<DataDefinitionsDeclaration> freeInputs;
    private final Set<DataDefinitionsDeclaration> outputs;

    public FlowExecution(FlowDefinition flowDefinition) {
        this.flowDefinition = flowDefinition;
        this.id = idCounter.toString();
        idCounter++;
        freeInputs = flowDefinition.getFreeInputs();
        outputs = flowDefinition.getAllOutputs().values().stream()
                .map(Pair::getKey)
                .collect(Collectors.toSet());
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

    public FlowDefinition getFlowDefinition() {
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
        this.hasExecuted = true;
    }

    public boolean hasExecuted() {
        return hasExecuted;
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

    public Set<DataDefinitionsDeclaration> getOutputs() {
        return outputs;
    }


    public <T> T getOneOutput(String dataName, Class<T> exceptedDataType) {
        return formalOutputs.containsKey(dataName) ? exceptedDataType.cast(formalOutputs.get(dataName)) : null;
    }

    public List<StepExecuteData> getStepsData() {
        return stepsData;
    }

    public Set<DataDefinitionsDeclaration> getFreeInputs() {
        return freeInputs;
    }

    public <T> T getInputValue(String inputName, Class<T> exceptedDataType){
        return freeInputsValue.containsKey(inputName) ? exceptedDataType.cast(freeInputsValue.get(inputName)): null;
    }

    public Map<String, Object> getFormalOutputs() {
        return formalOutputs;
    }
}
