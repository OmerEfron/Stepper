package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.context.StepExecutionContext2;
import StepperEngine.Step.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class FileDumper extends StepDefinitionAbstract {
    public FileDumper() {
        super("File Dumper", true);
        addInput(new DataDefinitionDeclarationImpl("CONTENT", "Content", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILE_NAME", "Target file path", DataNecessity.MANDATORY, DataDefinitionRegistry.FILE_PATH));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "File Creation Result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }

    /***
     * Generates a text file by name and name in it's content
     * @param step- The step name in the flow
     * @param context -interface that saves all system data
 * @param nameToData -Map of the name of the information definition to the name of the information in the current flow
 *
     */
    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step)  {
        Instant start = Instant.now();
        String content = context.getDataValue(nameToData.get("CONTENT"), String.class);
        String fileName = context.getDataValue(nameToData.get("FILE_NAME"), String.class);
        context.addLog(step,"About to create file named " + fileName);
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
            context.storeValue(nameToData.get("RESULT"),"SUCCESS");
            if(content.length()==0) {
                context.addLog(step,"The content is empty");
                context.setInvokeSummery(step,"The file "+fileName+"create, but the content was empty. ");
                context.setStepStatus(step,StepStatus.WARNING);
            }else {
                context.setInvokeSummery(step,"The file "+fileName+"created successfully.");
                context.setStepStatus(step, StepStatus.SUCCESS);
            }
        } catch (IOException e) {
            context.setInvokeSummery(step,"The file "+fileName+" already exists.");
            context.storeValue(nameToData.get("RESULT"),"FAIL,"+e.toString());
            context.setStepStatus(step,StepStatus.FAIL);
        }
        context.setTotalTime(step, Duration.between(start, Instant.now()));
        return context.getStepStatus(step);
    }
}


