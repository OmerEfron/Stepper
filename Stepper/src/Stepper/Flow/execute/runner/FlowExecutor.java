package Stepper.Flow.execute.runner;

import Stepper.Flow.api.StepUsageDeclerationClass;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Flow.execute.context.StepExecutionContextClass;

public class FlowExecutor {

    public void executeFlow(FlowExecution currFlow){
        StepExecutionContext stepExecutionContext=new StepExecutionContextClass(currFlow.getFlowDefinition());
        for(StepUsageDeclerationInterface step:currFlow.getFlowDefinition().getSteps()){
            //
        }
        Integer val=4;
        stepExecutionContext.storeValue("TIME_TO_SPEND",val);
        StepUsageDeclerationInterface step=currFlow.getFlowDefinition().getSteps().get(0);
        step.getStepDefinition().invoke(stepExecutionContext);
    }
}
