package StepperEngine.DataDefinitions.impl;

import StepperEngine.DataDefinitions.api.DataDefinitionAbstractClass;

import java.io.File;

public class StepperFile extends DataDefinitionAbstractClass {

    StepperFile(){
        super("File", false, File.class);
    }

}
