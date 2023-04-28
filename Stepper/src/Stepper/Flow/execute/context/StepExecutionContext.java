package Stepper.Flow.execute.context;

import Stepper.DataDefinitions.api.DataDefinitionInterface;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.StepData.StepExecuteData;
import Stepper.Step.api.DataDefinitionsDeclaration;
import Stepper.Step.api.StepStatus;

import java.time.Duration;
import java.util.List;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> exceptedDataType);
    boolean storeValue(String dataName, Object value);
    void updateCustomMap(StepUsageDeclerationInterface currStep);

    <T> T getOutput(String dataName, Class<T> exceptedDataType);
    void addFormalOutput(FlowExecution flowExecution);
    //void stepData(StepExecuteData name);
    void addLog(String stepName,String log);
    void setInvokeSummery(String stepName,String summery);
    void setStepStatus(String stepName, StepStatus stepStatus);
    void setTotalTime(String stepName,Duration totalTime);
    void addStepData(StepUsageDeclerationInterface step);


    StepStatus getStepStatus(String stepName);
    List<StepExecuteData>getStepsData();
}
