package StepperConsole;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.api.StepperReader;
import Stepper.ReadStepper.impl.StepperReaderFromXml;
import Stepper.Stepper;
import StepperConsole.Execute.Executor;
import StepperConsole.Execute.ExecutorImpl;
import StepperConsole.Flow.ShowFlow;
import StepperConsole.Flow.ShowFlowImpl;
import StepperConsole.Scanner.InputFromUser;
import StepperConsole.Scanner.InputFromUserImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private Stepper stepper;
    private InputFromUser inputFromUser=new InputFromUserImpl();


    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        if(!stepperConsoleDefinition.load()){
            return;
        }
        int choose;
        do {
            choose=stepperConsoleDefinition.chooseMenu();
            stepperConsoleDefinition.doCommand(choose);
        }while (choose != 0);
        stepperConsoleDefinition.executeFlow();

    }
    @Override
    public boolean load() {
        System.out.println("Hello to stepper!!\nPlease enter file path to load an xml application-wise file.\n" +
                "Make sure it's a valid .xml file!");
        String filePath = getFilePath();
        try {
            stepper = getStepper(new StepperReaderFromXml(), filePath);
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Stepper has been loaded successfully!");
        return true;
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
        String filePath=scanner.nextLine();
        return filePath;
    }

    @Override
    public void run() {

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
        Executor executor=new ExecutorImpl(stepper);
        executor.executeFlow(inputFromUser);
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

    @Override
    public int chooseMenu() {
        int choose;
        do {
            System.out.println("\nPlease choose which action you would like to perform");
            System.out.println("1.Introducing the Flow definition.\n" +
                    "2.Flow activation (Execution).\n" +
                    "3.Displaying full details of past flow execution.\n" +
                    "4.Statistics on the flow execution's that have happened in the system until now.\n" +
                    "5.exit.");
            choose = inputFromUser.getInt();
            if(choose<1 || choose>5)
                System.out.println(choose+" is not in range.\nchoose number between 1 to 5.");
        }while (choose<1 || choose>5);

        return choose;
    }

    @Override
    public void doCommand(int choose) {
        switch (choose){
            case (1):
                this.showFlowDetails();
                break;
            case(2):
                this.executeFlow();
                break;
            case (3):
                this.showExecuteHistory();
                break;
            case (4):
                this.showStats();
                break;
            case(5):
                this.exit();
                break;
        }
    }
}
