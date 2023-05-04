package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.Relation.Relation;
import StepperEngine.DataDefinitions.Relation.RelationInterface;
import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.execute.context.StepExecutionContext;
import StepperEngine.Step.api.DataDefinitionDeclarationImpl;
import StepperEngine.Step.api.DataNecessity;
import StepperEngine.Step.api.StepDefinitionAbstract;
import StepperEngine.Step.api.StepStatus;

import java.util.Map;

public class CSVExporter extends StepDefinitionAbstract {
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
