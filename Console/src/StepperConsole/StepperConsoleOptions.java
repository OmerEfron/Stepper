package StepperConsole;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum StepperConsoleOptions {
    LOAD(1, "Load new file to system"),
    SHOW_FLOW(2, "Show a flow details"),
    EXECUTE_FLOW(3, "Execute a flow"),
    SHOW_EXECUTE_HISTORY(4, "Show a flow execute history"),
    SHOW_STATS(5, "Show a flow Executions stats"),
    SAVE_TO_FILE(6,"Save stepper to file"),
    LOAD_FROM_FILE(7,"Load stepper from file"),
    EXIT(8, "Exit");

    private final Integer value;
    private final String userString;

    private static final Map<Integer, StepperConsoleOptions> optionsMap = Arrays.stream(StepperConsoleOptions.values())
                    .collect(Collectors.toMap(StepperConsoleOptions::getValue, option -> option));
    StepperConsoleOptions(Integer value, String userString){
        this.value = value;
        this.userString = userString;
    }

    public Integer getValue(){
        return value;
    }

    public String getUserString(){
        return userString;
    }

    public static StepperConsoleOptions getOption(Integer i){
        return optionsMap.get(i);
    }
}
