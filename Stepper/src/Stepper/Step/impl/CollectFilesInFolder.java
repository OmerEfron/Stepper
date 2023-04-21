package Stepper.Step.impl;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.DataDefinitions.impl.StepperNumber;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Flow.execute.context.StepExecutionContextClass;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CollectFilesInFolder extends StepDefinitionAbstractClass {


    public CollectFilesInFolder() {
        super("Collectes files in folder", true);
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", "Folder name to scan", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILTER", "Filter only this files", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files list", DataNecessity.NA, DataDefinitionRegistry.FIELS_LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", "Total files found", DataNecessity.NA, DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context) {
        String folderPath = context.getDataValue("FOLDER_NAME", String.class);
        String filterStr = context.getDataValue("FILTER", String.class);
        System.out.println("Reading folder " + folderPath + "content with filter " + filterStr);
        File folder = new File(folderPath);
        File[] files ;
        if (filterStr != null){
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(filterStr);
                }
            };
            files = folder.listFiles(filter); // get all files in the folder that match the filter
        }else {
            files = folder.listFiles();
        }

        List<File> fileList = new ArrayList<>(); // create an empty list to store the files

        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file); // add the file to the list if it is a file
            }
        }
        Integer size=fileList.size();
        context.storeValue("FILES_LIST",new FilesListDataDef(fileList),true);
        context.storeValue("TOTAL_FOUND",size,true);
        if (fileList.size() == 0) {
            System.out.println("The folder is empty");
            return StepStatus.WARNING;
        }
        return StepStatus.SUCCESS;
    }
}


