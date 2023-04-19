package Stepper.Logic.impl;

import Stepper.Logic.ReadStepper.StepInFlow;
import Stepper.Logic.ReadStepper.TheStepper;
import Stepper.Logic.api.StepperDefinitionLogic;
import Stepper.Step.StepDefinitionRegistry;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class StepperDefinitionLogicImpl implements StepperDefinitionLogic {
    List<String> problems = new ArrayList();

    //Set<String>stepsNames = EnumSet.allOf(StepDefinitionRegistry.)

    @Override
    public List<String> validateStepper(TheStepper theStepper) {
        if(hasDuplicateNames(theStepper)){
            return problems;
        }



        return problems;
    }

//    private boolean isStepInSystem(StepInFlow step){
//
//    }

    private boolean hasDuplicateNames(TheStepper theStepper) {
        if(theStepper.getFlows().getFlows().stream()
                .distinct()
                .count() != theStepper.getFlows().getFlows().size()){
            problems.add("duplicate flows names");
            return false;
        }
        return true;
    }
}
