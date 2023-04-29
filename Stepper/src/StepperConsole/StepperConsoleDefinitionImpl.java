package StepperConsole;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.api.StepperReader;
import Stepper.ReadStepper.impl.StepperReaderFromXml;
import Stepper.Stepper;
import StepperConsole.Execute.Executor;
import StepperConsole.Execute.ExecutorImpl;
import StepperConsole.Execute.Flow.FlowExecutionData;
import StepperConsole.Flow.ShowFlow;
import StepperConsole.Scanner.InputFromUser;
import StepperConsole.Scanner.InputFromUserImpl;

import java.util.*;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private Stepper stepper;
    private final InputFromUser inputFromUser=new InputFromUserImpl();

    private final List<FlowExecutionData> flowExecutions = new ArrayList<>();


    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        stepperConsoleDefinition.run();

    }
    @Override
    public void load() {
        System.out.println("Please enter file path to load an xml application-wise file.\n" +
                "Make sure it's a valid .xml file!");
        String filePath = getFilePath();
        try {
            stepper = getStepper(new StepperReaderFromXml(), filePath);
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Stepper has been loaded successfully!");
    }

    private static Stepper getStepper(StepperReader reader, String filePath){
        TheStepper stStepper = null;
        try {
            stStepper = reader.read(filePath);
            return new Stepper(stStepper);
        } catch (ReadException | FlowBuildException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFilePath() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void run() {
        ConsoleStatus consoleStatus = ConsoleStatus.RUN;
        while(consoleStatus == ConsoleStatus.RUN){
            System.out.println("Welcome to Stepper. A place that all of your steps connects in a glorious way to one" +
                    "marvelous flow.\nPlease choose what you would like to do:\n1. Load file\n2. Show flow details\n" +
                    "3.Execute flow\n4.Watch flow execution history\n5. Watch stats about flow executions.\n6. Exit");
            Integer choice = inputFromUser.getInt();
            switch (choice) {
                case 1:load(); break;
                case 2:showFlowDetails(); break;
                case 3:executeFlow();break;
                case 4:showExecuteHistory();break;
                case 5:showStats();break;
                default: consoleStatus = ConsoleStatus.EXIT;break;
            }
        }

    }


    @Override
    public void showFlowDetails() {
        System.out.println("Please choose the name of the Flow to show:\n");
        stepper.getNamesOfFlowsToPrint();
    }

    @Override
    public void executeFlow() {
        Executor executor=new ExecutorImpl(stepper);
        Optional<FlowExecutionData> optionalFlowExecutionData = executor.executeFlow(inputFromUser);
        optionalFlowExecutionData.ifPresent(flowExecutions::add);
    }

    @Override
    public void showExecuteHistory() {

    }

    @Override
    public void showStats() {

    }

    @Override
    public void exit() {

    }
}
