package StepperEngine;

import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;
import StepperEngine.Flow.api.FlowDefinitionImpl;
import StepperEngine.Flow.api.FlowDefinition;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Flow.execute.runner.FlowExecutor;
import StepperEngine.StepperReader.XMLReadClasses.Flow;
import StepperEngine.StepperReader.XMLReadClasses.TheStepper;
import StepperConsole.FlowDetails.FlowDetails;
import StepperConsole.FlowDetails.FlowDetailsImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * a class that repesents the engine of the system.
 * includes flow
 */
public class Stepper {
    private List<FlowDefinition> flows = new ArrayList<>();

    private List<String> flowNames = new ArrayList<>();

    private Map<String, FlowDefinition> flowsMap = new HashMap<>();

    private Map<Integer, String> flowsByNumber = new LinkedHashMap<>();

    public Stepper(){

    }

    public void newFlows(TheStepper stepper) throws FlowBuildException {
        List<FlowDefinition> newFlows = new ArrayList<>();
        Map<String, FlowDefinition> newFlowsMap = new HashMap<>();
        checkForDuplicateNames(stepper);
        checkForDuplicateOutputs(stepper);
        stepper.getFlows().getFlows().stream()
                .forEach(flow -> {
                    try {
                        newFlows.add(new FlowDefinitionImpl(flow));
                    } catch (FlowBuildException e) {
                        throw new RuntimeException(e);
                    }
                });
        flows = newFlows;
        flows.forEach(flow-> newFlowsMap.put(flow.getName(), flow));
        flowsMap = newFlowsMap;
        flowsByNumber  = IntStream.range(0, flows.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, i -> flows.get(i).getName()));
        flowNames = flows.stream()
                .map(FlowDefinition::getName)
                .collect(Collectors.toList());

    }

    public List<String> getFlowNames() {
        return flowNames;
    }

    public Integer getNumOfFlows(){
        return flows.size();
    }

    public Map<Integer, String> getFlowsByNumber() {
        return flowsByNumber;
    }

    public String flowNameByNumber(Integer i){
        return flows.get(i - 1).getName();
    }

    private void checkForDuplicateOutputs(TheStepper stepper) throws FlowBuildException {
        String duplicateOutput;
        for(Flow flow: stepper.getFlows().getFlows()){
            if((duplicateOutput = checkForDuplicatesOutputsInFlow(flow)) != null)
                throw new FlowBuildException("Duplicate output names: "+duplicateOutput, flow.getName());
        }
    }

    public String checkForDuplicatesOutputsInFlow(Flow flow){
        Set<String> uniqueOutputs = new HashSet<>();
        for(String output: flow.getFlowOutput().split(",")){
            if(!uniqueOutputs.add(output))
                return output;
        }
        return null;
    }



    private static void checkForDuplicateNames(TheStepper stepper) throws FlowBuildException {
        String duplicate;
        if((duplicate = findDuplicateFlowName(stepper)) != null){
            throw new FlowBuildException("Duplicate flow error. " + duplicate + " is already exist", duplicate);
        }
    }

    private static String findDuplicateFlowName(TheStepper stepper) {
        Set<String> flowNames = new HashSet<>();
        for (Flow flow : stepper.getFlows().getFlows()) {
            if (!flowNames.add(flow.getName())) {
                return flow.getName();
            }
        }
        return null;
    }


    public FlowExecution getFlowExecution(int flowNumber){
        FlowExecution flowExecution=new FlowExecution(flows.get(flowNumber-1) );
        return flowExecution;
    }

    public FlowExecution getFlowExecution(String flowName){
        return new FlowExecution(flowsMap.get(flowName));
    }

    public FlowExecution buildFlowExecution(String flowName){
        return new FlowExecution(flowsMap.get(flowName));
    }

    public void ExecuteFlow(FlowExecution flowExecution){
        FlowExecutor flowExecutor=new FlowExecutor();
        flowExecutor.executeFlow(flowExecution);
    }

    public FlowDetails showFlowByName(String flowName){
        Optional<FlowDefinition> flowByName = flows.stream()
                .filter(flow->flow.getName().equals(flowName))
                .findFirst();
        return flowByName.map(FlowDetailsImpl::new).orElse(null);
    }
    public FlowDetails showFlowByNumber(int flowNumber){

        return new FlowDetailsImpl(flows.get(flowNumber-1));
    }

    public FlowDetails buildShowFlow(String flowName){
        return new FlowDetailsImpl(flowsMap.get(flowName));
    }
    public String getNamesOfFlowsToPrint(){
        return IntStream.range(0, flows.size())
                .mapToObj(i -> (i + 1) + "." + flows.get(i).getName() + (i == flows.size() - 1 ? "" : "\n"))
                .collect(Collectors.joining());
    }


    public boolean isFlowExsitByNumber(int flowNumber) {
        return flows.size() > flowNumber-1 && flowNumber >0;
    }
}
