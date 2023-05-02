package StepperConsole.Execute.Flow.impl;

import StepperConsole.Execute.Flow.api.ConsoleFlowExecutor;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.DataNecessity;
import StepperConsole.Scanner.InputFromUser;

import java.util.*;
import java.util.stream.Collectors;

public class ConsoleFlowExecutorImpl implements ConsoleFlowExecutor {
    private final int TO_MENU=1;
    private final int FREE_INPUTS=2;

    private final int EXECUTE=3;

    private FlowExecution flowExecution;
    private InputFromUser inputFromUser;


    public ConsoleFlowExecutorImpl(FlowExecution flowExecution, InputFromUser inputFromUser) {
        this.flowExecution = flowExecution;
        this.inputFromUser=inputFromUser;
    }

    @Override
    public FlowExecutionStatus stratExcuteFlow() {
        System.out.println("Starting execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getId() + "]");
        return executeMenu();
    }
    @Override
    public void doneExcuteFlow() {
        System.out.println("End execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getId() + "]. Status: " + flowExecution.getFlowExecutionResult());
    }
    private FlowExecutionStatus executeMenu(){
        FlowExecutionStatus flowExecutionStatus=FlowExecutionStatus.NOT_READY;
        List<DataDefinitionsDeclaration> freeInputs=flowExecution.getFlowDefinition().getFreeInputsWithOptional()
                .keySet().stream().collect(Collectors.toList());
        List<DataDefinitionsDeclaration> mandatoryInputs=getMandatoryInputs(freeInputs);

        while (flowExecutionStatus==FlowExecutionStatus.NOT_READY) {
            int choose = showMenu();
            switch (choose) {
                case (TO_MENU):
                    flowExecutionStatus = FlowExecutionStatus.GIVEN_UP;
                    break;
                case (FREE_INPUTS):
                    showFreeInputs(freeInputs,mandatoryInputs);
                    break;
                case (EXECUTE):
                    if (!mandatoryInputs.isEmpty())
                        System.out.println("Cann't execute flow because there more mandatory inputs that you need to update.");
                    else
                        flowExecutionStatus = FlowExecutionStatus.START;
                    break;
            }
        }
        return flowExecutionStatus;
    }



    private void showFreeInputs(List<DataDefinitionsDeclaration> freeInputs,List<DataDefinitionsDeclaration> mandatoryInputs) {

        System.out.println("\nThe free input's are:");
        int i = 1;
        for (DataDefinitionsDeclaration dataDefinitionsDeclaration : freeInputs) {
            System.out.println(i + "." + dataDefinitionsDeclaration.userString() + "(" + dataDefinitionsDeclaration.getAliasName() + ")"
                    + ", is necessity " + dataDefinitionsDeclaration.necessity().toString()
                    + (mandatoryInputs.contains(dataDefinitionsDeclaration) ? " (need to update before execute)." : "."));
            i++;
        }

        updateFreeInputs(freeInputs,mandatoryInputs);
    }

    private int showMenu(){
        String menu="What would you like to accomplish? (enter the number of your choice)\n" +
                "1.Return to the main menu.\n"+"2.Update the free inputs.\n"
                +"3.Execute "+flowExecution.getFlowDefinition().getName();
        System.out.println(menu);
        Scanner scanner = new Scanner(System.in);
        int input=inputFromUser.getIntByRange(3);
        while (input<1 || input>3 ){
            System.out.println("The number is not between 1 to 3 \nplease try again\n"+menu);
            input=inputFromUser.getIntByRange(3);
        }
        return input;
    }
    private List<DataDefinitionsDeclaration> getMandatoryInputs(List<DataDefinitionsDeclaration> freeinputs){
        return freeinputs.stream()
                .filter(dataDefinitionsDeclaration -> dataDefinitionsDeclaration.necessity()== DataNecessity.MANDATORY)
                .collect(Collectors.toList());
    }

    public void updateFreeInputs(List<DataDefinitionsDeclaration> freeInputs,List<DataDefinitionsDeclaration> mandatoryInputs) {
        System.out.println("Please choose (by number) the input that you want to update :");

        int choose = inputFromUser.getInt();
        if(choose>freeInputs.size() || choose<1){
            System.out.println("There are no input in "+choose+".\nplease try again.");
            showFreeInputs(freeInputs,mandatoryInputs);
        }

        DataDefinitionsDeclaration dataDefinitionsDeclaration = freeInputs.get(choose - 1);
        System.out.println("Enter value for : " + dataDefinitionsDeclaration.userString());
        Object value;
        if (dataDefinitionsDeclaration.dataDefinition().getType().isAssignableFrom(Integer.class)) {
            value = inputFromUser.getInt();
        } else if (dataDefinitionsDeclaration.dataDefinition().getType().isAssignableFrom(Integer.class)) {
            value = inputFromUser.getDouble();
        } else {
            inputFromUser.cleanBuffer();
            value = inputFromUser.getString();
        }
        flowExecution.addFreeInput(dataDefinitionsDeclaration.getAliasName(), value);
        mandatoryInputs.remove(dataDefinitionsDeclaration);
    }

}
