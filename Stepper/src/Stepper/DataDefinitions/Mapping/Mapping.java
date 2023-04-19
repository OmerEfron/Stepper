package Stepper.DataDefinitions.Mapping;

import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;
import javafx.util.Pair;

public class Mapping {
    Pair<DataDefinitionAbstractClass, DataDefinitionAbstractClass> data;

    public Mapping(DataDefinitionAbstractClass car, DataDefinitionAbstractClass cdr){
        data = new Pair<>(car, cdr);
    }
}
