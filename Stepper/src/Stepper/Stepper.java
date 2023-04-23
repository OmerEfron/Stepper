package Stepper;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.Flow.api.FlowDefinition;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.runner.FlowExecutor;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;

import java.util.List;
import java.util.stream.Collectors;

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


    public void executeFlows(){
        FlowExecutor flowExecutor=new FlowExecutor();
        for(FlowDefinitionInterface flow:flows){
            FlowExecution flowExecution=new FlowExecution(flow,"1");

            flowExecutor.executeFlow(flowExecution);
        }
    }

}
