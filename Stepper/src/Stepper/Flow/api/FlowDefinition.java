package Stepper.Flow.api;

import Stepper.Logic.ReadStepper.CustomMapping;
import Stepper.Logic.ReadStepper.Flow;
import Stepper.Logic.ReadStepper.StepInFlow;
import Stepper.Step.StepDefinitionRegistry;
import Stepper.Step.api.DataDefinitionsDeclaration;

import java.util.*;
import java.util.stream.Collectors;

public class FlowDefinition implements FlowDefinitionInterface {

    private final Flow flow;


    private Set<String> outputs;

    private Set<String> freeInputs;

    private Map<String,String> freeInputsFromUser;

    private List<StepUsageDeclerationInterface> steps;

    private final List<String> problems = new ArrayList<>();

    private Boolean valid = null;

    public FlowDefinition(Flow flow) {
        freeInputs = new HashSet<>();
        this.flow = flow;
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

    // might be unnecessary.
    @Override
    public void addStep(StepUsageDeclerationInterface stepUsageDeclerationInterface) {
        steps.add(stepUsageDeclerationInterface);
    }

    @Override
    public String outputStrings() {
        return flow.getFlowOutput();
    }

    @Override
    public List<String> isFlowValid() {

        getStepUsages();
        if(!problems.isEmpty()){
            valid = false;
            return problems;
        }

        customMapping();
        if(!problems.isEmpty()){
            valid = false;
            return problems;
        }
        autoMapping();

        return problems;
    }

    @Override
    public void customMapping() {
        flow.getCustomMappings().getCustomMappings()
                .forEach(this::customMap);
    }

    private void customMap(CustomMapping customMapping){
        StepUsageDeclerationInterface source = findStepUsageByName(customMapping.getSourceStep());
        StepUsageDeclerationInterface target = findStepUsageByName(customMapping.getTargetStep());
        if(source == null){
            problems.add("source not found");
        } else if (target == null) {
            problems.add("target not found");
        } else if (!isSourceBeforeTarget(source, target)) {
            problems.add("source is after target");
        }
        else if(!isDataPartOfStepOutPuts(customMapping, source)){
            problems.add("cant find data in step");
        }
        else{
            target.addInputToMap(customMapping.getTargetData(), customMapping.getSourceStep(), customMapping.getSourceData());
        }
    }

    private StepUsageDeclerationInterface findStepUsageByName(String name) {
        StepUsageDeclerationInterface stepUsage = steps.stream()
                .filter(step -> step.getStepFinalName().equals(name))
                .findFirst()
                .orElse(null);
        return stepUsage;
    }

    private static boolean isSourceBeforeTarget(StepUsageDeclerationInterface source, StepUsageDeclerationInterface target) {
        return source.getIndex() < target.getIndex();
    }

    private static boolean isDataPartOfStepOutPuts(CustomMapping customMapping, StepUsageDeclerationInterface source) {
        return source.getStepDefinition().getOutputs().stream()
                .anyMatch(dd -> dd.getName().equals(customMapping.getSourceData()));
    }

    @Override
    public void autoMapping() {
        for(StepUsageDeclerationInterface step:steps)
            autoMap(step);
    }

    private void autoMap(StepUsageDeclerationInterface step) {
        Set<DataDefinitionsDeclaration> stepFreeInputs = getStepFreeInputs(step);
        for(int i=0;i<step.getIndex();i++){
            StepUsageDeclerationInterface currStep = steps.get(i);
            stepFreeInputs.stream().filter(input-> currStep.getStepDefinition().getOutputs().contains(input))
                    .forEach((input) -> step.addInputToMap(input.getName(), currStep.getStepFinalName(), input.getName()));
        }
    }



    private static Set<DataDefinitionsDeclaration> getStepFreeInputs(StepUsageDeclerationInterface step) {
        return step.getStepDefinition().getInputs().stream()
                .filter(input -> step.getInputRef(input.getName()) == null)
                .collect(Collectors.toSet());
    }


    private StepUsageDeclerationInterface getStepUsage(StepInFlow stepInFlow){
        StepDefinitionRegistry stepDefinitionRegistry = getStepDefinitionByName(stepInFlow.getName());
        if(stepDefinitionRegistry == null){
            problems.add("The following step is not exist: " + stepInFlow.getName());
            valid = false;
            return null;
        }
        if(stepInFlow.getAlias() != null && stepInFlow.isContinueIfFailing() != null){
            return new StepUsageDeclerationClass(stepInFlow.getAlias(), stepDefinitionRegistry.getStepDefinition(), stepInFlow.isContinueIfFailing(), stepInFlow.getNumOfStep() );
        }
        else if(stepInFlow.getAlias() != null){
            return new StepUsageDeclerationClass(stepInFlow.getAlias(), stepDefinitionRegistry.getStepDefinition(), stepInFlow.getNumOfStep());
        }
        else if (stepInFlow.isContinueIfFailing() != null){
            return new StepUsageDeclerationClass(stepDefinitionRegistry.getStepDefinition(), stepInFlow.isContinueIfFailing(), stepInFlow.getNumOfStep());
        }
        else{
            return new StepUsageDeclerationClass(stepDefinitionRegistry.getStepDefinition(), stepInFlow.getNumOfStep());
        }
    }
    private void getStepUsages() {
        steps = flow.getStepsInFlow().getStepsInFlowList().stream()//get list of step usages for the specific flow.
                .map(this::getStepUsage)
                .collect(Collectors.toList());
        steps.sort(Comparator.comparingInt(StepUsageDeclerationInterface::getIndex));

    }

    private List<StepUsageDeclerationInterface> initStepList(){
        List<StepUsageDeclerationInterface> res = new ArrayList<>();
        return res;
    }

    // gets a step name and returns a stepUsage matching to name.
    // if there isnt a step with that name, returns null and adds the problem to problem list.
    private StepDefinitionRegistry getStepDefinitionByName(String name){
        for(StepDefinitionRegistry stepDefinition: StepDefinitionRegistry.values()){
            if (stepDefinition.getStepDefinition().getName().equals(name)){
                return stepDefinition;
            }
        }
        return null;
    }
}
