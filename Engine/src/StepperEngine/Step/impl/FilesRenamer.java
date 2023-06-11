package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.List.FilesListDataDef;
import StepperEngine.DataDefinitions.Relation.RelationOfStringRows;
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
import java.util.Optional;

public class FilesRenamer extends StepDefinitionAbstract {
    public FilesRenamer() {
        super("Files Renamer", false);
        this.addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME","Files to rename", DataNecessity.MANDATORY, DataDefinitionRegistry.FILES_LIST));
        this.addInput(new DataDefinitionDeclarationImpl("PREFIX","Add this prefix", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        this.addInput(new DataDefinitionDeclarationImpl("SUFFIX","Append this suffix", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        this.addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT","Rename operation summary",DataNecessity.NA,DataDefinitionRegistry.RELATION_STRING));
    }

    /***
     *Given a list of filenames, renames all of them by adding a prefix or suffix as defined in the input.
     * @param context -interface that saves all system data
     * @param nameToData -Map of the name of the information definition to the name of the information in the current flow
     * @param step - The step name in the flow
     * @return List in a table configuration describing all files with their original name and their final name
     */
    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step) {
        Instant start = Instant.now();
        FilesListDataDef filesListDataDef = context.getDataValue(nameToData.get("FILES_TO_RENAME"), FilesListDataDef.class);
        List<File> filesToRename = filesListDataDef.getFilesList();
        List<String> filesFailed=new ArrayList<>();
        Optional<String> prefix=Optional.ofNullable(context.getDataValue(nameToData.get("PREFIX"), String.class));
        Optional<String> suffix=Optional.ofNullable(context.getDataValue(nameToData.get("SUFFIX"), String.class));
        RelationOfStringRows result = createRelationOfStringRows();
        Integer num=0;

        context.addLog(step,"About to start rename " + filesToRename.size() + " files. Adding prefix: " + prefix.orElse(",") + " adding suffix: " + suffix.orElse(""));
        StepStatus success = checkIfTheFolderEmpty(context, filesToRename, result, nameToData.get("RENAME_RESULT"),step);
        if (success != null) {
            context.setTotalTime(step,Duration.between(start, Instant.now()));
            return success;}

        for (File file : filesToRename) {
            List<String> row=new ArrayList<>();
            row.add((++num).toString());
            String originalFileName = file.getName();
            row.add(originalFileName);
            int lastDotIndex = originalFileName.lastIndexOf('.');
            String nameWithoutExtension = lastDotIndex != -1 ? originalFileName.substring(0, lastDotIndex) : originalFileName;
            String extension = lastDotIndex != -1 ? originalFileName.substring(lastDotIndex) : "";
            String newFileName = prefix.orElse("") + nameWithoutExtension + suffix.orElse("") + extension;
            try {
            File newFile = new File(file.getParentFile(), newFileName);
            row.add(newFileName);
            file.renameTo(newFile);
            result.addRow(row);
        } catch(Exception e){
                row.add(originalFileName);
                context.addLog(step,"Problem renaming file " + originalFileName);
                result.addRow(row);
                filesFailed.add(originalFileName);
        }
    }
        context.storeValue(nameToData.get("RENAME_RESULT"),result);

        if(filesFailed.size()!=0){
            context.setInvokeSummery( step,"Step ended with warning because some files failed to rename\n" +
                    "The files that failed to rename are :" + String.join(", ", filesFailed) + ".");
            context.setStepStatus(step,StepStatus.WARNING);
        }else{
            context.setInvokeSummery(step,"All files renamed successfully");
            context.setStepStatus(step,StepStatus.SUCCESS);
        }
        context.setTotalTime(step,Duration.between(start, Instant.now()));
        return context.getStepStatus(step);
    }

    private static StepStatus checkIfTheFolderEmpty(StepExecutionContext2 context, List<File> filesToRename, RelationOfStringRows result,DataDefinitionsDeclaration data,StepUsageDecleration step) {
        if(filesToRename.size()==0){
            context.storeValue(data, result);
            context.setInvokeSummery(step,"There are no files no rename");
            context.setStepStatus(step,StepStatus.SUCCESS);
            return StepStatus.SUCCESS;
        }
        return null;
    }

    private static RelationOfStringRows createRelationOfStringRows() {
        List<String> colNames=new ArrayList<>();
        colNames.add("Serial Number");
        colNames.add("Original File Name");
        colNames.add("File Name After The Change");
        RelationOfStringRows result = new RelationOfStringRows(colNames);
        return result;
    }

}
