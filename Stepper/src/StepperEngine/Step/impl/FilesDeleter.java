package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.List.FilesListDataDef;
import StepperEngine.DataDefinitions.List.StringListDataDef;
import StepperEngine.DataDefinitions.Mapping.NumberMapping;
import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.execute.context.StepExecutionContext;
import StepperEngine.Step.api.DataDefinitionDeclarationImpl;
import StepperEngine.Step.api.DataNecessity;
import StepperEngine.Step.api.StepDefinitionAbstractClass;
import StepperEngine.Step.api.StepStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilesDeleter extends StepDefinitionAbstractClass {
    public FilesDeleter() {
        super("Files Deleter", false);
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files to delete", DataNecessity.MANDATORY, DataDefinitionRegistry.FILES_LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", "Files failed to be deleted", DataNecessity.NA, DataDefinitionRegistry.STRINGS_LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", "Deletion summary results", DataNecessity.NA, DataDefinitionRegistry.MAPPING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias, String stepName) {
        FilesListDataDef filesListDataDef = context.getDataValue(nameToAlias.get("FILES_LIST"), FilesListDataDef.class);
        List<File> filesToDelete = filesListDataDef.getFilesList();
        List<String> failToDelete=new ArrayList<>();
        int numOfFiles=filesToDelete.size();
        int countHowMuchNotDeleted=0;

        context.addLog(stepName,"About to start delete "+numOfFiles+" files");
        for (File file : filesToDelete) {
            if (!file.delete()) {
                countHowMuchNotDeleted++;
                failToDelete.add(file.getName());
            }
        }
        NumberMapping stats=new NumberMapping(filesToDelete.size()-countHowMuchNotDeleted,countHowMuchNotDeleted);
        context.storeValue(nameToAlias.get("DELETION_STATS"),stats);
        context.storeValue(nameToAlias.get("DELETED_LIST"),new StringListDataDef(failToDelete));

        if(countHowMuchNotDeleted==numOfFiles){
            context.setInvokeSummery(stepName,"No file was Deleted.");
            context.setStepStatus(stepName,StepStatus.FAIL);
        }
        else if(countHowMuchNotDeleted>0 ){
            failToDelete.stream().forEach(s -> context.addLog(stepName,"Failed to delete file "+s));
            context.setInvokeSummery(stepName,"There are "+countHowMuchNotDeleted+" files that didn't deleted");
            context.setStepStatus(stepName,StepStatus.WARNING);
        }else {
            context.setInvokeSummery(stepName,"All files deleted successfully.");
            context.setStepStatus(stepName, StepStatus.SUCCESS);
        }
        return context.getStepStatus(stepName);
    }
}