package StepperConsole;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.impl.StepperReaderFromXml;
import Stepper.Stepper;
import StepperConsole.Execute.Executor;
import StepperConsole.Execute.ExecutorImpl;
import StepperConsole.Execute.Flow.FlowExecutionData;
import StepperConsole.Execute.Flow.FlowExecutionDataImpl;
import StepperConsole.Flow.ShowFlow;
import StepperConsole.Scanner.InputFromUser;
import StepperConsole.Scanner.InputFromUserImpl;

import java.util.*;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private final Stepper stepper = new Stepper();
    private final InputFromUser inputFromUser=new InputFromUserImpl();

    private final Map<String,List<FlowExecutionData>> flowExecutions = new HashMap<>();


    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        int choose;
        do {
            choose=stepperConsoleDefinition.chooseMenu();
            stepperConsoleDefinition.doCommand(choose);
        }while (choose != 0);
        stepperConsoleDefinition.executeFlow();

    }
    @Override
    public boolean load() {
        System.out.println("Please enter file path to load an xml application-wise file.\n" +
                "Make sure it's a valid .xml file!");
        String filePath = getFilePath();
        TheStepper stStepper = null;

        try {
            stStepper = new StepperReaderFromXml().read(filePath);
            stepper.newFlows(stStepper);
        } catch (ReadException | FlowBuildException e ) {
            System.out.println(e.getMessage());
            return false;
        }

        System.out.println("Stepper has been loaded successfully!");
        return true;
    }


    private static String getFilePath() {
        Scanner scanner = new Scanner(System.in);
        String filePath=scanner.nextLine();
        return filePath;
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
        ShowFlow showFlow = stepper.showFlowByNumber(getFlowNumber());
        showFlow.showFlowDetails();
    }
    private int getFlowNumber() {
        System.out.println("The Flow's that in the system :");
        System.out.println(stepper.getNamesOfFlowsToPrint());
        System.out.println("Please choose the number of the flow");
        int choose =inputFromUser.getInt();
        while(!stepper.isFlowExsitByNumber(choose)){
            System.out.println("The flow number doesn't exsit");
            System.out.println("The Flow's that in the system :");
            System.out.println(stepper.getNamesOfFlowsToPrint());
            System.out.println("Please choose the number of the flow");
            choose =inputFromUser.getInt();
        }
        return choose;
    }

    @Override
    public void executeFlow() {
        Executor executor = new ExecutorImpl(stepper);
        executor.executeFlow(inputFromUser).ifPresent(flowExecutionData ->
                flowExecutions.computeIfAbsent(flowExecutionData.getFlowName(), k -> new ArrayList<>())
                        .add(flowExecutionData)
        );
    }

    @Override
    public void showExecuteHistory() {
        System.out.println("Please choose which flow you want to see an execution of it:\n");
        Integer choice = getFlowNumber();
        String flowName = stepper.flowNameByNumber(choice);
        List<FlowExecutionData> flowExecutionDataList = flowExecutions.get(flowName);

        for(FlowExecutionData flowExecutionData:flowExecutionDataList){
            showAFlowExecutionHistory(flowExecutionData);
            System.out.println("---------------------------------");
        }

    }

    private static void showAFlowExecutionHistory(FlowExecutionData flowExecutionData) {
        System.out.println("UUID: "+ flowExecutionData.getUniqueExecutionId());
        System.out.println("Name: " + flowExecutionData.getFlowName());
        System.out.println("execution final result: "+ flowExecutionData.getFlowExecutionFinalResult());
        System.out.println("Duration (in milis): "+ flowExecutionData.getFlowExecutionDuration());
        flowExecutionData.getFreeInputs()
                .forEach(data -> {
                        System.out.println(data.getName());
                        System.out.println(data.getContent());
                        System.out.println(data.getType());
                        System.out.println(data.getNecessity());
                }
                );
    }


    @Override
    public void showStats() {

    }

    @Override
    public void exit() {

    }

    @Override
    public int chooseMenu() {
        int choose;
        do {
            System.out.println("\nPlease choose which action you would like to perform");
            System.out.println("1. Load new file to the system\n2.Introducing the Flow definition.\n" +
                    "3.Flow activation (Execution).\n" +
                    "4.Displaying full details of past flow execution.\n" +
                    "5.Statistics on the flow execution's that have happened in the system until now.\n" +
                    "6.exit.");
            choose = inputFromUser.getInt();
            if(choose<1 || choose>6)
                System.out.println(choose+" is not in range.\nchoose number between 1 to 6.");
        }while (choose<1 || choose>6);

        return choose;
    }

    @Override
    public void doCommand(int choose) {
        switch (choose){
            case (1):
                this.load();
                break;
            case (2):
                this.showFlowDetails();
                break;
            case(3):
                this.executeFlow();
                break;
            case (4):
                this.showExecuteHistory();
                break;
            case (5):
                this.showStats();
                break;
            case(6):
                this.exit();
                break;
        }
    }
}
