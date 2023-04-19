package Stepper.Logic.ReadStepper;

import generated.STFlowLevelAliasing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowLevelAliasing {
    private List<FlowLevelAlias> flowLevelAliases;

    public FlowLevelAliasing(STFlowLevelAliasing stFlowLevelAliasing){
        if(stFlowLevelAliasing != null) {
            flowLevelAliases = stFlowLevelAliasing.getSTFlowLevelAlias().stream()
                    .map(element -> new FlowLevelAlias(element))
                    .collect(Collectors.toList());
        }
    }
    public void addFlowLevelAlias(FlowLevelAlias flowLevelAlias){
        flowLevelAliases.add(flowLevelAlias);
    }

    public List<FlowLevelAlias> getFlowLevelAliases() {
        return flowLevelAliases;
    }
}
