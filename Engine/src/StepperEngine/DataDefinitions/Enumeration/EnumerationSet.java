package StepperEngine.DataDefinitions.Enumeration;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class EnumerationSet <T>{
    protected Set<T> set=new HashSet<>();

    public Set<T> getSet() {
        return set;
    }
}
