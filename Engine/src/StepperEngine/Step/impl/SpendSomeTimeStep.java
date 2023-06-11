package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.context.StepExecutionContext2;
import StepperEngine.Step.api.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class SpendSomeTimeStep extends StepDefinitionAbstract {
    public SpendSomeTimeStep(){
        super("Spend Some Time", true);
        this.addInput(new DataDefinitionDeclarationImpl("TIME_TO_SPEND","Total sleeping time (sec)", DataNecessity.MANDATORY, DataDefinitionRegistry.NUMBER));
    }
    /***
     *Sleep operation for a time limited in seconds.
     * @param context -interface that saves all system data
     * @param nameToData -Map of the name of the information definition to the name of the information in the current flow
     * @param step - The step name in the flow
     */
    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step)  {
        Instant start = Instant.now();
        Integer timeToSleep= context.getDataValue(nameToData.get("TIME_TO_SPEND"), Integer.class);
        if(timeToSleep == null || timeToSleep <= 0){
            context.setInvokeSummery(step,"Cannot sleep if the time is less then 1 !!!");
            context.setStepStatus(step,StepStatus.FAIL);
            return StepStatus.FAIL;
        }
        context.addLog(step,"About to sleep for "+timeToSleep.toString()+" seconds…");
        try {
            Thread.sleep(timeToSleep*1000);
            context.addLog(step,"Done sleeping…");
        } catch (InterruptedException e) {
            context.setInvokeSummery(step,e.getMessage());
            context.setStepStatus(step,StepStatus.FAIL);
            return StepStatus.FAIL;
        }
        context.setInvokeSummery(step,"The step "+step+", performed a sleep for "+timeToSleep*1000+
                " milliseconds.");
        context.setStepStatus(step,StepStatus.SUCCESS);
        context.setTotalTime(step,Duration.between(start, Instant.now()));
        return StepStatus.SUCCESS;
    }
}
