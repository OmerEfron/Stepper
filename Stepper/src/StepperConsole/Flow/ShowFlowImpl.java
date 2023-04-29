package StepperConsole.Flow;

import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Step.api.DataDefinitionsDeclaration;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ShowFlowImpl implements ShowFlow{
    private FlowDefinitionInterface flow;

    public ShowFlowImpl(FlowDefinitionInterface flow){
        this.flow=flow;
    }

    @Override
    public void showFlowDetails() {
        this.showFlowName();
        this.showFlowDescription();
        this.showFormalOutputs();
        this.showIsReadOnlyFlow();
        this.showSteps();
        this.showFreeInputs();
        this.showAllOutputs();
    }


    private void showFlowName() {
        System.out.println("\n-The Flow name is: " + flow.getName());
    }

    private void showFlowDescription() {
        System.out.println("-"+flow.getName() + " description is: "+ flow.getDescription());
    }


   private void showFormalOutputs() {
        System.out.println("-The formal flow's output's are:");
        System.out.println(flow.outputStrings());
    }


    private void showIsReadOnlyFlow() {
        System.out.println("-The Flow " + flow.getName() + (flow.isReadOnlyFlow() ? " is read only" : " is not read only"));
    }


    private void showSteps() {
        int i=1;
        System.out.println("-The steps in the flow "+flow.getName()+ " are:");
        for (StepUsageDeclerationInterface step: flow.getSteps()){
            System.out.println(i+"."+ (step.getStepDefinition().getName().equals(step.getStepFinalName())
                    ? step.getStepDefinition().getName()
                    : "Original step name: "+step.getStepDefinition().getName()+", is alias name: "+step.getStepFinalName())+
                    ", the step is "+ (step.isReadOnlyStep() ? "read only" : "not read only"));
            i++;
        }

    }

    private void showFreeInputs() {
        System.out.println("-The free input's are:");
        int i=1;
        Map<DataDefinitionsDeclaration, List<String>> freeInputs=flow.getFreeInputsWithOptional();
        for(DataDefinitionsDeclaration dataDefinitionsDeclaration: freeInputs.keySet()){
            System.out.println(i+".The input is: "+ dataDefinitionsDeclaration.getAliasName()+", is type "
                    +dataDefinitionsDeclaration.dataDefinition().getType().getSimpleName()+
                    ", is necessity "+dataDefinitionsDeclaration.necessity().toString()+
                    ", the steps that related to him :");
            for(String stepName:freeInputs.get(dataDefinitionsDeclaration)){
                System.out.println(stepName);
            }
            i++;
        }
    }

    private void showAllOutputs() {
        System.out.println("-The output's are:");
        int i=1;
        Map<String , Pair<DataDefinitionsDeclaration,String>> outputs=flow.getAllOutputs();
        for(String outputName:outputs.keySet()){
            System.out.println(i+"."+outputName+
                    ", his type is "+outputs.get(outputName).getKey().dataDefinition().getType().getSimpleName()
                    +", the step that related to him: "+outputs.get(outputName).getValue()+ ".");
            i++;
        }
    }
}
