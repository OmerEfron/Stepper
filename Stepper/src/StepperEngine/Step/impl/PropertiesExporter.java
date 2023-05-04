package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.Relation.Relation;
import StepperEngine.DataDefinitions.Relation.RelationInterface;
import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.execute.context.StepExecutionContext;
import StepperEngine.Step.api.DataDefinitionDeclarationImpl;
import StepperEngine.Step.api.DataNecessity;
import StepperEngine.Step.api.StepDefinitionAbstract;
import StepperEngine.Step.api.StepStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class PropertiesExporter extends StepDefinitionAbstract {
    public PropertiesExporter(){
        super("Properties Exporter",true);
        addInput(new DataDefinitionDeclarationImpl("SOURCE", "Source data", DataNecessity.MANDATORY, DataDefinitionRegistry.RELATION));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "Properties export result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias, String stepName) {
        Instant start = Instant.now();
        RelationInterface relation=context.getDataValue(nameToAlias.get("SOURCE"), Relation.class);
        Integer totalProperties=0;
        String result=relation.createPropertiesExporter(totalProperties);
        context.addLog(stepName,"About to process "+relation.numOfRows()+" lines of data");

        context.addLog(stepName,"Extracted total of "+totalProperties);

        context.storeValue(nameToAlias.get("RESULT"),result);
        if(relation.isEmpty()){
            context.setInvokeSummery(stepName,"There are no rows in the relation");
            context.setStepStatus(stepName,StepStatus.WARNING);
        }else {
            context.setStepStatus(stepName,StepStatus.SUCCESS);
        }
        context.setTotalTime(stepName,Duration.between(start, Instant.now()));
        return context.getStepStatus(stepName);
    }
}
