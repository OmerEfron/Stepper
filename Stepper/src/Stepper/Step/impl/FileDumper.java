package Stepper.Step.impl;

import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.io.FileWriter;
import java.io.IOException;

public class FileDumper extends StepDefinitionAbstractClass {
    public FileDumper() {
        super("File Dumper", true);
        addInput(new DataDefinitionDeclarationImpl("CONTENT", "Content", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILE_NAME", "Target file path", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "File Creation Result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context)  {
        String content = context.getDataValue("CONTENT", String.class);
        String fileName = context.getDataValue("FILE_NAME", String.class);
        System.out.println("About to create file named " + fileName);
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
            context.addOutput("RESULT","SUCCESS");
            if(content.length()==0) {
                return StepStatus.WARNING;
            }
            return StepStatus.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            context.addOutput("RESULT","FAIL,"+e.toString());
            return StepStatus.FAIL;
        }
    }
}


