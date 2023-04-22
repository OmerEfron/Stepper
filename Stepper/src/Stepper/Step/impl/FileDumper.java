package Stepper.Step.impl;

import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileDumper extends StepDefinitionAbstractClass {
    public FileDumper() {
        super("File Dumper", true);
        addInput(new DataDefinitionDeclarationImpl("CONTENT", "Content", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILE_NAME", "Target file path", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "File Creation Result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias)  {
        String content = context.getDataValue(nameToAlias.get("CONTENT"), String.class);
        String fileName = context.getDataValue(nameToAlias.get("FILE_NAME"), String.class);
        System.out.println("About to create file named " + fileName);
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
            context.storeValue(nameToAlias.get("RESULT"),"SUCCESS");
            if(content.length()==0) {
                return StepStatus.WARNING;
            }
            return StepStatus.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            context.storeValue(nameToAlias.get("RESULT"),"FAIL,"+e.toString());
            return StepStatus.FAIL;
        }
    }
}


