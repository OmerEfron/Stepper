package StepperConsole.Execute.Flow.api;

import StepperConsole.Execute.Flow.impl.FlowExecutionStatus;

public interface ConsoleFlowExecutor {

  FlowExecutionStatus startExecuteFlow();
  void doneExecuteFlow();
}
