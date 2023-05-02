package StepperConsole.Execute.Flow.impl;

import StepperConsole.Execute.Flow.api.FlowExecutionData;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FlowExecutionsCollector {

    private final String flowName;



    private final Map<String, FlowExecutionData> flowExecutionDataMap;
    private final Map<Integer, String> flowExecutionByNumber;

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
