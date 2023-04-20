package Stepper.Logic.impl;

import Stepper.Logic.ReadStepper.Flow;
import Stepper.Logic.api.FlowDefinitionLogic;

import java.util.List;
import java.util.Map;

public class FlowDefinitionLogicImpl implements FlowDefinitionLogic {
   Flow flow;
   Map<CustomMappingData,CustomMappingData> customMappingDataMap;

    public FlowDefinitionLogicImpl(Flow flow) {
        this.flow = flow;
    }

    @Override
    public void applyAutoMapping() {

    }

    @Override
    public List<String> applyCustomMapping() {

        return null;
    }
}
