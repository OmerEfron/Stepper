package StepperConsole.Execute.Flow.impl;

import StepperConsole.Execute.Flow.api.FlowExecutionData;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Flow.execute.FlowStatus;
import StepperEngine.Flow.execute.StepData.StepExecuteData;

import java.util.*;
import java.util.stream.Collectors;

public class FlowExecutionDataImpl implements FlowExecutionData {


    private final String flowName;
    private final String uuid;
    private final String executionTime;
    private final String executionResult;
    private final Long executionDuration;
    private final List<StepExecuteData> stepExecuteDataList;

    private final Set<IOData> freeInputs = new HashSet<>();
    private final Set<IOData> outputs = new HashSet<>();

    private final Set<IOData> formalOutputs;

    public FlowExecutionDataImpl(FlowExecution flowExecution){
        flowName = flowExecution.getFlowDefinition().getName();
        uuid = flowExecution.getUUID();
        executionTime = flowExecution.getTotalTimeInFormat();
        executionDuration = flowExecution.getTotalTime().toMillis();
        executionResult = FlowStatus.getAsString(flowExecution.getFlowExecutionResult());
        stepExecuteDataList = flowExecution.getStepsData();
        setFreeInputs(flowExecution);
        setOutputs(flowExecution);
        formalOutputs = getFormalOutputs(flowExecution);
    }

    private Set<IOData> getFormalOutputs(FlowExecution flowExecution) {
        final Set<IOData> formalOutputs;
        formalOutputs = outputs.stream()
                .filter(output -> flowExecution.getFormalOutputs().containsKey(output.getName()))
                .collect(Collectors.toSet());
        return formalOutputs;
    }

    public List<StepExecuteData> getStepExecuteDataList() {
        return stepExecuteDataList;
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
                    String content;
                    Object value = flowExecution.getInputValue(data.getAliasName(), data.dataDefinition().getType());
                    if (value == null){
                        content = "not provided";
                    }
                    else {
                        content = value.toString();
                    }
                    return new IOData(
                            false,
                            data.getAliasName(),
                            data.userString(),
                            data.dataDefinition().getName(),
                            content,
                            String.valueOf(data.necessity()));
                })
                .forEach(freeInputs::add);
    }
    private void setOutputs(FlowExecution flowExecution) {
        flowExecution.getOutputs().stream()
                .map(data -> {
                    String content;
                    Object value = flowExecution.getOneOutput(data.getAliasName(), data.dataDefinition().getType());
                    if (value == null){
                        content = "not provided";
                    }
                    else {
                        content = value.toString();
                    }
                    return new IOData(
                            true,
                            data.getAliasName(),
                            data.userString(),
                            data.dataDefinition().getName(),
                            content, String.valueOf(data.necessity()));
                })
                .forEach(outputs::add);
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

    @Override
    public Set<IOData> getOutputs() {
        return outputs;
    }

    @Override
    public Set<IOData> getFormalOutputs() {
        return formalOutputs;
    }
}

