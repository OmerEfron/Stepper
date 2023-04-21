package Stepper.DataDefinitions.List;

import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

import java.util.ArrayList;
import java.util.List;

public class DataDefList<T> {
    protected List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }
}
