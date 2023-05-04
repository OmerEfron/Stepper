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

public class FlowExecutor {

    public void executeFlow(FlowExecution currFlow) {
        StepExecutionContext stepExecutionContext = new StepExecutionContextClass(currFlow);
        FlowStatus flowStatus = FlowStatus.SUCCESS;
        Instant start = updateTime(currFlow);

        for (StepUsageDecleration step : currFlow.getFlowDefinition().getSteps()) {
            stepExecutionContext.updateCustomMap(step);
            stepExecutionContext.addStepData(step);
            Instant stepStart = Instant.now();

            StepStatus stepStatus = invokeStep(stepExecutionContext, step);
            Instant stepEnd = Instant.now();
            setStepTotalTime(stepExecutionContext, step, stepStart, stepEnd);
            if (stepStatus == StepStatus.FAIL && !step.skipIfFail()) {
                flowStatus = FlowStatus.FAIL;
                break;
            }
            else if (stepStatus == StepStatus.WARNING) {
                flowStatus = FlowStatus.WARNING;
            }
        }

        finishExecution(currFlow, stepExecutionContext, flowStatus, start);
    }

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
