package Stepper.Logic.ReadStepper;

import generated.STCustomMapping;
import generated.STCustomMappings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomMappings {

    private List<CustomMapping> customMappings;
    public List<CustomMapping> getCustomMappings() {
        return customMappings;
    }

    public CustomMappings(STCustomMappings stCustomMappings) {
        if (stCustomMappings != null) {
            customMappings = stCustomMappings.getSTCustomMapping().stream()
                    .filter(Objects::nonNull)
                    .map(element -> new CustomMapping(element))
                    .collect(Collectors.toList());
        }
    }
    public void addCutsomMapping(CustomMapping customMapping){
        customMappings.add(customMapping);
    }


}
