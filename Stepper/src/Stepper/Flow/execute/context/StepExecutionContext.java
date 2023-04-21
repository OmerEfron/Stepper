package Stepper.Flow.execute.context;

import Stepper.DataDefinitions.api.DataDefinitionInterface;
import Stepper.Step.api.DataDefinitionsDeclaration;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> exceptedDataType);
    boolean storeValue(String dataName, Object value, boolean isOutput);

}
