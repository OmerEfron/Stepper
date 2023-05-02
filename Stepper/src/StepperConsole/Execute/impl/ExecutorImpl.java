package StepperConsole.Execute.impl;

import StepperConsole.Execute.Flow.api.ConsoleFlowExecutor;
import StepperConsole.Execute.Flow.api.FlowExecutionData;
import StepperConsole.Execute.Flow.impl.ConsoleFlowExecutorImpl;
import StepperConsole.Execute.Flow.impl.FlowExecutionDataImpl;
import StepperConsole.Execute.Flow.impl.FlowExecutionStatus;
import StepperConsole.Execute.api.Executor;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Stepper;
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
    public Optional<FlowExecutionData> executeFlow(String flowName, InputFromUser inputFromUser) {
        FlowExecution flowExecution=stepper.getFlowExecution(flowName);
        ConsoleFlowExecutor flowExecutor=new ConsoleFlowExecutorImpl(flowExecution, inputFromUser);
        if(flowExecutor.stratExcuteFlow()== FlowExecutionStatus.START) {
            stepper.ExecuteFlow(flowExecution);
            //printFlowExecutionData(flowExecution);
        }
        return FlowExecutionDataImpl.newInstance(flowExecution);
    }



    private void printFlowExecutionData(FlowExecution flowExecution){
        System.out.println("The flow "+flowExecution.getFlowDefinition().getName()+" done to execute.\n"
        +"His unique identity: "+flowExecution.getUUID()
                +"\nExecute status: "+flowExecution.getFlowExecutionResult().name());
        printFormalOutputs(flowExecution);

    }
    private void printFormalOutputs(FlowExecution flowExecution){
        Map<String, Pair<DataDefinitionsDeclaration, String>> formalOuputs = flowExecution.getFlowDefinition().getFormalOuputs();
        if(formalOuputs.isEmpty()){
            System.out.println("No formal outputs.");
        }
        else {
            System.out.println("The flow formal outputs are:");
            for (String outputName : formalOuputs.keySet()) {
                System.out.println(formalOuputs.get(outputName).getKey().userString());
                Object o = flowExecution.getOneOutput(outputName, formalOuputs.get(outputName)
                        .getKey().dataDefinition().getType());
                if (o == null)
                    System.out.println("Not created due to failure in flow");
                else
                    System.out.println(o);
            }
        }
    }


}
