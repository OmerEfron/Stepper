package StepperConsole.Execute.impl;

import StepperConsole.Execute.Flow.api.ConsoleFlowExecutor;
import StepperEngine.DTO.FlowExecutionData.api.FlowExecutionData;
import StepperConsole.Execute.Flow.impl.ConsoleFlowExecutorImpl;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperConsole.Execute.Flow.impl.FlowExecutionStatus;
import StepperConsole.Execute.api.Executor;
import StepperEngine.Flow.execute.ExecutionNotReadyException;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Stepper;
import StepperConsole.Scanner.InputFromUser;

import java.util.Optional;

/**
 * A class that is in-charge of the execution connection between the engine and the console.
 * holds a stepper engine, and it can execute a lot of flows one by one, without need to create a new instance
 * of itself.
 */
public class ExecutorImpl implements Executor {
    private final Stepper stepper;

    public ExecutorImpl(Stepper stepper) {

        this.stepper = stepper;
    }

    /**
     * executes the flow matching to the name received.
     * @param flowName - the flow name to execute
     * @param inputFromUser - an instance of InputFromUser to get from user the inputs for the execution.
     * @return and Optional FlowExecutionData that holds all the data of the execution.
     */
    @Override
    public Optional<FlowExecutionData> executeFlow(String flowName, InputFromUser inputFromUser) {
        FlowExecution flowExecution=stepper.getFlowExecution(flowName);
        ConsoleFlowExecutor flowExecutor=new ConsoleFlowExecutorImpl(flowExecution, inputFromUser);
        if(flowExecutor.startExecuteFlow()== FlowExecutionStatus.START) {
            try {
                stepper.executeFlow(flowExecution.getUUID());
            } catch (ExecutionNotReadyException e) {
                throw new RuntimeException(e);
            }
        }
        return FlowExecutionDataImpl.newInstance(flowExecution);
    }



}
