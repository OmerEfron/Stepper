package Stepper;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.Flow.api.FlowDefinition;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.runner.FlowExecutor;
import Stepper.ReadStepper.XMLReadClasses.Flow;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import StepperConsole.Flow.ShowFlow;
import StepperConsole.Flow.ShowFlowImpl;

import java.util.*;
import java.util.stream.Collectors;

public class Stepper {
    private List<FlowDefinitionInterface> flows;

    private Map<String, FlowDefinitionInterface> flowsMap;

    public Stepper(TheStepper stepper) throws FlowBuildException{
        checkForDuplicateNames(stepper);
        checkForDuplicateOutputs(stepper);

        flows = stepper.getFlows().getFlows().stream()
                .map(element-> {
                    try {
                        return new FlowDefinition(element);
                    } catch (RuntimeException | FlowBuildException e) {
                        throw new RuntimeException("Error while building stepper: " + e.toString());
                    }
                })
                .collect(Collectors.toList());

        flowsMap = flows.stream().collect(Collectors.toMap(FlowDefinitionInterface::getName, flow -> flow));
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
            throw new FlowBuildException("flow " + duplicate + " is already exist", duplicate);
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


    public void executeFlows(){
        FlowExecutor flowExecutor=new FlowExecutor();
        for(FlowDefinitionInterface flow:flows){
            FlowExecution flowExecution=new FlowExecution(flow,"1");

            flowExecutor.executeFlow(flowExecution);
        }
    }

    public ShowFlow showFlowByName(String flowName){
        Optional<FlowDefinitionInterface> flowByName = flows.stream()
                .filter(flow->flow.getName().equals(flowName))
                .findFirst();
        return flowByName.map(ShowFlowImpl::new).orElse(null);
    }

}
