package StepperEngine.Flow.api;

import StepperEngine.Step.api.DataDefinitionsDeclaration;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlowDefinitionInterface {

    String getName();
    String getDescription();
    List<StepUsageDeclerationInterface> getSteps();
    String outputStrings();
    List<String> isFlowValid();

    void customMapping();
    void autoMapping();

   boolean isReadOnlyFlow();
    Map<DataDefinitionsDeclaration, List<String>> getFreeInputsWithOptional();
    Map<String , Pair<DataDefinitionsDeclaration,String>> getAllOutputs();
    public Map<String, Pair<DataDefinitionsDeclaration, String>> getFormalOuputs();

    public Set<DataDefinitionsDeclaration> getFreeInputs();

}
