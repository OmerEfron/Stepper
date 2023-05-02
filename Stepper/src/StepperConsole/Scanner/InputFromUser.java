package StepperConsole.Scanner;

public interface InputFromUser {
    Integer getIntByRange(Integer range);

    Integer getInt();
    Double getDouble();
    String getString();
    void cleanBuffer();
}
