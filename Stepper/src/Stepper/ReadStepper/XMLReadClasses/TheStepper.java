package Stepper.ReadStepper.XMLReadClasses;

import generated.STStepper;

public class TheStepper {

    private Flows flows;


    public TheStepper(STStepper stStepper){
        this.flows = new Flows(stStepper.getSTFlows());
    }
    public Flows getFlows() {
        return flows;
    }

    public void setFlows(Flows flows) {
        this.flows = flows;
    }
}
