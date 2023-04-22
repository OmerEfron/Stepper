package Stepper.Step.impl;

import Stepper.DataDefinitions.Relation.Relation;
import Stepper.DataDefinitions.Relation.RelationInterface;
import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.util.Map;

public class CSVExporter extends StepDefinitionAbstractClass {
    public CSVExporter (){
        super("CSV Exporter", true);
        addInput(new DataDefinitionDeclarationImpl("SOURCE", "Source data", DataNecessity.MANDATORY, DataDefinitionRegistry.RELATION));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "CSV export result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }
    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias) {
        RelationInterface relation=context.getDataValue(nameToAlias.get("SOURCE"),Relation.class);
        String result=relation.relationToCSV();
        System.out.println("About to process "+relation.numOfRows().toString()+" lines of data");

        context.storeValue(nameToAlias.get("RESULT"),result);
        if(relation.numOfRows()==0){
            System.out.println("There are no rows in the relation");
            return StepStatus.WARNING;
        }
        return StepStatus.SUCCESS;
    }
}
