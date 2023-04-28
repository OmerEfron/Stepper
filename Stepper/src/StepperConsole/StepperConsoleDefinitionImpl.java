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
import StepperConsole.Scanner.InputFromUser;
import StepperConsole.Scanner.InputFromUserImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private Stepper stepper;
    private InputFromUser inputFromUser=new InputFromUserImpl();


    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        stepperConsoleDefinition.load();
        stepperConsoleDefinition.executeFlow();

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
        //showFlowDetails();
        executeFlow();
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
        ShowFlow showFlow = stepper.showFlowByName("Rename Files");
        showFlow.showFlowDetails();
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
}
