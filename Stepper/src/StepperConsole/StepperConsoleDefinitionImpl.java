package StepperConsole;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.Logic.ReadStepper.TheStepper;
import Stepper.Logic.ReadStepper.api.Reader.StepperReader;
import Stepper.Logic.ReadStepper.api.Reader.StepperReaderFromXml;
import Stepper.Stepper;

import java.util.Scanner;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private Stepper theStepper;


    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        stepperConsoleDefinition.load();
    }
    @Override
    public void load() {
        System.out.println("Welcome to Stepper console !!!");
        System.out.println("Please enter file path :");
        Scanner scanner = new Scanner(System.in);
        String filePath=scanner.nextLine();
        StepperReader stepperReader=new StepperReaderFromXml();
        TheStepper stStepper = stepperReader.read(filePath);
        try {
            theStepper = new Stepper(stStepper);
        }catch (RuntimeException e) {
            System.out.println("The problems in the stepper are :");
            System.out.println(e.getMessage());
        } catch (FlowBuildException e) {
            throw new RuntimeException(e);
        }

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
