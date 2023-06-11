package StepperEngine.Flow.api;

import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.StepDefinition;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface StepUsageDecleration {

    StepDefinition getStepDefinition();
    String getStepFinalName();
    void setAliasName(String alias);
    boolean skipIfFail();

    Integer getIndex();

    void addInputToStringMap(String dataName, String stepRefName, String dataNameInStepRef);
    void addOutputToMap(String dataName, String stepRefName, String dataNameInStepRef);
    Map<String, List<Pair<String, String>>> getOutputDataMap();

    Map<String, Pair<String, String>> getStringPairMap();

    Pair<String, String> getInputRef(String input);
    Map<String, DataDefinitionsDeclaration> getNameToAlias();
    boolean isReadOnlyStep();
    void addInputToMap(DataDefinitionsDeclaration dest, StepUsageDecleration stepSource, DataDefinitionsDeclaration source);
    Map<DataDefinitionsDeclaration, Pair<StepUsageDecleration, DataDefinitionsDeclaration>> getDataMap();

}
