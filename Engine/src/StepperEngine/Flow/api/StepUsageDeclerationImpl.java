package StepperEngine.Flow.api;

import StepperEngine.Step.api.StepDefinition;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StepUsageDeclerationImpl implements StepUsageDecleration, Serializable {

    private String name;
    private final boolean skipIfFail;

    private final StepDefinition stepDefinition;
    private final Map<String,String> nameToAlias=new HashMap<>();

    private final Integer index;



    // Map from name of data in step to the step that the data is in it with the name of the data in that step.
    private final Map<String, Pair<String, String>> dataMap = new HashMap<>();

    public StepUsageDeclerationImpl(StepDefinition stepDefinition, Integer index){
        skipIfFail = false;
        name = stepDefinition.getName();
        this.stepDefinition = stepDefinition;
        this.index = index;
    }

    public StepUsageDeclerationImpl(StepDefinition stepDefinition, boolean skipIfFail, Integer index){
        this.skipIfFail = skipIfFail;
        name = stepDefinition.getName();
        this.stepDefinition = stepDefinition;
        this.index = index;
    }

    public Map<String, String> getNameToAlias() {
        createNameToAliasMap();
        return nameToAlias;
    }

    public void createNameToAliasMap(){
        stepDefinition.getOutputs().forEach(dataDefinitionsDeclaration -> nameToAlias.put(dataDefinitionsDeclaration.getName(),dataDefinitionsDeclaration.getAliasName()));
        stepDefinition.getInputs().forEach(dataDefinitionsDeclaration -> nameToAlias.put(dataDefinitionsDeclaration.getName(),dataDefinitionsDeclaration.getAliasName()));
    }


    StepUsageDeclerationImpl(String name, StepDefinition stepDefinition, boolean skipIfFail, Integer index){
        this.name = name;
        this.skipIfFail = skipIfFail;
        this.stepDefinition = stepDefinition;
        this.index = index;
    }

    StepUsageDeclerationImpl(String name, StepDefinition stepDefinition, Integer index){
        this.skipIfFail = false;
        this.name = name;
        this.stepDefinition = stepDefinition;
        this.index = index;
    }

    @Override
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }

    @Override
    public String getStepFinalName() {
        return name;
    }

    @Override
    public void setAliasName(String alias) {
        this.name = alias;
    }

    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }


    @Override
    public Integer getIndex() {
        return index;
    }

    @Override
    public void addInputToMap(String dataName, String stepRefName, String dataNameInStepRef) {
        dataMap.put(dataName, new Pair(stepRefName, dataNameInStepRef));
    }

    @Override
    public Map<String, Pair<String, String>> getDataMap() {
        return dataMap;
    }

    @Override
    public Pair<String, String> getInputRef(String input) {
        Pair<String, String> res;
        res = dataMap.get(input);
        return res;
    }

    @Override
    public boolean isReadOnlyStep() {
        return stepDefinition.isReadOnly();
    }


}