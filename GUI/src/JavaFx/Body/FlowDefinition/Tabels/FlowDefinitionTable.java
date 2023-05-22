package JavaFx.Body.FlowDefinition.Tabels;

import StepperEngine.FlowDetails.FlowDetails;

public class FlowDefinitionTable {
    private String flowName;
    private Integer stepsNumber;
    private Integer freeInputsNumber;
    private Integer continuationsNumber;
    private String description;


    public FlowDefinitionTable(FlowDetails flow){
        flowName= flow.getFlowName();
        stepsNumber=flow.getSteps().size();
        freeInputsNumber=flow.getFreeInputs().size();
        description=flow.getFlowDescription();
        continuationsNumber=5;
    }

    public String getDescription() {
        return description;
    }

    public String getFlowName() {
        return flowName;
    }

    public Integer getStepsNumber() {
        return stepsNumber;
    }

    public Integer getFreeInputsNumber() {
        return freeInputsNumber;
    }

    public Integer getContinuationsNumber() {
        return continuationsNumber;
    }
}
