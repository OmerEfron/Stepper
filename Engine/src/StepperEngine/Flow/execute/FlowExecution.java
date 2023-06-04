package StepperEngine.Flow.execute;

import StepperEngine.DataDefinitions.Enumeration.ZipEnumerator;
import StepperEngine.Flow.api.FlowDefinition;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import javafx.util.Pair;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/***
 * Saves information about execution of flow
 */
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
    private List<StepExecuteData> stepsData = new LinkedList<>();
    private Map<String,Object> allData=new HashMap<>();
    private final Set<DataDefinitionsDeclaration> freeInputs;
    private final Set<DataDefinitionsDeclaration> outputs;

    private final int numOfSteps;
    private int numOfStepsExecuted = 0;

    public FlowExecution(FlowDefinition flowDefinition) {
        this.flowDefinition = flowDefinition;
        this.id = idCounter.toString();
        idCounter++;
        freeInputs = flowDefinition.getFreeInputs();
        outputs = flowDefinition.getAllOutputs().values().stream()
                .map(Pair::getKey)
                .collect(Collectors.toSet());
        numOfSteps = flowDefinition.getSteps().size();
    }

    public void setAllData(Map<String, Object> allData) {
        this.allData = allData;
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

    public void setHasExecuted(boolean hasExecuted) {
        this.hasExecuted = hasExecuted;
    }

    public Map<String, Object> getFormalOutputs() {
        return formalOutputs;
    }

    public int getNumOfStepsExecuted() {
        return numOfStepsExecuted;
    }

    public void setNumOfStepsExecuted(int numOfStepsExecuted) {
        this.numOfStepsExecuted = numOfStepsExecuted;
    }

    public int getNumOfSteps() {
        return numOfSteps;
    }

    public void applyContinuation(FlowExecution pastFlow){
        for(DataDefinitionsDeclaration dd:freeInputs){
            if (pastFlow.allData.containsKey(dd.getAliasName())){
                freeInputsValue.put(dd.getAliasName(),pastFlow.allData.get(dd.getAliasName()));
            }
        }
        for(String source:flowDefinition.getContinuationMapping().keySet()){
            freeInputsValue.put(flowDefinition.getContinuationMapping().get(source),pastFlow.allData.get(source));
        }
    }
}
