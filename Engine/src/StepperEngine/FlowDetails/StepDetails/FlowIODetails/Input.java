package StepperEngine.FlowDetails.StepDetails.FlowIODetails;

import java.util.List;
import java.util.stream.Collectors;

public class Input extends FlowIODetailsImpl{

    private final String necessity;
    private final String relatedStepsString;
    private final List<String> relatedSteps;


    public Input(String dataName, String typeName, String necessity, List<String> relatedSteps) {
        super(dataName, typeName);
        this.necessity = necessity;
        this.relatedSteps = relatedSteps;
        relatedStepsString=relatedSteps.stream().collect(Collectors.joining(" , "));
    }

    public String getNecessity() {
        return necessity;
    }

    public List<String> getRelatedSteps() {
        return relatedSteps;
    }
    public String getRelatedStepsString() {
        return relatedStepsString;
    }
}
