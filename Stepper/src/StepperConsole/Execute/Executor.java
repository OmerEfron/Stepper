package StepperConsole.Execute;

import StepperConsole.Execute.Flow.FlowExecutionData;
import StepperConsole.Execute.Flow.FlowExecutionDataImpl;
import StepperConsole.Scanner.InputFromUser;

import java.util.Optional;

public interface Executor {
    Optional<FlowExecutionData> executeFlow(InputFromUser inputFromUser);
}
