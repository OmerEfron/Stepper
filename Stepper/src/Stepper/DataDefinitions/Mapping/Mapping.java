package Stepper.DataDefinitions.Mapping;

import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;
import javafx.util.Pair;

public class Mapping<T,K> {
    Pair<T, K> data;

    public Mapping(T car, K cdr){
        data = new Pair<>(car, cdr);
    }
}
