package StepperConsole;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.api.StepperReader;
import Stepper.ReadStepper.impl.StepperReaderFromXml;
import Stepper.Stepper;

import java.util.Scanner;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private Stepper stepper;


    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        stepperConsoleDefinition.load();
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
        String filePath=scanner.nextLine();
        return filePath;
    }

    @Override
    public void run() {

    }

    @Override
    public void showFlowDetails() {

    }

    @Override
    public void executeFlow() {

    }

    @Override
    public void showExecuteHistory() {

    }
}
