package Stepper.Step.impl;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.DataDefinitions.Relation.RelationOfStringRows;
import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilesContentExtractor extends StepDefinitionAbstractClass {
    public FilesContentExtractor() {
        super("Files Content Extractor", true);
        this.addInput(new DataDefinitionDeclarationImpl("FILES_LIST","Files to extract", DataNecessity.MANDATORY, DataDefinitionRegistry.FIELS_LIST));
        this.addInput(new DataDefinitionDeclarationImpl("LINE","Line number to extract", DataNecessity.MANDATORY, DataDefinitionRegistry.NUMBER));
        this.addOutput(new DataDefinitionDeclarationImpl("DATA","Data extraction",DataNecessity.NA,DataDefinitionRegistry.RELATION_STRING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias) {
        FilesListDataDef filesListDataDef=context.getDataValue(nameToAlias.get("FILES_LIST"),FilesListDataDef.class);
        List<File> files=filesListDataDef.getFilesList();
        Integer lineNumber=context.getDataValue(nameToAlias.get("LINE"), Integer.class);
        String line;
        Integer rowNumber=0;
        RelationOfStringRows result = createRelationOfStringRows();

        for (File file : files) {
            List<String> row =new ArrayList<>();
            rowNumber++;
            boolean isRowFound = false;
            row.add(rowNumber.toString());
            row.add(file.getName());
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                int currentLine = 1;
                while ((line = br.readLine()) != null) {
                    if (currentLine == lineNumber) {
                        row.add(line);
                        isRowFound=true;
                        break;
                    }
                    currentLine++;
                }
                if (!isRowFound){row.add("Not such line");}
            } catch (FileNotFoundException e) {
                    row.add("File not found");
                    row.add("File not found");
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            result.addRow(row);
        }
        context.storeValue(nameToAlias.get("DATA"),result);

        if(result.isEmpty()){return  StepStatus.WARNING;}
        return StepStatus.SUCCESS;
    }

    private static RelationOfStringRows createRelationOfStringRows() {
        List<String> colNames=new ArrayList<>();
        colNames.add("Serial Number");
        colNames.add("Original File Name");
        colNames.add("The textual information retrieved from the file");
        RelationOfStringRows result=new RelationOfStringRows(colNames);
        return result;
    }
}