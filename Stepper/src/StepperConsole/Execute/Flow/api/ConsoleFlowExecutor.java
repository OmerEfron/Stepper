package StepperConsole.Execute.Flow.api;

import StepperConsole.Execute.Flow.impl.FlowExecutionStatus;

public interface ConsoleFlowExecutor {

  FlowExecutionStatus stratExcuteFlow();
  void doneExcuteFlow();
}
