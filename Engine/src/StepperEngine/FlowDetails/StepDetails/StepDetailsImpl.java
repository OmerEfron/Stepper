package StepperEngine.FlowDetails.StepDetails;

import StepperEngine.Flow.api.StepUsageDecleration;

/**
 * holds a step details from a certain flow.
 */
public class StepDetailsImpl implements StepDetails{


    private final String stepName; // its final name.
    private final boolean readOnly;
    public StepDetailsImpl(StepUsageDecleration step){
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
