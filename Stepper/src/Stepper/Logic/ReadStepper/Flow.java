package Stepper.Logic.ReadStepper;

import generated.STCustomMappings;
import generated.STFlow;
import generated.STFlowLevelAliasing;
import generated.STStepsInFlow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

public class Flow {
    private String flowOutput;
    private CustomMappings customMappings;
    private FlowLevelAliasing flowLevelAliasing;

    private StepsInFlow stepsInFlow;

    private String flowDescription;

    private String name;
    private Map<String,String> customMapping;

    public String getFlowOutput() {
        return flowOutput;
    }

    public CustomMappings getCustomMappings() {
        return customMappings;
    }

    public FlowLevelAliasing getFlowLevelAliasing() {
        return flowLevelAliasing;
    }

    public StepsInFlow getStepsInFlow() {
        return stepsInFlow;
    }

    public String getFlowDescription() {
        return flowDescription;
    }

    public String getName() {
        return name;
    }

    public Flow(STFlow stflow){
        this.flowOutput = stflow.getSTFlowOutput();
        this.flowDescription = stflow.getSTFlowDescription();
        this.stepsInFlow = new StepsInFlow(stflow.getSTStepsInFlow());
        this.name = stflow.getName();
        this.customMappings = new CustomMappings(stflow.getSTCustomMappings());
        this.flowLevelAliasing = new FlowLevelAliasing(stflow.getSTFlowLevelAliasing());
    }

    public Flow(String flowOutput, CustomMappings customMappings, FlowLevelAliasing flowLevelAliasing, StepsInFlow stepsInFlow, String flowDescription, String name) {
        this.flowOutput = flowOutput;
        this.customMappings = customMappings;
        this.flowLevelAliasing = flowLevelAliasing;
        this.stepsInFlow = stepsInFlow;
        this.flowDescription = flowDescription;
        this.name = name;
    }
}
