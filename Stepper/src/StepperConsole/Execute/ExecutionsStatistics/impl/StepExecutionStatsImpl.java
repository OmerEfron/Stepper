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

    public StepExecutionStatsImpl(String stepName, FlowExecutionsCollector flowExecutionsCollector){
        this.stepName = stepName;
        long totalTimeCount = 0L;
        int exeCount = 0;
        for(FlowExecutionData flowExecutionData: flowExecutionsCollector.getFlowExecutionDataMap().values()){
            exeCount++;
            for(StepExecuteData stepExecuteData: flowExecutionData.getStepExecuteDataList()){
                if (stepExecuteData.getFinalName().equals(stepName) && stepExecuteData.getStepStatus() != StepStatus.NOT_INVOKED){

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
