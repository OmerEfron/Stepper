package Stepper.Flow.api;

import Stepper.Step.api.StepDefinitionInterface;
import javafx.util.Pair;

import java.util.Map;

public interface StepUsageDeclerationInterface {

    StepDefinitionInterface getStepDefinition();
    String getStepFinalName();
    void setAliasName(String alias);
    boolean skipIfFail();

    Integer getIndex();

    void addInputToMap(String dataName, String stepRefName, String dataNameInStepRef);

    Map<String, Pair<String, String>> getDataMap();

    Pair<String, String> getInputRef(String input);

}
