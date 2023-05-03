package StepperConsole.Execute.ExecutionsStatistics.impl;

import StepperConsole.Execute.ExecutionsStatistics.api.FlowExecutionStatsDefinition;
import StepperConsole.Execute.ExecutionsStatistics.api.StepExecutionStats;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.StepStatus;
import StepperConsole.Execute.Flow.api.FlowExecutionData;
import StepperConsole.Execute.Flow.impl.FlowExecutionsCollector;

import java.util.ArrayList;
import java.util.List;

public class FlowExecutionStatsDefinitionImpl implements FlowExecutionStatsDefinition {

    private final String flowName;

    private final Integer numOfExecutions;
    private final Long avgTimeOfExecutions;

    private final List<StepExecutionStats> stepExecutionStatisticsList = new ArrayList<>();

    public FlowExecutionStatsDefinitionImpl(FlowExecutionsCollector flowExecutionsCollector){


        flowName = flowExecutionsCollector.getFlowName();
        numOfExecutions = flowExecutionsCollector.getNumOfExecutions();

        if(numOfExecutions > 0) {
            String firstExeName = flowExecutionsCollector.getFlowExecutionByNumber().get(1);
            List<StepExecuteData> stepExecuteDataList = flowExecutionsCollector.getFlowExecutionData(firstExeName).getStepExecuteDataList();
            stepExecuteDataList.stream()
                    .filter(stepExecuteData -> stepExecuteData.getStepStatus() != StepStatus.NOT_INVOKED)
                    .forEach(stepExecuteData -> stepExecutionStatisticsList.add(
                            new StepExecutionStatsImpl(stepExecuteData.getFinalName(), flowExecutionsCollector)));
            Long totalTime = 0L;
            for (FlowExecutionData flowExecutionData : flowExecutionsCollector.getFlowExecutionDataMap().values()) {
                totalTime += flowExecutionData.getFlowExecutionDuration();
            }
            avgTimeOfExecutions = totalTime / numOfExecutions;
        }
        else {
            avgTimeOfExecutions = 0L;
        }

    }
    @Override
    public String getFlowName() {
        return flowName;
    }

    @Override
    public Integer getNumOfExecutions() {
        return numOfExecutions;
    }

    @Override
    public Long getAvgTimeOfExecutions() {
        return avgTimeOfExecutions;
    }

    @Override
    public List<StepExecutionStats> getStepExecutionsStats() {
        return stepExecutionStatisticsList;
    }
}