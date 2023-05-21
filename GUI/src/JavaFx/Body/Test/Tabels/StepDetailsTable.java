package JavaFx.Body.Test.Tabels;

import StepperEngine.FlowDetails.StepDetails.StepDetails;

public class StepDetailsTable {
    private String stepName;
    private String isReadOnly;

    public StepDetailsTable(StepDetails stepDetails){
        this.stepName=stepDetails.getStepName();
        isReadOnly=stepDetails.isReadOnly()? "Yes": "No";

    }

    public String getStepName() {
        return stepName;
    }

    public String getIsReadOnly() {
        return isReadOnly;
    }
}
