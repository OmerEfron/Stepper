package StepperConsole.Execute.Flow.impl;

import StepperConsole.Execute.Flow.api.FlowExecutionData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds all the flow executions history data.
 */
public class FlowExecutionsCollector implements Serializable {

    private final String flowName;
    private final Map<String, FlowExecutionData> flowExecutionDataMap; //uuid -> execution data
    private final Map<Integer, String> flowExecutionByNumber; // id -> uuid

    public FlowExecutionsCollector(String flowName) {
        this.flowName = flowName;
        this.flowExecutionDataMap = new HashMap<>();
        this.flowExecutionByNumber = new LinkedHashMap<>();
    }

    public Integer getNumOfExecutions(){
        return flowExecutionDataMap.size();
    }

    public String getFlowName() {
        return flowName;
    }

    public Map<String, FlowExecutionData> getFlowExecutionDataMap() {
        return flowExecutionDataMap;
    }

    public void addFlowExecutionData(FlowExecutionData flowExecutionData){
        flowExecutionDataMap.put(flowExecutionData.getUniqueExecutionId(), flowExecutionData);
        flowExecutionByNumber.put(flowExecutionDataMap.size(), flowExecutionData.getUniqueExecutionId());
    }

    public FlowExecutionData getFlowExecutionData(String uuid){
        return flowExecutionDataMap.get(uuid);
    }

    public Map<Integer, String> getFlowExecutionByNumber() {
        return flowExecutionByNumber;
    }
}
