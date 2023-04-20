package Stepper.Logic.ReadStepper;

import generated.STStepper;
import generated.STStepsInFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StepsInFlow {


    private List<StepInFlow> stepsInFlowList;


    public StepsInFlow(STStepsInFlow stStepsInFlow){
        stepsInFlowList = stStepsInFlow.getSTStepInFlow().stream()
                .map(element -> new StepInFlow(element))
                .collect(Collectors.toList());
    };

    public StepsInFlow() {
        this.stepsInFlowList = new ArrayList<>();
    }

    public void addStep(StepInFlow step){
        stepsInFlowList.add(step);
    }

    public List<StepInFlow> getStepsInFlowList(){
        return stepsInFlowList;
    }

}
