package Stepper.Flow.execute.runner;

import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.FlowStatus;
import Stepper.Flow.execute.StepData.StepExecuteData;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Flow.execute.context.StepExecutionContextClass;
import Stepper.Step.api.StepStatus;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FlowExecutor {

    public void executeFlow(FlowExecution currFlow) {
        StepExecutionContext stepExecutionContext = new StepExecutionContextClass(currFlow);
        FlowStatus flowStatus = FlowStatus.SUCCESS;
        Instant start = updateTime(currFlow);

        for (StepUsageDeclerationInterface step : currFlow.getFlowDefinition().getSteps()) {
            stepExecutionContext.updateCustomMap(step);
            stepExecutionContext.addStepData(step);
            Instant stepStart= Instant.now();
            StepStatus stepStatus = step.getStepDefinition().invoke(stepExecutionContext, step.getNameToAlias(),step.getStepFinalName());
            Instant stepEnd=Instant.now();
            if (stepStatus == StepStatus.FAIL && !step.skipIfFail()) {
                flowStatus=FlowStatus.FAIL;
                break;
            } else if (stepStatus == StepStatus.WARNING) {
                flowStatus = FlowStatus.WARNING;
            }
            stepExecutionContext.setTotalTime(step.getStepFinalName(),Duration.between(stepStart,stepEnd));
        }

        //todo:update flow data
        currFlow.setTotalTime(Duration.between(start, Instant.now()));
        currFlow.createUUID();
        currFlow.setFlowStatus(flowStatus);
        stepExecutionContext.addFormalOutput(currFlow);
        currFlow.setStepsData(stepExecutionContext.getStepsData());
    }



    private static Instant updateTime(FlowExecution currFlow) {
        Instant start = Instant.now();
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        currFlow.setFormattedStartTime(currentTime.format(formatter));
        return start;
    }
}
