package StepperConsole.Scanner;

import StepperConsole.StepperConsoleOptions;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputFromUserImpl implements InputFromUser{

    private Scanner scanner = new Scanner(System.in);

    @Override
    public Integer getInt() {
        Integer choose=-1;
        boolean exceptionCaught = false;
        while (choose == -1 || exceptionCaught) {
            try {
                exceptionCaught = false; // reset the flag
                choose = scanner.nextInt();
            } catch (InputMismatchException e) {
                exceptionCaught = true; // set the flag
                System.out.println("You need to enter a number!\nplease try again");
                scanner.next(); // consume the invalid input
            }
        }
        return choose;
    }

    @Override
    public Integer getIntByRange(Integer range) {
        Integer choose=-1;
        boolean exceptionCaught = false;
        while (choose == -1 || exceptionCaught || choose > range) {
            try {
                exceptionCaught = false; // reset the flag
                choose = scanner.nextInt();
            } catch (InputMismatchException e) {
                exceptionCaught = true; // set the flag
                System.out.println("You need to enter a number!\nplease try again");
                scanner.next(); // consume the invalid input
            }
        }
        return choose;
    }



    @Override
    public Double getDouble() {
        Double choose=-1.0;
        while (choose ==-1.0) {
            try {
                choose = scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("You need to enter a double!\n please try again");
            }
        }
        return choose;
    }

    @Override
    public String getString() {
        return scanner.nextLine();
    }

    @Override
    public void cleanBuffer() {
        scanner.nextLine();
    }
}
