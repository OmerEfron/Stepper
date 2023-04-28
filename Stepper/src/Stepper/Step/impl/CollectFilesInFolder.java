package Stepper.Step.impl;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CollectFilesInFolder extends StepDefinitionAbstractClass {


    public CollectFilesInFolder() {
        super("Collect Files In Folder", true);
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", "Folder name to scan", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILTER", "Filter only this files", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files list", DataNecessity.NA, DataDefinitionRegistry.FIELS_LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", "Total files found", DataNecessity.NA, DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias, String stepName) {
        String folderPath = context.getDataValue(nameToAlias.get("FOLDER_NAME"), String.class);
        Optional<String> filterStr = Optional.ofNullable(context.getDataValue(nameToAlias.get("FILTER"), String.class));
        context.setInvokeSummery(stepName,"Reading folder " + folderPath + "content with filter " + filterStr);
        File folder = new File(folderPath);
        File[] files = filterStr.map(filter -> folder.listFiles((dir, name) -> name.endsWith(filter)))
                .orElse(folder.listFiles());
        List<File> fileList = new ArrayList<>(); // create an empty list to store the files

        if(files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file); // add the file to the list if it is a file
                }
            }
        }else{
            context.addLog(stepName,"There are no folder in path "+folderPath);
            context.setInvokeSummery(stepName,"There are no folder in path "+folderPath);
            context.setStepStatus(stepName,StepStatus.FAIL);
        }
        Integer size=fileList.size();

        context.storeValue(nameToAlias.get("FILES_LIST"),new FilesListDataDef(fileList));
        context.storeValue(nameToAlias.get("TOTAL_FOUND"),size);

        if (fileList.size() == 0) {
            context.setInvokeSummery(stepName,"The folder is empty.");
            context.setStepStatus(stepName,StepStatus.WARNING);
            return StepStatus.WARNING;
        }
        context.setInvokeSummery(stepName,"The files in "+folderPath+" collected successfully");
        context.setStepStatus(stepName,StepStatus.SUCCESS);
        return StepStatus.SUCCESS;
    }
}


