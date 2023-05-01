package StepperConsole.Execute;

import Stepper.Flow.execute.FlowExecution;
import Stepper.Step.api.DataDefinitionsDeclaration;
import Stepper.Stepper;
import StepperConsole.Execute.Flow.*;
import StepperConsole.Scanner.InputFromUser;
import javafx.util.Pair;

import java.util.Map;
import java.util.Optional;

public class ExecutorImpl implements Executor {
    private Stepper stepper;

    public ExecutorImpl(Stepper stepper) {
        this.stepper = stepper;
    }

    @Override
    public Optional<FlowExecutionData> executeFlow(InputFromUser inputFromUser) {
        FlowExecution flowExecution=stepper.getFlowExecution(getFlowNumber(inputFromUser));
        ConsoleFlowExecutor flowExecutor=new ConsoleFlowExecutorImpl(flowExecution, inputFromUser);
        if(flowExecutor.stratExcuteFlow()== FlowExecutionStatus.START) {
            stepper.ExecuteFlow(flowExecution);
            printFlowExecutionData(flowExecution);
        }
        return FlowExecutionDataImpl.newInstance(flowExecution);
    }

    private int getFlowNumber(InputFromUser inputFromUser) {
        System.out.println("The Flow's that in the system :");
        System.out.println(stepper.getNamesOfFlowsToPrint());
        System.out.println("Please choose the number of the flow that you want do execute");
        int choose =inputFromUser.getIntByRange(stepper.getNumOfFlows());
        while(!stepper.isFlowExsitByNumber(choose)){
            System.out.println("\nThe flow number doesn't exsit");
            System.out.println("The Flow's that in the system :");
            System.out.println(stepper.getNamesOfFlowsToPrint());
            System.out.println("Please choose the number of the flow that you want do execute");
            choose =inputFromUser.getIntByRange(stepper.getNumOfFlows());
        }
        return choose;
    }

    private void printFlowExecutionData(FlowExecution flowExecution){
        System.out.println("The flow "+flowExecution.getFlowDefinition().getName()+" done to execute.\n"
        +"His unique identity: "+flowExecution.getUUID()
                +"\nExecute status: "+flowExecution.getFlowExecutionResult().name());
        printFormalOutputs(flowExecution);

    }
    private void printFormalOutputs(FlowExecution flowExecution){
        System.out.println("The flow formal outputs are:");
        Map<String, Pair<DataDefinitionsDeclaration, String>> formalOuputs = flowExecution.getFlowDefinition().getFormalOuputs();
        for (String outputName:formalOuputs.keySet()){
            System.out.println(formalOuputs.get(outputName).getKey().userString());
            Object o=flowExecution.getOneOutput(outputName,formalOuputs.get(outputName)
                    .getKey().dataDefinition().getType());
            if(o== null)
                System.out.println("Not created due to failure in flow");
            else
                System.out.println(o);
        }
    }


}
