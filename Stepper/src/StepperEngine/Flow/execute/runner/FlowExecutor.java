package StepperEngine.Flow.execute.runner;

import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Flow.execute.FlowStatus;
import StepperEngine.Flow.execute.context.StepExecutionContext;
import StepperEngine.Flow.execute.context.StepExecutionContextClass;
import StepperEngine.Step.api.StepStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/***
 * The purpose of the class is to execute different flows each time
 */
public class FlowExecutor {
    /***
     *The function receives an instance of flow that needs to be executed.
     * As part of the execution, we execute each step.
     * @param currFlow flow execution data wich we want to execute
     */
    public void executeFlow(FlowExecution currFlow) {
        StepExecutionContext stepExecutionContext = new StepExecutionContextClass(currFlow);
        FlowStatus flowStatus = FlowStatus.SUCCESS;
        Instant start = updateTime(currFlow);
        /*
        execute each step in order .
         */
        for (StepUsageDecleration step : currFlow.getFlowDefinition().getSteps()) {
            stepExecutionContext.updateCustomMap(step);
            stepExecutionContext.addStepData(step);
            Instant stepStart = Instant.now();

            StepStatus stepStatus = invokeStep(stepExecutionContext, step);
            Instant stepEnd = Instant.now();
            setStepTotalTime(stepExecutionContext, step, stepStart, stepEnd);
            if (stepStatus == StepStatus.FAIL && !step.skipIfFail()) {// if the step failed all the flow his failed and we need to stop.
                flowStatus = FlowStatus.FAIL;
                break;
            }
            else if (stepStatus == StepStatus.WARNING) {
                flowStatus = FlowStatus.WARNING;
            }
        }

        finishExecution(currFlow, stepExecutionContext, flowStatus, start);
    }

    /***
     * Updates the results of the flow run
     * @param currFlow
     * @param stepExecutionContext
     * @param flowStatus
     * @param start
     */
    private static void finishExecution(FlowExecution currFlow, StepExecutionContext stepExecutionContext, FlowStatus flowStatus, Instant start) {
        currFlow.setTotalTime(Duration.between(start, Instant.now()));
        currFlow.createUUID();
        currFlow.setFlowStatus(flowStatus);
        stepExecutionContext.addFormalOutput(currFlow);
        currFlow.setStepsData(stepExecutionContext.getStepsData());
    }

    private static StepStatus invokeStep(StepExecutionContext stepExecutionContext, StepUsageDecleration step) {
        StepStatus stepStatus = step.getStepDefinition().invoke(stepExecutionContext, step.getNameToAlias(), step.getStepFinalName());
        return stepStatus;
    }

    private static void setStepTotalTime(StepExecutionContext stepExecutionContext, StepUsageDecleration step, Instant stepStart, Instant stepEnd) {
        stepExecutionContext.setTotalTime(step.getStepFinalName(), Duration.between(stepStart, stepEnd));
    }


    private static Instant updateTime(FlowExecution currFlow) {
        Instant start = Instant.now();
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        currFlow.setFormattedStartTime(currentTime.format(formatter));
        return start;
    }
}
