package StepperEngine.Flow.api;

import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;
import StepperEngine.StepperReader.XMLReadClasses.CustomMapping;
import StepperEngine.StepperReader.XMLReadClasses.Flow;
import StepperEngine.StepperReader.XMLReadClasses.FlowLevelAlias;
import StepperEngine.StepperReader.XMLReadClasses.StepInFlow;
import StepperEngine.Step.StepBuilder;
import StepperEngine.Step.StepDefinitionRegistry;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.DataNecessity;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class FlowDefinition implements FlowDefinitionInterface {

    private final Flow flow;

    private Boolean valid;

    private Boolean isReadOnly = true;

    //   <output name, <data definition, the step that related to him>
    Map<String , Pair<DataDefinitionsDeclaration,String>> allOuputs=new HashMap<>();
    Map<String , Pair<DataDefinitionsDeclaration,String>> formalOuputs =new HashMap<>();

    private Set<DataDefinitionsDeclaration> freeInputs;

    private final List<StepUsageDeclerationInterface> steps = new ArrayList<>();

    private final List<String> problems = new ArrayList<>();


    public FlowDefinition(Flow flow) throws FlowBuildException {
        freeInputs = new HashSet<>();
        this.flow = flow;
        isFlowValid();
        if(problems.size() > 0){
            valid = false;
            throw new FlowBuildException(problems.get(0), flow.getName());
        }
        valid = true;
        determinateIfFlowIsReadOnly();

   }

    private void determinateIfFlowIsReadOnly() {
        for(StepUsageDeclerationInterface step:steps){
            if(!step.isReadOnlyStep()) {
                isReadOnly = false;
                break;
            }
        }
        //isReadOnly = steps.stream().anyMatch(step->step.getStepDefinition().isReadOnly());
    }

    public boolean isReadOnlyFlow(){
        return isReadOnly;
    }


    @Override
    public String getName() {
        return flow.getName();
    }

    @Override
    public String getDescription() {
        return flow.getFlowDescription();
    }


    @Override
    public List<StepUsageDeclerationInterface> getSteps() {
        return steps;
    }


    @Override
    public String outputStrings() {
        return flow.getFlowOutput();
    }

    /**
     * method to validate the flow.
     * @return a list of problems in string format. if there are no problems, returns an empty list.
     */
    @Override
    public List<String> isFlowValid() {

        if(valid != null) // in case the validation already happened. else, validate the flow.
            return problems;

        buildStepUsages();
        if(!problems.isEmpty()){
            return problems;
        }
        stepAlising();

        flowLevelAliasing();
        if(!problems.isEmpty()){
            return problems;
        }



        customMapping();
        if(!problems.isEmpty()){
            return problems;
        }

        autoMapping();

        setFreeInputs();
        if(!problems.isEmpty()){
            return problems;
        }

        if(!flow.getFlowOutput().isEmpty()) {
            updateAlloutputs();
        }
        if(!problems.isEmpty()){
            return problems;
        }
        valid = true;
        return problems;
    }

    @Override
    public void autoMapping() {
        for(StepUsageDeclerationInterface step:steps)
            autoMap(step);
    }

    /**
     * applying custom mapping on each custom map instance in flow.
     */
    @Override
    public void customMapping() {
        if (flow.getCustomMappings().getCustomMappings() != null) {
            flow.getCustomMappings().getCustomMappings()
                    .forEach(this::customMap);

        }
    }

    /**
     * finds all free inputs of the flow
     * if a mandatory input that is not a user-friendly one is found, adds it to the problems list.
     */
    private void setFreeInputs() {
        freeInputs = steps.stream()
                .flatMap(step -> getStepFreeInputs(step).stream())
                .collect(Collectors.toSet());
        for(DataDefinitionsDeclaration dd: freeInputs){
            if(!dd.dataDefinition().isUserFriendly() && dd.necessity() == DataNecessity.MANDATORY){
                problems.add("Mandatory input \""+ dd.userString() + "\" is not accessible");
            }
        }

    }
    public Set<DataDefinitionsDeclaration> getFreeInputsFromUser(){return freeInputs;}

    /**
     * applying step aliasing for each step, if needed.
     */
    public void stepAlising() {
        steps.stream()
                .collect(Collectors.groupingBy(StepUsageDeclerationInterface::getStepFinalName))
                .forEach((name, list) -> {
                    if(list.size() > 1)
                        for (int i = 0; i < list.size(); i++)
                            list.get(i).setAliasName(name + "(" + i + ")");

                });
    }


    /**
     * applying flow level aliasing.
     */
    public void flowLevelAliasing(){
        if(flow.getFlowLevelAliasing().getFlowLevelAliases() != null) {
            flow.getFlowLevelAliasing().getFlowLevelAliases()
                    .forEach(this::alias);
        }
    }


    /**
     * taking a FlowLevelAlias instance and updates the source data with his new name.
     * if the source or the step not found, adds to the problems list with the details.
     * @param fla - Flow Level Alias that represents which data should get the alias name.
     */
    private void alias(FlowLevelAlias fla) {
        StepUsageDeclerationInterface step = findStepUsageByName(fla.getStep());
        if (step == null) {
            problems.add("Trying to alias step " + fla.getStep() + ", step not found");
            return;
        }

        // check in the output datadef list.
        boolean found = setAliasIfPresent(step.getStepDefinition().getOutputs(), fla.getSourceDataName(), fla.getAlias());
        if (!found) { // if not found in outputs, check in the inputs
            found = setAliasIfPresent(step.getStepDefinition().getInputs(), fla.getSourceDataName(), fla.getAlias());
        }

        //if not found in inputs nor outputs, adds the details to the problems the source data name
        if (!found) {
            problems.add("Source data name not found: " + fla.getSourceDataName());
        }
    }

    /**
     *
     * @param dataDefs - a list of data definition declarations
     * @param sourceDataName - the source data name
     * @param alias - the name we want to give to source data name
     * @return - true if the aliasing succeed, else false.
     */
    private boolean setAliasIfPresent(List<DataDefinitionsDeclaration> dataDefs, String sourceDataName, String alias) {
        return dataDefs.stream()
                .filter(dd -> dd.getName().equals(sourceDataName))
                .findFirst()
                .map(dd -> {
                    dd.setAliasName(alias);
                    return true;
                })
                .orElse(false);
    }


    /**
     * takes the custom mapping instance, and applying on flow.
     * adds to the matching target step's map of inputs, the source step's name and the data as it calls in the
     * source.
     * if source or target not found,
     * @param customMapping a structure for source/target data and step
     */
    private void customMap(CustomMapping customMapping){
        StepUsageDeclerationInterface source = findStepUsageByName(customMapping.getSourceStep());
        StepUsageDeclerationInterface target = findStepUsageByName(customMapping.getTargetStep());
        if(source == null){
            problems.add("source not found " + customMapping.getSourceStep());
        } else if (target == null) {
            problems.add("target not found " + customMapping.getTargetStep());
        } else if (!isSourceBeforeTarget(source, target)) {
            problems.add("source step " + customMapping.getSourceStep() + "is after target step " + customMapping.getTargetStep());
        }
        else if(!isDataPartOfStepOutPuts(customMapping, source)){
            problems.add("cant find data in source step " + source.getStepFinalName() + ", data name " + customMapping.getSourceData());
        }
        else if(!isDataPartOfStepInPuts(customMapping, target)){
            problems.add("cant find data in target step " + target.getStepFinalName() + ", data name " + customMapping.getTargetData());
        }
        else{
            target.addInputToMap(customMapping.getTargetData(), customMapping.getSourceStep(), customMapping.getSourceData());
        }
    }




    /**
     * checks if source step is before target step
     * @param source - a stepUsage
     * @param target - a stepUsage
     * @return true if source before target.
     */
    private static boolean isSourceBeforeTarget(StepUsageDeclerationInterface source, StepUsageDeclerationInterface target) {
        return source.getIndex() < target.getIndex();
    }

    /**
     * checks if source step outputs contains any data with the name specified at the custom mapping instance.
     * @param customMapping - a CustomMapping instance.
     * @param source - a stepUsage
     * @return true if customMapping.sourceData is a data in source.
     */
    private static boolean isDataPartOfStepOutPuts(CustomMapping customMapping, StepUsageDeclerationInterface source) {
        return source.getStepDefinition().getOutputs().stream()
                .anyMatch(dd -> dd.getAliasName().equals(customMapping.getSourceData()));
    }

    /**
     * checks if target step inputs contains any data with the name specified at the custom mapping instance.
     * @param customMapping - a CustomMapping instance.
     * @param target - a stepUsage
     * @return true if customMapping.targetData is a data in target.
     */
    private static boolean isDataPartOfStepInPuts(CustomMapping customMapping, StepUsageDeclerationInterface target) {
        return target.getStepDefinition().getInputs().stream()
                .anyMatch(dd -> dd.getAliasName().equals(customMapping.getTargetData()));
    }
    /**
     *
     * @param name - a name of a step in the flow
     * @return the step usage of it
     */
    private StepUsageDeclerationInterface findStepUsageByName(String name) {
        return steps.stream()
                .filter(step -> step.getStepFinalName().equals(name))
                .findFirst()
                .orElse(null);
    }




    private void autoMap(StepUsageDeclerationInterface step) {
        Set<DataDefinitionsDeclaration> stepFreeInputs = getStepFreeInputs(step);
        for(int i=0;i<step.getIndex();i++){
            StepUsageDeclerationInterface currStep = steps.get(i);
            stepFreeInputs.stream().filter(input-> currStep.getStepDefinition().getOutputs().contains(input))
                    .forEach((input) -> step.addInputToMap(input.getName(), currStep.getStepFinalName(), input.getName()));
        }
    }


    /**
     * create a set of a step freeInputs as defined.
     * @param step the step to get from it the free inputs
     * @return a set of DataDefinitionDeclarations.
     */
    private static Set<DataDefinitionsDeclaration> getStepFreeInputs(StepUsageDeclerationInterface step) {
        return step.getStepDefinition().getInputs().stream()
                .filter(input -> step.getInputRef(input.getAliasName()) == null)
                .collect(Collectors.toSet());
    }


    /**
     * creates a new stepUsage in the system, using the stepInFlow instance
     * if there is invalid data in the stepInFlow, adds the problem to problems and returns null.
     * @param stepInFlow a step that has been read by the flowReader to a StepInFlow instance.
     * @return a new instance of StepUsage if stepInFlow valid, else false.
     */

    private StepUsageDeclerationInterface createNewStepUsage(StepInFlow stepInFlow){
        StepDefinitionRegistry stepDefinitionRegistry = getStepDefinitionByName(stepInFlow.getName());
        if(stepDefinitionRegistry == null){
            problems.add("The following step is not exist: " + stepInFlow.getName());
            return null;
        }
        if(stepInFlow.getAlias() != null && stepInFlow.isContinueIfFailing() != null){
            return new StepUsageDeclerationClass(stepInFlow.getAlias(),
                    new StepBuilder().getStepInstance(stepDefinitionRegistry.getStepDefinition().getClass().getName()),
                    stepInFlow.isContinueIfFailing(), stepInFlow.getNumOfStep() );
        }
        else if(stepInFlow.getAlias() != null){
            return new StepUsageDeclerationClass(stepInFlow.getAlias(),
                    new StepBuilder().getStepInstance(stepDefinitionRegistry.getStepDefinition().getClass().getName()),
                    stepInFlow.getNumOfStep());
        }
        else if (stepInFlow.isContinueIfFailing() != null){
            return new StepUsageDeclerationClass(
                    new StepBuilder().getStepInstance(stepDefinitionRegistry.getStepDefinition().getClass().getName()),
                    stepInFlow.isContinueIfFailing(), stepInFlow.getNumOfStep());
        }
        else{
            return new StepUsageDeclerationClass(
                    new StepBuilder().getStepInstance(stepDefinitionRegistry.getStepDefinition().getClass().getName()),
                    stepInFlow.getNumOfStep());
        }
    }

    /**
     * creates all steps in the system.
     * if and when finds a step name that isn't in system, breaks after adding the problem to the problems.
     */
    private void buildStepUsages() {
        boolean flag = true;
        for (StepInFlow stepInFlow : flow.getStepsInFlow().getStepsInFlowList()) {
            StepUsageDeclerationInterface stepUsage = createNewStepUsage(stepInFlow);
            if (stepUsage == null) {
                flag = false;
                break; // stop iteration if a step is not found
            }
            steps.add(stepUsage);
        }
        if(flag) // then it means that all steps are in the system.
            steps.sort(Comparator.comparingInt(StepUsageDeclerationInterface::getIndex));

    }


    /**
     * gets a step name and returns a stepUsage matching to name.
     * if there isn't a step with that name, returns null and adds the problem to problem list.
     * @param stepName - a step name
     * @return StepDefinition registry that matches to name.
     */
    private StepDefinitionRegistry getStepDefinitionByName(String stepName){
//        NameToStep nameToStep=new NameToStepImpl();
//        return nameToStep.getDataDefinitionRegistry(stepName);
        for(StepDefinitionRegistry stepDefinition: StepDefinitionRegistry.values()){
            if (stepDefinition.getStepDefinition().getName().equals(stepName)){
                return stepDefinition;
            }
        }
        return null;
    }

    /**
     * @return all free inputs fo th flow
     */
    public Map<DataDefinitionsDeclaration, List<String>> getFreeInputsWithOptional() {
        return steps.stream()
                .flatMap(step -> getStepFreeInputs(step).stream().map(dd -> new AbstractMap.SimpleEntry<>(dd, step.getStepFinalName())))
                .filter(entry -> entry.getKey().dataDefinition().isUserFriendly())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    /**
     * update's all outputs of the system
     */
    private void updateAlloutputs(){
        allOuputs = steps.stream()
                .flatMap(step -> step.getStepDefinition().getOutputs().stream()
                        .map(dd -> new AbstractMap.SimpleEntry<>(dd.getAliasName(), new Pair<>(dd, step.getStepFinalName()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, HashMap::new));
        this.updateFormalOutputs();
    }
    @Override
    public Map<String , Pair<DataDefinitionsDeclaration,String>> getAllOutputs() {
        return allOuputs;
    }

    private void updateFormalOutputs(){
        formalOuputs =Arrays.stream(flow.getFlowOutput().split(","))
                .filter(allOuputs::containsKey)
                .collect(Collectors.toMap(name -> name, allOuputs::get));

        if((Arrays.stream(flow.getFlowOutput().split(","))).collect(Collectors.toList()).size()!=formalOuputs.size())
        {
            for(String outputName:Arrays.stream(flow.getFlowOutput().split(",")).collect(Collectors.toList()))
            {
                if(!formalOuputs.containsKey(outputName))
                    problems.add("The output: "+outputName+" dosent exists in the flow.");
            }
        }
    }

    @Override
    public Map<String, Pair<DataDefinitionsDeclaration, String>> getFormalOuputs() {
        return formalOuputs;
    }

    @Override
    public Set<DataDefinitionsDeclaration> getFreeInputs() {
        return freeInputs;
    }
}