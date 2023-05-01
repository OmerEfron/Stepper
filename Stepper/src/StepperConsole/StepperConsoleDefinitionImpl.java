package StepperConsole;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.impl.StepperReaderFromXml;
import Stepper.Stepper;
import StepperConsole.Execute.Executor;
import StepperConsole.Execute.ExecutorImpl;
import StepperConsole.Execute.Flow.FlowExecutionData;
import StepperConsole.Execute.Flow.FlowExecutionsCollector;
import StepperConsole.Flow.ShowFlow;
import StepperConsole.Scanner.InputFromUser;
import StepperConsole.Scanner.InputFromUserImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private final Stepper stepper = new Stepper();
    private final InputFromUser inputFromUser=new InputFromUserImpl();

    private Map<String,List<FlowExecutionData>> flowExecutions = new HashMap<>();

    private Map<String,FlowExecutionsCollector> flowExecutionsCollectorMap = new HashMap<>();

    private List<String> flowNames;

    private ConsoleStatus consoleStatus = ConsoleStatus.RUN;


    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        stepperConsoleDefinition.run();
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
        flowNames = stepper.getFlowNames();
        flowExecutionsCollectorMap.clear();
        flowNames.stream().forEach(flowName -> flowExecutionsCollectorMap.put(flowName, new FlowExecutionsCollector(flowName)));
        return true;
    }


    private static String getFilePath() {
        Scanner scanner = new Scanner(System.in);
        String filePath=scanner.nextLine();
        return filePath;
    }

    private void welcomeUser(){
        System.out.println("Welcome to Stepper. A place that all of your steps connects in a glorious way to one" +
                "marvelous flow.");
    }

    @Override
    public void run() {
        welcomeUser();
        while(consoleStatus == ConsoleStatus.RUN){
            System.out.println("Please choose what you would like to do:\n1. Load file\n2. Show flow details\n" +
                    "3. Execute flow\n4. Watch flow execution history\n5. Watch stats about flow executions.\n6. Exit");
            Integer choice = inputFromUser.getIntByRange(6);
            switch (choice) {
                case 1:load(); break;
                case 2:showFlowDetails(); break;
                case 3:executeFlow();break;
                case 4:showExecuteHistory();break;
                case 5:showStats();break;
                case 6: exit();
            }
        }

    }


    @Override
    public void showFlowDetails() {
        printFlows();
        ShowFlow showFlow = stepper.buildShowFlow(getFlowName());
        showFlow.showFlowDetails();
    }
    private int getFlowNumber() {
        System.out.println("The Flow's that in the system :");
        System.out.println(stepper.getNamesOfFlowsToPrint());
        System.out.println("Please choose the number of the flow");
        int choose =inputFromUser.getIntByRange(stepper.getNumOfFlows());
        while(!stepper.isFlowExsitByNumber(choose)){
            System.out.println("The flow number doesn't exsit");
            System.out.println("The Flow's that in the system :");
            System.out.println(stepper.getNamesOfFlowsToPrint());
            System.out.println("Please choose the number of the flow");
            //choose =inputFromUser.getInt();
        }
        return choose;
    }

    private String getFlowName(){
        Integer choice = inputFromUser.getIntByRange(stepper.getNumOfFlows());
        return stepper.getFlowsByNumber().get(choice);
    }

    private void printFlows(){
        stepper.getFlowsByNumber().forEach((place, name) -> System.out.println(place + ". " + name));
    }

    @Override
    public void executeFlow() {
        Executor executor = new ExecutorImpl(stepper);
        executor.executeFlow(inputFromUser).ifPresent(flowExecutionData ->
                flowExecutionsCollectorMap.get(flowExecutionData.getFlowName())
                        .addFlowExecutionData(flowExecutionData)
        );
    }

    @Override
    public void showExecuteHistory() {
        System.out.println("Please choose which flow you want to see an execution of it:\n");
        printFlows();
        String flowName = getFlowName();
        FlowExecutionsCollector flowExecutionsCollector = flowExecutionsCollectorMap.get(flowName);
        flowExecutionsCollector.getFlowExecutionByNumber().forEach((id, name) -> System.out.println(id + ". " + name));
        String uuid = getUUID(flowExecutionsCollector);
        showAFlowExecutionHistory(flowExecutionsCollector.getFlowExecutionData(uuid));
    }

    private String getUUID(FlowExecutionsCollector flowExecutionsCollector) {
        String uuid = flowExecutionsCollector.getFlowExecutionByNumber().get(inputFromUser.getIntByRange(
                flowExecutionsCollector.getFlowExecutionByNumber().size()));
        return uuid;
    }


    private static void showAFlowExecutionHistory(FlowExecutionData flowExecutionData) {
        System.out.println("UUID: "+ flowExecutionData.getUniqueExecutionId());
        System.out.println("Name: " + flowExecutionData.getFlowName());
        System.out.println("execution final result: "+ flowExecutionData.getFlowExecutionFinalResult());
        System.out.println("Duration (in milis): "+ flowExecutionData.getFlowExecutionDuration());
        System.out.println("Inputs:");
        flowExecutionData.getFreeInputs()
                .forEach(data -> {
                        System.out.println("Name: " + data.getName());
                        System.out.println("Content: " + data.getContent());
                        System.out.println("Type: "+data.getType());
                        System.out.println("Necessity: "+data.getNecessity());
                }
                );
    }


    @Override
    public void showStats() {

    }

    @Override
    public void exit() {
        System.out.println("Goodbye!");
        consoleStatus = ConsoleStatus.EXIT;
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
            choose = inputFromUser.getIntByRange(6);
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
