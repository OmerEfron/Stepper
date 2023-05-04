package StepperConsole.Execute.Flow.impl;

import StepperConsole.Execute.Flow.api.ConsoleFlowExecutor;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.DataNecessity;
import StepperConsole.Scanner.InputFromUser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * this class is in-charge of getting the execution ready to execute.
 * gets from the user the inputs, makes sure that all the mandatory inputs has been given.
 */
public class ConsoleFlowExecutorImpl implements ConsoleFlowExecutor {
    private final int TO_MENU=1;
    private final int FREE_INPUTS=2;
    private final int EXECUTE=3;

    private final FlowExecution flowExecution;
    private final InputFromUser inputFromUser;


    public ConsoleFlowExecutorImpl(FlowExecution flowExecution, InputFromUser inputFromUser) {
        this.flowExecution = flowExecution;
        this.inputFromUser=inputFromUser;
    }

    /**
     * starting the execution by running the execution menu.
     * @return the status of the execution. START if all mandatory inputs inserted and the user asks to execute.
     *                                      GIVEN_UP if the user decided to give up on this execution.
     *
     */
    @Override
    public FlowExecutionStatus startExecuteFlow() {
        System.out.println("Starting execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getId() + "]");
        return executeMenu();
    }
    @Override
    public void doneExecuteFlow() {
        System.out.println("End execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getId() + "]. Status: " + flowExecution.getFlowExecutionResult());
    }
    private FlowExecutionStatus executeMenu(){
        FlowExecutionStatus flowExecutionStatus=FlowExecutionStatus.NOT_READY;
        List<DataDefinitionsDeclaration> freeInputs= new ArrayList<>(flowExecution.getFlowDefinition().getFreeInputsWithOptional()
                .keySet());
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


    /**
     * shows all the free inputs of the flow, and update the user's desired one.
     * @param freeInputs a list of free inputs
     * @param mandatoryInputs a list of the mandatory free inputs.
     */
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

    /**
     * shows the options for the user to get the flow ready to execute.
     * @return the int value of the user's choice
     */
    private int showMenu(){
        String menu="What would you like to accomplish? (enter the number of your choice)\n" +
                "1.Return to the main menu.\n"+"2.Update the free inputs.\n"
                +"3.Execute "+flowExecution.getFlowDefinition().getName();
        System.out.println(menu);
        int input=inputFromUser.getIntByRange(3);
        while (input<1 || input>3 ){
            System.out.println("The number is not between 1 to 3 \nplease try again\n"+menu);
            input=inputFromUser.getIntByRange(3);
        }
        return input;
    }

    /**
     * filters free inputs to mandatory only
     * @param freeinputs the free inputs of a flow
     * @return list of free mandatory inputs.
     */
    private List<DataDefinitionsDeclaration> getMandatoryInputs(List<DataDefinitionsDeclaration> freeinputs){
        return freeinputs.stream()
                .filter(dataDefinitionsDeclaration -> dataDefinitionsDeclaration.necessity()== DataNecessity.MANDATORY)
                .collect(Collectors.toList());
    }


    /**
     * gets from user the input he wants and stores it.
     * @param freeInputs the free inputs in the flow
     * @param mandatoryInputs the mandatory free inputs in the flow.
     */
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
            value = inputFromUser.getString();
        }
        flowExecution.addFreeInput(dataDefinitionsDeclaration.getAliasName(), value);
        mandatoryInputs.remove(dataDefinitionsDeclaration);
    }

}
