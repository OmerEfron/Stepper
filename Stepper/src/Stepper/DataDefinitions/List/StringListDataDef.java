package Stepper.DataDefinitions.List;

import Stepper.DataDefinitions.impl.StepperString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringListDataDef extends DataDefList<String>{
    public StringListDataDef(List<String> list) {
        this.list=list;
    }

    @Override
    public String toString() {
        String res="";
        int i=1;
        for(String str:list){
            res+=i+"."+str;
            if(i!= list.size())
                res+="\n";
        }
        return res;
//        return IntStream.range(1, list.size())
//                .mapToObj(i -> i + "." + list.get(i - 1))
//                .collect(Collectors.joining("\n"));
    }
}
