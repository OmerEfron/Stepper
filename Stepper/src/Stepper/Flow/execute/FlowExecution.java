package Stepper.Flow.execute;

import Stepper.Flow.api.FlowDefinitionInterface;

import java.time.Duration;

public class FlowExecution {

    private final FlowDefinitionInterface flowDefinition;
    private final String id;

    private Duration totalTime;
    private FlowStatus status;

    public FlowExecution(FlowDefinitionInterface flowDefinition, String id){
        this.flowDefinition = flowDefinition;
        this.id = id;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public FlowDefinitionInterface getFlowDefinition() {
        return flowDefinition;
    }

    public String getId() {
        return id;
    }

    public FlowStatus getStatus() {
        return status;
    }
}
