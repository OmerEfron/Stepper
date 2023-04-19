package Stepper.Logic.ReadStepper;

import generated.STStepInFlow;

import javax.xml.bind.annotation.XmlAttribute;

public class StepInFlow {

    private String name;

    private String alias;

    private Boolean continueIfFailing;

    StepInFlow(STStepInFlow stStepInFlow){
        this.name = stStepInFlow.getName();
        this.alias = stStepInFlow.getAlias();
        this.continueIfFailing = stStepInFlow.isContinueIfFailing();
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
