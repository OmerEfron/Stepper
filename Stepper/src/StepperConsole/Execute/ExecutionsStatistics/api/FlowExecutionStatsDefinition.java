package StepperConsole.Execute.ExecutionsStatistics.api;

import StepperConsole.Execute.ExecutionsStatistics.api.StepExecutionStats;

import java.util.List;

public interface FlowExecutionStatsDefinition {

    Integer getNumOfExecutions();

    Long getAvgTimeOfExecutions();
    String getFlowName();

    List<StepExecutionStats> getStepExecutionsStats();


}
