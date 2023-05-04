package StepperEngine.Flow.api;

import StepperEngine.Step.api.DataDefinitionsDeclaration;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FlowDefinition {

    String getName();
    String getDescription();
    List<StepUsageDecleration> getSteps();
    String outputStrings();
    List<String> isFlowValid();

    void customMapping();
    void autoMapping();

   boolean isReadOnlyFlow();
    Map<DataDefinitionsDeclaration, List<String>> getFreeInputsWithOptional();
    Map<String , Pair<DataDefinitionsDeclaration,String>> getAllOutputs();
    Map<String, Pair<DataDefinitionsDeclaration, String>> getFormalOuputs();

    Set<DataDefinitionsDeclaration> getFreeInputs();

}
