package StepperConsole;

public interface StepperConsoleDefinition {
    boolean load() ;

    void run ();
    void showFlowDetails();
    void executeFlow();
    void showExecuteHistory();

    void showStats();

    void exit();

    int chooseMenu();

    void doCommand(int choose);
}
