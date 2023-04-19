package Stepper.Logic.ReadStepper;

import generated.STFlows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Flows {

    private List<Flow> flows;

    public Flows(STFlows stFlows){
        flows = stFlows.getSTFlow().stream()
                .map(element -> new Flow(element))
                .collect(Collectors.toList());
    }
    public List<Flow> getFlows() {
        return flows;
    }

    public void addFlow(Flow flow){
        flows.add(flow);
    }
}
