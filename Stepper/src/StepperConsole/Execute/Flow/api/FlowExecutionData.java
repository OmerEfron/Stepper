package StepperConsole.Execute.Flow.api;

import StepperConsole.Execute.Flow.impl.IOData;
import StepperEngine.Flow.execute.StepData.StepExecuteData;

import java.util.List;
import java.util.Set;

public interface FlowExecutionData {


    String getFlowName();
    String getUniqueExecutionId();
    String getExecutionTime();

    List<StepExecuteData> getStepExecuteDataList();

    String getFlowExecutionFinalResult();

    Long getFlowExecutionDuration();

    Set<IOData> getFreeInputs();

    Set<IOData> getOutputs();

    Set<IOData> getFormalOutputs();
}
