package StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails;

public class Output extends FlowIODetailsImpl{

    private final String stepRelated;

    public Output(String dataName, String typeName, String stepRelated) {
        super(dataName, typeName);
        this.stepRelated = stepRelated;
    }

    public String getStepRelated() {
        return stepRelated;
    }
}
