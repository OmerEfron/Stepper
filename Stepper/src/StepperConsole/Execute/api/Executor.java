package StepperConsole.Execute.api;

import StepperConsole.Execute.Flow.api.FlowExecutionData;
import StepperConsole.Scanner.InputFromUser;

import java.util.Optional;

public interface Executor {
    Optional<FlowExecutionData> executeFlow(String flowName, InputFromUser inputFromUser);
}
