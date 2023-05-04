package StepperConsole.Scanner;


import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * a class that helps to get inputs from the user
 * implements methods for getting a string from the user, an integer, double and so
 */
public class InputFromUserImpl implements InputFromUser{

    private final Scanner scanner = new Scanner(System.in);

    /**
     * gets an int from the user and returns it
     * won't proceed until it gets an integer
     * @return the int the user inserted
     */
    @Override
    public Integer getInt() {
        Integer choose=-1;
        boolean exceptionCaught = false;
        //while (choose == -1 || exceptionCaught) {
        do {
            try {
                exceptionCaught = false; // reset the flag
                choose = scanner.nextInt();
            } catch (InputMismatchException e) {
                exceptionCaught = true; // set the flag
                System.out.println("You need to enter a number!\nplease try again");
                scanner.next(); // consume the invalid input
            }
        }while (exceptionCaught);
        return choose;
    }

    /**
     * gets an int from the user from range (1,range]
     * won't proceed until it gets a valid integer.
     * @param range the max number the user can insert.
     * @return the integer the user inserted.
     */
    @Override
    public Integer getIntByRange(Integer range) {
        Integer choose=null;
        boolean isInt = false;
        while (!isInt) {
            try {

                choose = scanner.nextInt();
                if(choose>range || choose<1) {
                    System.out.println(choose + " is not in range.\nPlease try again.");
                    isInt = false;
                }
                else
                    isInt=true;
            } catch (InputMismatchException e) {
                isInt = false; // set the flag
                System.out.println("You need to enter a number!\nplease try again");
                scanner.next(); // consume the invalid input
            }
        }
        return choose;
    }


    /**
     * gets a floating point positive number from the user and returns it
     * won't proceed until it gets a floating point positive
     * @return the number the user inserted
     */
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

    /**
     * gets a line from the user
     * @return a string of the line
     */
    @Override
    public String getString() {
        return scanner.nextLine();
    }

    @Override
    public void cleanBuffer() {
        scanner.nextLine();
    }
}
