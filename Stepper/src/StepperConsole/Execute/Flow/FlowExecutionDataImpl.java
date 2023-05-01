package StepperConsole.Execute.Flow;

import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.FlowStatus;

import java.util.*;

public class FlowExecutionDataImpl implements FlowExecutionData{


    private final String flowName;
    private final String uuid;
    private final String executionTime;
    private final String executionResult;
    private final Long executionDuration;

    private final Set<IOData> freeInputs = new HashSet<>();


    public FlowExecutionDataImpl(FlowExecution flowExecution){
        flowName = flowExecution.getFlowDefinition().getName();
        uuid = flowExecution.getUUID();
        executionTime = flowExecution.getTotalTimeInFormat();
        executionDuration = flowExecution.getTotalTime().toMillis();
        executionResult = FlowStatus.getAsString(flowExecution.getFlowExecutionResult());
        setFreeInputs(flowExecution);

    }

    public static Optional<FlowExecutionData> newInstance(FlowExecution flowExecution) {
        if (flowExecution.hasExecuted()) {
            return Optional.of(new FlowExecutionDataImpl(flowExecution));
        } else {
            return Optional.empty();
        }
    }

    private void setFreeInputs(FlowExecution flowExecution) {
        flowExecution.getFreeInputs().stream()
                .map(data -> {
                    Object value = Optional.ofNullable(flowExecution.getFreeInputsValue().get(data.getAliasName()))
                            .orElse("not provided");
                    return new IOData(true, data.getAliasName(), data.dataDefinition().getType().getSimpleName(),
                            value.toString(), String.valueOf(data.necessity()));
                })
                .forEach(freeInputs::add);
    }

    @Override
    public Set<IOData> getFreeInputs() {
        return freeInputs;
    }

    @Override
    public String getFlowName() {
        return flowName;
    }

    @Override
    public String getUniqueExecutionId() {
        return uuid;
    }

    @Override
    public String getExecutionTime() {
        return executionTime;
    }

    @Override
    public String getFlowExecutionFinalResult() {
        return executionResult;
    }

    @Override
    public Long getFlowExecutionDuration() {
        return executionDuration;
    }





}

