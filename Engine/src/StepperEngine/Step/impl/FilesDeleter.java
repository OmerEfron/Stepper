package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.List.FilesListDataDef;
import StepperEngine.DataDefinitions.List.StringListDataDef;
import StepperEngine.DataDefinitions.Mapping.NumberMapping;
import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.context.StepExecutionContext2;
import StepperEngine.Step.api.*;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilesDeleter extends StepDefinitionAbstract {
    public FilesDeleter() {
        super("Files Deleter", false);
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files to delete", DataNecessity.MANDATORY, DataDefinitionRegistry.FILES_LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", "Files failed to be deleted", DataNecessity.NA, DataDefinitionRegistry.STRINGS_LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", "Deletion summary results", DataNecessity.NA, DataDefinitionRegistry.MAPPING));
    }

    /***
     * Given a list, files, goes through all of them and deletes them.
     * @param context -interface that saves all system data
     * @param nameToData -Map of the name of the information definition to the name of the information in the current flow
     * @param step - The step name in the flow
     */
    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step) {
        Instant start = Instant.now();
        FilesListDataDef filesListDataDef = context.getDataValue(nameToData.get("FILES_LIST"), FilesListDataDef.class);
        List<File> filesToDelete = filesListDataDef.getFilesList();
        List<String> failToDelete=new ArrayList<>();
        int numOfFiles=filesToDelete.size();
        int countHowMuchNotDeleted=0;

        context.addLog(step,"About to start delete "+numOfFiles+" files");
        for (File file : filesToDelete) {
            if (!file.delete()) {
                countHowMuchNotDeleted++;
                failToDelete.add(file.getName());
            }
        }
        NumberMapping stats=new NumberMapping(filesToDelete.size()-countHowMuchNotDeleted,countHowMuchNotDeleted);
        context.storeValue(nameToData.get("DELETION_STATS"),stats);
        context.storeValue(nameToData.get("DELETED_LIST"),new StringListDataDef(failToDelete));

        if(countHowMuchNotDeleted==numOfFiles&& !filesToDelete.isEmpty()){
            context.setInvokeSummery(step,"No file was Deleted.");
            context.setStepStatus(step,StepStatus.FAIL);
        }
        else if(countHowMuchNotDeleted>0 ){
            failToDelete.stream().forEach(s -> context.addLog(step,"Failed to delete file "+s));
            context.setInvokeSummery(step,"There are "+countHowMuchNotDeleted+" files that didn't deleted");
            context.setStepStatus(step,StepStatus.WARNING);
        }else {
            if (filesToDelete.isEmpty())
                context.setInvokeSummery(step,"There are no files to delete.");
            else
                context.setInvokeSummery(step,"All files deleted successfully.");
            context.setStepStatus(step, StepStatus.SUCCESS);
        }
        context.setTotalTime(step, Duration.between(start, Instant.now()));
        return context.getStepStatus(step);
    }
}
