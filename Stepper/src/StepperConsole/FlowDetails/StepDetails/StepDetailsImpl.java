package StepperConsole.FlowDetails.StepDetails;

import StepperEngine.Flow.api.StepUsageDeclerationInterface;

public class StepDetailsImpl implements StepDetails{


    private final String stepName;
    private final boolean readOnly;
    public StepDetailsImpl(StepUsageDeclerationInterface step){
        stepName = step.getStepFinalName();
        readOnly = step.isReadOnlyStep();
    }

    @Override
    public String getStepName() {
        return stepName;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }
}
