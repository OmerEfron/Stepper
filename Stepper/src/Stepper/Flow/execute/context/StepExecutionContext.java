package Stepper.Flow.execute.context;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> exceptedDataType);
    boolean storeValue(String dataName, Object value);
}
