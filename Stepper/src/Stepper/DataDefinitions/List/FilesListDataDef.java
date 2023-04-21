package Stepper.DataDefinitions.List;

import Stepper.DataDefinitions.impl.StepperFile;

import java.io.File;
import java.util.List;

public class FilesListDataDef extends DataDefList<File>{
    public FilesListDataDef(List<File> list){
        this.list=list;
    }
public List<File> getFilesList(){
    return list;
}
}
