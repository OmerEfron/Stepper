package StepperEngine.Flow.execute.runner;

import StepperEngine.Flow.api.StepUsageDeclerationInterface;
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

        for (StepUsageDeclerationInterface step : currFlow.getFlowDefinition().getSteps()) {
            stepExecutionContext.updateCustomMap(step);
            stepExecutionContext.addStepData(step);
            Instant stepStart = Instant.now();
            if(flowStatus != FlowStatus.FAIL) {
                StepStatus stepStatus = step.getStepDefinition().invoke(stepExecutionContext, step.getNameToAlias(), step.getStepFinalName());
                Instant stepEnd = Instant.now();
                if (stepStatus == StepStatus.FAIL && !step.skipIfFail()) {
                    flowStatus = FlowStatus.FAIL;
                    break;
                } else if (stepStatus == StepStatus.WARNING) {
                    flowStatus = FlowStatus.WARNING;
                }
                stepExecutionContext.setTotalTime(step.getStepFinalName(), Duration.between(stepStart, stepEnd));
            }
            else{
                stepExecutionContext.setStepStatus(step.getStepFinalName() ,StepStatus.NOT_INVOKED);
            }
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
