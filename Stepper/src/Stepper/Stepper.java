package Stepper;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.Flow.api.FlowDefinition;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.runner.FlowExecutor;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import StepperConsole.Flow.ShowFlow;
import StepperConsole.Flow.ShowFlowImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Stepper {
    private List<FlowDefinitionInterface> flows;

    public Stepper(TheStepper stepper) throws FlowBuildException{
        flows = stepper.getFlows().getFlows().stream()
                .map(element-> {
                    try {
                        return new FlowDefinition(element);
                    } catch (RuntimeException | FlowBuildException e) {
                        throw new RuntimeException("Error while building stepper: " + e.toString());
                    }
                })
                .collect(Collectors.toList());
    }


    public FlowExecution getFlowExecution(int flowNumber){
        FlowExecution flowExecution=new FlowExecution(flows.get(flowNumber-1) );
        return flowExecution;
    }
    public void ExecuteFlow(FlowExecution flowExecution){
        FlowExecutor flowExecutor=new FlowExecutor();
        flowExecutor.executeFlow(flowExecution);
    }

    public ShowFlow showFlowByName(String flowName){
        Optional<FlowDefinitionInterface> flowByName = flows.stream()
                .filter(flow->flow.getName().equals(flowName))
                .findFirst();
        return flowByName.map(ShowFlowImpl::new).orElse(null);
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
