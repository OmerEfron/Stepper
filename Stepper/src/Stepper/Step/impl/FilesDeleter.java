package Stepper.Step.impl;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.DataDefinitions.List.StringListDataDef;
import Stepper.DataDefinitions.Mapping.NumberMapping;
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

public class FilesDeleter extends StepDefinitionAbstractClass {
    public FilesDeleter() {
        super("Files Deleter", false);
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files to delete", DataNecessity.MANDATORY, DataDefinitionRegistry.FIELS_LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", "Files failed to be deleted", DataNecessity.NA, DataDefinitionRegistry.STRINGS_LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", "Deletion summary results", DataNecessity.NA, DataDefinitionRegistry.MAPPING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias) {
        FilesListDataDef filesListDataDef = context.getDataValue(nameToAlias.get("FILES_LIST"), FilesListDataDef.class);
        List<File> filesToDelete = filesListDataDef.getFilesList();
        List<String> failToDelete=new ArrayList<>();
        int numOfFiles=filesToDelete.size();
        int countHowMuchNotDeleted=0;

        System.out.println("About to start delete "+numOfFiles+" files");
        for (File file : filesToDelete) {
            if (!file.delete()) {
                countHowMuchNotDeleted++;
                failToDelete.add(file.getName());
            }
        }
        NumberMapping stats=new NumberMapping(filesToDelete.size(),countHowMuchNotDeleted);
        context.storeValue(nameToAlias.get("DELETION_STATS"),stats);
        context.storeValue(nameToAlias.get("DELETED_LIST"),new StringListDataDef(failToDelete));

        if(countHowMuchNotDeleted==numOfFiles){
            System.out.println("No fail was Deleted");
            return StepStatus.FAIL;
        }
        else if(countHowMuchNotDeleted>0 ){
            System.out.println("Failed to delete file ");
            failToDelete.stream().forEach(System.out::println);
            return StepStatus.WARNING;
        }
        return StepStatus.SUCCESS;
    }
}
