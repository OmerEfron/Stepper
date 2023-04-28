package Stepper.DataDefinitions.List;

import Stepper.DataDefinitions.impl.StepperFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilesListDataDef extends DataDefList<File>{
    public FilesListDataDef(List<File> list){
        this.list=list;
    }
    public List<File> getFilesList(){
    return list;
}

    @Override
    public String toString() {
        return IntStream.iterate(0, i -> i + 1)
                .limit(list.size())
                .mapToObj(i -> (i + 1) + "." + list.get(i).getAbsolutePath())
                .collect(Collectors.joining("\n"));
    }
}
