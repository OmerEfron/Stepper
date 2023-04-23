package StepperConsole.Flow;

import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Step.api.DataDefinitionsDeclaration;

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
        //this.showAllOutputs();
    }


    private void showFlowName() {
        System.out.println("The Flow name is: " + flow.getName());
    }

    private void showFlowDescription() {
        System.out.println(flow.getName() + " description is: "+ flow.getDescription());
    }


   private void showFormalOutputs() {
        System.out.println("\nThe formal flow's output's are:");
        System.out.println(flow.outputStrings());
    }


    private void showIsReadOnlyFlow() {
        System.out.println("\nThe Flow " + flow.getName() + (flow.isReadOnlyFlow() ? " is read only" : " is not read only"));
    }


    private void showSteps() {
        int i=1;
        System.out.println("\nThe steps in the flow "+flow.getName()+ " are:");
        for (StepUsageDeclerationInterface step: flow.getSteps()){
            System.out.println(i+"."+ (step.getStepDefinition().getName().equals(step.getStepFinalName())
                    ? step.getStepDefinition().getName()
                    : "Original step name: "+step.getStepDefinition().getName()+", is alias name: "+step.getStepFinalName()));
            System.out.println("The step is "+ (step.isReadOnlyStep() ? "read only" : "not read only"));
            i++;
        }

    }

    private void showFreeInputs() {
        System.out.println("\nThe free input's are:");
        Map<DataDefinitionsDeclaration, List<String>> freeInputs=flow.getFreeInputsWithOptional();
        for(DataDefinitionsDeclaration dataDefinitionsDeclaration: freeInputs.keySet()){
            System.out.println("The input is:\n"+ dataDefinitionsDeclaration.getAliasName()+", is type "
                    +dataDefinitionsDeclaration.dataDefinition().getType().getSimpleName()+
                    ", is necessity "+dataDefinitionsDeclaration.necessity().toString()+
                    ", the steps that related to him :");
            for(String stepName:freeInputs.get(dataDefinitionsDeclaration)){
                System.out.println(stepName);

            }
        }
    }

//    private void showAllOutputs() {
//        System.out.println("\nThe output's are:");
//        Map<DataDefinitionsDeclaration, String> outputs=flow.getAllOutputs();
//        for(DataDefinitionsDeclaration dataDefinitionsDeclaration: outputs.keySet()){
//            System.out.println("The output is:\n"+ dataDefinitionsDeclaration.getAliasName()+", is type "
//                    +dataDefinitionsDeclaration.dataDefinition().getType().getSimpleName()+
//                    ", the steps that related to him: "+outputs.get(dataDefinitionsDeclaration));
//        }
//    }
}
