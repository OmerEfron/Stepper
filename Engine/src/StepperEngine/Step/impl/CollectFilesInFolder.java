package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.List.FilesListDataDef;
import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.context.StepExecutionContext2;
import StepperEngine.Step.api.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CollectFilesInFolder extends StepDefinitionAbstract {


    public CollectFilesInFolder() {
        super("Collect Files In Folder", true);
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", "Folder name to scan", DataNecessity.MANDATORY, DataDefinitionRegistry.FOLDER_PATH));
        addInput(new DataDefinitionDeclarationImpl("FILTER", "Filter only this files", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files list", DataNecessity.NA, DataDefinitionRegistry.FILES_LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", "Total files found", DataNecessity.NA, DataDefinitionRegistry.NUMBER));
    }

    /**
     * Given a path to the directory, it will go through and scan all the files that are in it and return a list of values of type File.
     *
     * @param context    -interface that saves all system data
     * @param nameToData -Map of the name of the information definition to the name of the information in the current flow
     * @param step       - The step name in the flow
     */
    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step) {
        Instant start = Instant.now();
        String folderAlias = nameToData.get("FOLDER_NAME").getAliasName();
        String filterAlias = nameToData.get("FILTER").getAliasName();
        String filesListAlias = nameToData.get("FILES_LIST").getAliasName();
        String totalFoundAlias = nameToData.get("TOTAL_FOUND").getAliasName();
        String folderPath = context.getDataValue(nameToData.get("FOLDER_NAME"), String.class);
        Optional<String> filterStr = Optional.ofNullable(context.getDataValue(nameToData.get("FILTER"), String.class));
        StepStatus stepStatus = StepStatus.SUCCESS;
        context.addLog(step,"Reading folder " + folderPath + "content with filter " + filterStr.orElse(""));
        Path path = Paths.get(folderPath);

        File folder = path.toFile();
        File[] files = filterStr.map(filter -> folder.listFiles((dir, name) -> name.endsWith(filter)))
                .orElse(folder.listFiles());
        List<File> fileList = new ArrayList<>(); // create an empty list to store the files

        if(files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file); // add the file to the list if it is a file
                }
            }
        }

        Integer size=fileList.size();

        context.storeValue(nameToData.get("FILES_LIST"), new FilesListDataDef(fileList));
        context.storeValue(nameToData.get("TOTAL_FOUND"), size);
        if(!Files.isDirectory(path)){
            context.addLog(step,"There are no folder in path "+folderPath);
            context.setInvokeSummery(step,"There are no folder in path "+folderPath);
            context.setStepStatus(step,StepStatus.FAIL);
            stepStatus = StepStatus.FAIL;
        }
        else if (fileList.size() == 0) {
            context.addLog(step,"No files in folder matching the filter.");
            context.setInvokeSummery(step,"The folder is empty.");
            context.setStepStatus(step,StepStatus.WARNING);
            stepStatus = StepStatus.WARNING;
        }
        else {
            context.addLog(step, "Found " + size + " files in folder matching the filter ");
            context.setInvokeSummery(step, "The files in " + folderPath + " collected successfully");
            context.setStepStatus(step, StepStatus.SUCCESS);
        }
        context.setTotalTime(step, Duration.between(start, Instant.now()));
        return stepStatus;
    }
}


