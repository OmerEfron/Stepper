package StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails;

import java.util.List;

public class Input extends FlowIODetailsImpl{

    private final String necessity;

    private final List<String> relatedSteps;

    public Input(String dataName, String typeName, String necessity, List<String> relatedSteps) {
        super(dataName, typeName);
        this.necessity = necessity;
        this.relatedSteps = relatedSteps;
    }

    public String getNecessity() {
        return necessity;
    }

    public List<String> getRelatedSteps() {
        return relatedSteps;
    }
}
