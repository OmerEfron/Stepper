package Stepper;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.Flow.api.FlowDefinition;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Logic.ReadStepper.TheStepper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Stepper {

    List<String> problems;
    List<FlowDefinitionInterface> flows;

    public Stepper(TheStepper stepper) {
        flows = stepper.getFlows().getFlows().stream()
                .map(element-> {
                    try {
                        return new FlowDefinition(element);
                    } catch (FlowBuildException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public void showProblems(){
        if(!problems.isEmpty())
            System.out.println("Problem found!\n" + problems.get(0));
        else System.out.println("Flow is valid");
    }

//    public boolean validFlows(){
//        if(flows.stream().distinct().count() != flows.size()){
//            return false;
//        }
//
//        Optional<List<String>> optionalProblems = flows.stream()
//                .map(flow -> flow.isFlowValid())
//                .filter(result -> !((List<?>) result).isEmpty())
//                .findFirst();
//        if(optionalProblems.isPresent()){
//            problems = optionalProblems.get();
//            showProblems();
//            return false;
//        }
//        return true;
//    }
}
