package StepperEngine.Step;

import StepperEngine.Step.api.StepDefinitionInterface;

public class StepBuilder  {


    public StepDefinitionInterface getStepInstance(String name){
        try {
            Class<?> stepClass = Class.forName(name);
            Object newStep = stepClass.newInstance();
            return (StepDefinitionInterface)newStep;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
