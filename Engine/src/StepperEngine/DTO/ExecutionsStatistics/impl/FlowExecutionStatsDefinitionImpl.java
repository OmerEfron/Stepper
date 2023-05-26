package StepperEngine.DTO.ExecutionsStatistics.impl;

import StepperEngine.DTO.FlowExecutionData.api.FlowExecutionData;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionsCollector;
import StepperEngine.DTO.ExecutionsStatistics.api.FlowExecutionStatsDefinition;
import StepperEngine.DTO.ExecutionsStatistics.api.StepExecutionStats;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.StepStatus;

import java.util.ArrayList;
import java.util.List;

public class FlowExecutionStatsDefinitionImpl implements FlowExecutionStatsDefinition {

    private final String flowName;

    private final Integer numOfExecutions;
    private final Long avgTimeOfExecutions;

    private final List<StepExecutionStats> stepExecutionStatisticsList = new ArrayList<>();

    /**
     * builds a statistic information based on the current data in flowExecutionCollector
     * @param flowExecutionsCollector holds all the flow's history of executions
     */
    public FlowExecutionStatsDefinitionImpl(FlowExecutionsCollector flowExecutionsCollector){

        flowName = flowExecutionsCollector.getFlowName();
        numOfExecutions = flowExecutionsCollector.getNumOfExecutions();

        if(numOfExecutions > 0) {
            String firstExeName = flowExecutionsCollector.getFlowExecutionByNumber().get(1);
            List<StepExecuteData> stepExecuteDataList = flowExecutionsCollector.getFlowExecutionData(firstExeName)
                    .getStepExecuteDataList(); // extract the steps data from the first instance.
            stepExecuteDataList.stream()
                    .filter(stepExecuteData -> stepExecuteData.getStepStatus() != StepStatus.NOT_INVOKED)
                    .forEach(stepExecuteData -> stepExecutionStatisticsList.add(
                            new StepExecutionStatsImpl(stepExecuteData.getFinalName(), flowExecutionsCollector)));
            Long totalTime = 0L;
            for (FlowExecutionData flowExecutionData : flowExecutionsCollector.getFlowExecutionDataMap().values()) {
                totalTime += flowExecutionData.getFlowExecutionDuration();
            }
            avgTimeOfExecutions = totalTime / numOfExecutions; // to get the average
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
