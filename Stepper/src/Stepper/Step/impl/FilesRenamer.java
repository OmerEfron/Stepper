package Stepper.Step.impl;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.DataDefinitions.Relation.RelationOfStringRows;
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

public class FilesRenamer extends StepDefinitionAbstractClass {
    public FilesRenamer() {
        super("Files Renamer", false);
        this.addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME","Files to rename", DataNecessity.MANDATORY, DataDefinitionRegistry.FIELS_LIST));
        this.addInput(new DataDefinitionDeclarationImpl("PREFIX","Add this prefix", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        this.addInput(new DataDefinitionDeclarationImpl("SUFFIX","Append this suffix", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        this.addOutput(new DataDefinitionDeclarationImpl("RENAME_RESULT","Rename operation summary",DataNecessity.NA,DataDefinitionRegistry.RELATION_STRING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias) {
        FilesListDataDef filesListDataDef = context.getDataValue(nameToAlias.get("FILES_TO_RENAME"), FilesListDataDef.class);
        List<File> filesToRename = filesListDataDef.getFilesList();
        List<String> filesFailed=new ArrayList<>();
        Optional<String> prefix=Optional.ofNullable(context.getDataValue("PREFIX", String.class));
        Optional<String> suffix=Optional.ofNullable(context.getDataValue("SUFFIX", String.class));
        RelationOfStringRows result = createRelationOfStringRows();
        Integer num=0;

        System.out.println("About to start rename" + filesToRename.size() + " files. Adding prefix: " + prefix + " adding suffix: " + suffix);
        StepStatus success = checkIfTheFolderEmpty(context, filesToRename, result,nameToAlias.get("RENAME_RESULT"));
        if (success != null) return success;

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
            System.out.println("Prefix and postfix added to file names successfully.");
        } catch(Exception e){
                row.add(originalFileName);
                System.err.println("Problem renaming file " + originalFileName);
                result.addRow(row);
                filesFailed.add(originalFileName);

        }
    }
        context.storeValue(nameToAlias.get("RENAME_RESULT"),result);

        if(filesFailed.size()!=0){
            System.out.println("The files that failed to rename:");
            filesFailed.forEach(System.out::println);
            return StepStatus.WARNING;
        }
        return StepStatus.SUCCESS;
    }

    private static StepStatus checkIfTheFolderEmpty(StepExecutionContext context, List<File> filesToRename, RelationOfStringRows result,String name) {
        if(filesToRename.size()==0){
            context.storeValue(name, result);
            System.out.println("There are no files no rename");
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