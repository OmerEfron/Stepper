package StepperEngine.FlowDetails.StepDetails.FlowIODetails;

public class Output extends FlowIODetailsImpl{

    private final String stepRelated;
    private final String connectedToStep;
    private final String toInput;

    public Output(String dataName, String typeName, String stepRelated) {
        super(dataName, typeName);
        this.stepRelated = stepRelated;
        this.connectedToStep="";
        this.toInput="";
    }
    public Output(String dataName, String typeName,String connectedToStep,String toInput ) {
        super(dataName, typeName);
        this.stepRelated = "";
        this.connectedToStep=connectedToStep;
        this.toInput=toInput;
    }


    public String getConnectedToStep() {
        return connectedToStep;
    }

    public String getToInput() {
        return toInput;
    }

    public String getStepRelated() {
        return stepRelated;
    }
}
