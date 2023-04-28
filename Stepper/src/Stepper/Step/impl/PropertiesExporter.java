package Stepper.Step.impl;

import Stepper.DataDefinitions.Relation.Relation;
import Stepper.DataDefinitions.Relation.RelationInterface;
import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class PropertiesExporter extends StepDefinitionAbstractClass {
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
