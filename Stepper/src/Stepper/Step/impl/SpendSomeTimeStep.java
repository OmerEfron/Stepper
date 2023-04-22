package Stepper.Step.impl;

import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.util.Map;

public class SpendSomeTimeStep extends StepDefinitionAbstractClass {
    public SpendSomeTimeStep(){
        super("Spend Some Time", true);
        this.addInput(new DataDefinitionDeclarationImpl("TIME_TO_SPEND","Total sleeping time (sec)", DataNecessity.MANDATORY, DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias)  {
        Integer timeToSleep= context.getDataValue(nameToAlias.get("TIME_TO_SPEND"), Integer.class);
        if(timeToSleep == null || timeToSleep <= 0){
            System.out.println("Cannot sleep if the time is less then 1 !!!");
            return StepStatus.FAIL;
        }
        System.out.println("About to sleep for "+timeToSleep.toString()+" seconds…");
        try {
            Thread.sleep(timeToSleep*1000);
            System.out.println("Done sleeping…");
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            return StepStatus.FAIL;
        }
        return StepStatus.SUCCESS;
    }
}
