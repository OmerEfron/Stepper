package StepperEngine.Flow.execute;

import StepperEngine.DataDefinitions.Enumeration.ZipEnumerator;
import StepperEngine.Flow.api.FlowDefinition;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.DataNecessity;
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
    private final Map<DataDefinitionsDeclaration, Object> freeInputsValueByDD = new HashMap<>();

    private String uuidAsString;
    private final Map<String, Object> formalOutputs = new HashMap<>();
    private List<StepExecuteData> stepsData = new LinkedList<>();
    private final Map<String, StepExecuteData> stepsDataMap = new HashMap<>();
    private Map<DataDefinitionsDeclaration,Object> allData=new HashMap<>();
    private final Set<DataDefinitionsDeclaration> freeInputs;
    private final Set<DataDefinitionsDeclaration> outputs;

    private final int numOfSteps;
    private int numOfStepsExecuted = 0;
    private boolean canBeExecuted;

    public FlowExecution(FlowDefinition flowDefinition) {
        this.flowDefinition = flowDefinition;
        this.id = idCounter.toString();
        idCounter++;
        freeInputs = flowDefinition.getFreeInputs();
        outputs = flowDefinition.getAllOutputs().values().stream()
                .map(Pair::getKey)
                .collect(Collectors.toSet());
        numOfSteps = flowDefinition.getSteps().size();
        canBeExecuted = freeInputs.size() == 0;
        createUUID();
        getInitialValues(flowDefinition);
    }

    private void getInitialValues(FlowDefinition flowDefinition) {
        flowDefinition.getInitialInputs().keySet().forEach(key -> freeInputsValueByDD.put(key,flowDefinition.getInitialInputs().get(key) ));
    }

    public StepExecuteData getStepExecuteData(String stepName){
        return stepsDataMap.get(stepName);
    }


    public void setAllData(Map<DataDefinitionsDeclaration, Object> allData) {
        this.allData = allData;
//        for(StepExecuteData step:stepsData){
//            step.setStepData(allData);
//        }
    }

    public void setStepsData(Map<StepUsageDecleration, StepExecuteData> stepsData) {
        this.stepsData = new ArrayList<>(stepsData.values());
        for(StepExecuteData step:this.stepsData){
            stepsDataMap.put(step.getFinalName(), step);
        }
    }

    public void addStepExecuteData(StepExecuteData stepExecuteData) {
        stepsData.add(stepExecuteData);
        stepsDataMap.put(stepExecuteData.getFinalName(), stepExecuteData);
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



    public boolean addFreeInput(String dataName, Object value) {
        if(hasExecuted){
            return false;
        }
        Optional<DataDefinitionsDeclaration> optionalData = flowDefinition.getFreeInputs().stream().filter(input -> input.getAliasName().equals(dataName)).findFirst();
        if(optionalData.isPresent()){
            if(optionalData.get().dataDefinition().getType().isAssignableFrom(value.getClass())){
                return addValueToFreeInputs(optionalData.get(), value);
            }
            else if(optionalData.get().dataDefinition().getName().equals("Enumerator")){
                if(EnumSet.allOf(ZipEnumerator.class).stream()
                        .map(ZipEnumerator::getStringValue)
                        .anyMatch(zipType-> zipType.equals(value))){
                    return addValueToFreeInputs(optionalData.get(), ZipEnumerator.fromString(value.toString()));
                }
            }
        }
        return false;
    }

    private boolean addValueToFreeInputs2(String dataName, Object value) {
        freeInputsValue.put(dataName, value);
        canBeExecuted = freeInputs.stream()
                .filter(data -> data.necessity().equals(DataNecessity.MANDATORY))
                .allMatch(data -> freeInputsValue.containsKey(data));
        return true;
    }

    private boolean addValueToFreeInputs(DataDefinitionsDeclaration dd, Object value){
        freeInputsValueByDD.put(dd, value);
        canBeExecuted = freeInputs.stream()
                .filter(data -> data.necessity().equals(DataNecessity.MANDATORY))
                .allMatch(freeInputsValueByDD::containsKey);
        return true;
    }

    public boolean isCanBeExecuted() {
        return canBeExecuted;
    }


    public Map<DataDefinitionsDeclaration, Object> getFreeInputsValue() {
        return freeInputsValueByDD;
    }

    public void addOutput(String dataName, Object value) {
        formalOutputs.put(dataName, value);
    }

    public Set<DataDefinitionsDeclaration> getOutputs() {
        return outputs;
    }

//
//    public <T> T getOneOutput(String dataName, Class<T> exceptedDataType) {
//        return formalOutputs.containsKey(dataName) ? exceptedDataType.cast(formalOutputs.get(dataName)) : null;
//    }
    //Changed form up to down because we need all outputs
    public <T> T getOneOutput(String dataName, Class<T> exceptedDataType) {
        return allData.containsKey(dataName) ? exceptedDataType.cast(allData.get(dataName)) : null;
    }

    public List<StepExecuteData> getStepsData() {
        return stepsData;
    }

    public Set<DataDefinitionsDeclaration> getFreeInputs() {
        return freeInputs;
    }

    public <T> T getInputValue(DataDefinitionsDeclaration input, Class<T> exceptedDataType){
        return freeInputsValueByDD.containsKey(input) ? exceptedDataType.cast(freeInputsValueByDD.get(input)): null;
    }

    public void setHasExecuted(boolean hasExecuted) {
        this.hasExecuted = hasExecuted;
        canBeExecuted = false;
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
            if (pastFlow.allData.containsKey(dd)){
                freeInputsValueByDD.put(dd,pastFlow.allData.get(dd));
            }
        }
        for(DataDefinitionsDeclaration source:flowDefinition.getContinuationMapping().keySet()){
            freeInputsValueByDD.put(flowDefinition.getContinuationMapping().get(source),pastFlow.allData.get(source));
        }
    }
}
