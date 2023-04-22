package Stepper.Step;

import Stepper.Step.api.StepDefinitionInterface;

import java.util.function.Supplier;

public class StepBuilder  {


    public StepDefinitionInterface getStepInstance(String name){
        try {
            Class<?> stepClass = Class.forName(name);
            Object newStep = stepClass.newInstance();
            return (StepDefinitionInterface)newStep;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
