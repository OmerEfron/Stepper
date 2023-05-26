package StepperEngine.FlowDetails.StepDetails.FlowIODetails;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Input extends FlowIODetailsImpl{

    private final String necessity;
    private final String relatedStepsString;
    private final String connectedToStep;
    private final String formOutput;
    private final List<String> relatedSteps;


    public Input(String dataName, String typeName, String necessity, List<String> relatedSteps) {
        super(dataName, typeName);
        this.necessity = necessity;
        this.relatedSteps = relatedSteps;
        relatedStepsString=relatedSteps.stream().collect(Collectors.joining(" , "));
        connectedToStep="";
        formOutput="";
    }
    public Input(String dataName, String typeName, String necessity, Pair<String,String> fromStepsData) {
        super(dataName, typeName);
        this.necessity = necessity;
        this.relatedSteps = new ArrayList<>();
        relatedStepsString=relatedSteps.stream().collect(Collectors.joining(" , "));
        connectedToStep=fromStepsData.getKey();
        formOutput=fromStepsData.getValue();
    }

    public String getConnectedToStep() {
        return connectedToStep;
    }

    public String getFormOutput() {
        return formOutput;
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
