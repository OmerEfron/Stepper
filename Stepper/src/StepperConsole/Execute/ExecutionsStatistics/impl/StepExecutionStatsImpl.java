package StepperConsole.Execute.ExecutionsStatistics.impl;

import StepperConsole.Execute.ExecutionsStatistics.api.StepExecutionStats;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.StepStatus;
import StepperConsole.Execute.Flow.api.FlowExecutionData;
import StepperConsole.Execute.Flow.impl.FlowExecutionsCollector;

public class StepExecutionStatsImpl implements StepExecutionStats {

    private final String stepName;

    private final Integer numOfExecutions;
    private final Long avgTimeOfExecutions;

    /**
     * builds a statistic information of a step from a flow, based on the current data in flowExecutionCollector
     * @param flowExecutionsCollector holds all the flow's history of executions
     * @param stepName holds the name of the step to evaluate the stats of it.
     */
    public StepExecutionStatsImpl(String stepName, FlowExecutionsCollector flowExecutionsCollector){
        this.stepName = stepName;
        long totalTimeCount = 0L;
        int exeCount = 0;
        for(FlowExecutionData flowExecutionData: flowExecutionsCollector.getFlowExecutionDataMap().values()){
            for(StepExecuteData stepExecuteData: flowExecutionData.getStepExecuteDataList()){
                if (stepExecuteData.getFinalName().equals(stepName) &&
                        stepExecuteData.getStepStatus() != StepStatus.NOT_INVOKED){
                    exeCount++;
                    totalTimeCount += stepExecuteData.getTotalTime().toMillis();
                }
            }
        }
        numOfExecutions = exeCount;
        if(exeCount != 0){
            avgTimeOfExecutions = totalTimeCount / exeCount;
        }
        else{
            avgTimeOfExecutions = 0L;
        }
    }
    @Override
    public String getStepName() {
        return stepName;
    }

    @Override
    public Integer getNumOfExecutions() {
        return numOfExecutions;
    }

    @Override
    public Long getAvgTimeOfExecutions() {
        return avgTimeOfExecutions;
    }
}
