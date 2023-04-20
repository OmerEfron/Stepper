package Stepper.Logic.impl;

import Stepper.Logic.ReadStepper.Flow;
import Stepper.Logic.ReadStepper.StepInFlow;
import Stepper.Logic.ReadStepper.StepsInFlow;
import Stepper.Logic.ReadStepper.TheStepper;
import Stepper.Logic.api.NameToStep;
import Stepper.Logic.api.StepperDefinitionLogic;
import Stepper.Step.StepDefinitionRegistry;
import Stepper.Step.api.StepDefinitionAbstractClass;

import java.util.*;
import java.util.stream.Collectors;

public class StepperDefinitionLogicImpl implements StepperDefinitionLogic {
    List<String> problems = new ArrayList();
    //List<String> stepNames=new ArrayList();
    NameToStep nameToStep=new NameToStepImpl();
    //List<String> stepNames= Arrays.stream(StepDefinitionRegistry.values()).map(StepDefinitionAbstractClass::getName).collect(Collectors.toList());
    //Set<String>stepsNames = EnumSet.allOf(StepDefinitionRegistry.)

    @Override
    public List<String> validateStepper(TheStepper theStepper) {
        if(hasDuplicateNames(theStepper)){
            return problems;
        }
        if(getStepsNotExits(theStepper)){
            return problems;
        }


        return problems;
    }
    private boolean getStepsNotExits (TheStepper theStepper){
        List<StepInFlow> stepsNotExits= theStepper.getFlows().getFlows().stream()
                .flatMap(flow -> flow.getStepsInFlow().getStepsInFlowList().stream())
                .filter(stepInFlow -> !isStepInSystem(stepInFlow))
                .collect(Collectors.toList());
        if(stepsNotExits != null){
            StringBuilder problem = new StringBuilder();
            problem.append("The following steps dosent exist:\n");

            stepsNotExits.stream().forEach(stepInFlow ->problem.append(stepInFlow.getName()+"\n"));
            problems.add(problem.toString());
            return true;
        }
        return false;
    }
    private boolean isStepInSystem(StepInFlow step){
        return nameToStep.isStepInSystem(step.getName());
        //bulidStepsNamesList();
        //return stepNames.contains(step.getName());
    }

//    private void bulidStepsNamesList() {
//        stepNames.add("Spend Some Time");
//        stepNames.add("Collect Files In Folder");
//        stepNames.add("Files Deleter");
//        stepNames.add("Files Renamer");
//        stepNames.add("Files Content Extractor");
//        stepNames.add("CSV Exporter");
//        stepNames.add("Properties Exporter");
//        stepNames.add("File Dumper");
//    }

    private boolean hasDuplicateNames(TheStepper theStepper) {
        if(theStepper.getFlows().getFlows().stream()
                .distinct()
                .count() != theStepper.getFlows().getFlows().size()){
            problems.add("duplicate flows names");
            return true;
        }
        return false;
    }
}
