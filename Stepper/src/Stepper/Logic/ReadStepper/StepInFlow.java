package Stepper.Logic.ReadStepper;

import Stepper.Logic.api.NameToStep;
import Stepper.Logic.impl.NameToStepImpl;
import Stepper.Step.StepDefinitionRegistry;
import generated.STStepInFlow;

import javax.xml.bind.annotation.XmlAttribute;

public class StepInFlow {

    private String name;

    private String alias;

    private Boolean continueIfFailing;
    private StepDefinitionRegistry stepDefinitionRegistry;
    private int numOfStep;

    private static Integer count = 0;

    StepInFlow(STStepInFlow stStepInFlow){
        this.name = stStepInFlow.getName();
        this.alias = stStepInFlow.getAlias();
        this.continueIfFailing = stStepInFlow.isContinueIfFailing();
        numOfStep = count++;
        stepDefinitionRegistry=getStepDefinitionRegistry(name);

    }

    public static void resetIndexing(){
        count = 0;
    }

    public int getNumOfStep() {
        return numOfStep;
    }

    private StepDefinitionRegistry getStepDefinitionRegistry(String name){
        NameToStep nameToStep=new NameToStepImpl();
        return nameToStep.getDataDefinitionRegistry(name);
    }

    StepInFlow(String name, String alias, Boolean continueIfFailing){
        this.name  = name;
        this.alias = alias;
        this.continueIfFailing = continueIfFailing;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public Boolean isContinueIfFailing() {
        return continueIfFailing;
    }
}
