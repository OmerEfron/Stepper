package StepperConsole.Execute.Flow;

import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.StepData.StepExecuteData;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FlowExecutionData {


    String getFlowName();
    String getUniqueExecutionId();
    String getExecutionTime();

    String getFlowExecutionFinalResult();

    Long getFlowExecutionDuration();

    Set<IOData> getFreeInputs();


}
