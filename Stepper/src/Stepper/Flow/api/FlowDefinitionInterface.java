package Stepper.Flow.api;

import Stepper.Step.api.DataDefinitionsDeclaration;

import java.util.List;
import java.util.Set;

public interface FlowDefinitionInterface {

    String getName();
    String getDescription();
    List<StepUsageDeclerationInterface> getSteps();
    void addStep(StepUsageDeclerationInterface stepUsageDeclerationInterface);
    String outputStrings();
    List<String> isFlowValid();

    void customMapping();
    void autoMapping();
   Set<DataDefinitionsDeclaration> getFreeInputsFromUser();



}