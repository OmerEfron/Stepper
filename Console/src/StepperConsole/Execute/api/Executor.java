package StepperConsole.Execute.api;

import StepperEngine.FlowExecutionData.api.FlowExecutionData;
import StepperConsole.Scanner.InputFromUser;

import java.util.Optional;

public interface Executor {
    Optional<FlowExecutionData> executeFlow(String flowName, InputFromUser inputFromUser);
}
