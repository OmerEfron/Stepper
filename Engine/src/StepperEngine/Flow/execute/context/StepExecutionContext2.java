package StepperEngine.Flow.execute.context;

import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperEngine.Step.api.StepStatus;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface StepExecutionContext2 {
    <T> T getDataValue(DataDefinitionsDeclaration data, Class<T> exceptedDataType);
    boolean storeValue(DataDefinitionsDeclaration data, Object value);

    void updateCustomMap(StepUsageDecleration currStep);

    Map<DataDefinitionsDeclaration, Object> getAllData();

    void addFormalOutput(FlowExecution flowExecution);
    void addLog(StepUsageDecleration step, String log);

    void setInvokeSummery(StepUsageDecleration step, String summery);

    void setStepStatus(StepUsageDecleration step, StepStatus stepStatus);

    void setTotalTime(StepUsageDecleration step, Duration totalTime);

    void addStepData(StepUsageDecleration step);

    void setStartStep(StepUsageDecleration step);

    void setEndStep(StepUsageDecleration step);

    StepStatus getStepStatus(StepUsageDecleration step);

    Map<StepUsageDecleration,StepExecuteData> getStepsData();

    void addDataToStepData(StepUsageDecleration step, DataDefinitionsDeclaration data,boolean isOutput);
}
