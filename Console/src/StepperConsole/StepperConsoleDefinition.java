package StepperConsole;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface StepperConsoleDefinition {
    void load() ;

    void run () throws IOException, ClassNotFoundException;
    void showFlowDetails();
    void executeFlow();
    void showExecuteHistory();

    void showStats();
    void saveToFile() throws IOException;
    void uploadFromFile() throws IOException, ClassNotFoundException;

    void exit();




}
