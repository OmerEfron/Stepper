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
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias, String stepName) {
        RelationInterface relation=context.getDataValue(nameToAlias.get("SOURCE"),Relation.class);
        String result=relation.relationToCSV();
        context.addLog(stepName,"About to process "+relation.numOfRows().toString()+" lines of data");

        context.storeValue(nameToAlias.get("RESULT"),result);
        if(relation.numOfRows()==0){
            context.addLog(stepName,"There are no rows in the relation.");
            context.setStepStatus(stepName,StepStatus.WARNING);
            context.setInvokeSummery(stepName,"There are no rows in the relation.");
        }
        else {
            context.setInvokeSummery(stepName, "We created the CSV successfully.");
            context.setStepStatus(stepName, StepStatus.SUCCESS);
        }
        return context.getStepStatus(stepName);
    }
}
