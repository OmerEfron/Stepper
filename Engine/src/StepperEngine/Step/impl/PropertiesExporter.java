package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.Relation.Relation;
import StepperEngine.DataDefinitions.Relation.RelationInterface;
import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.Flow.execute.context.StepExecutionContext2;
import StepperEngine.Step.api.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class PropertiesExporter extends StepDefinitionAbstract {
    public PropertiesExporter(){
        super("Properties Exporter",true);
        addInput(new DataDefinitionDeclarationImpl("SOURCE", "Source data", DataNecessity.MANDATORY, DataDefinitionRegistry.RELATION));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "Properties export result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }

    /***
     *Given some tabular information, converts it to the format of a properties file (in the form of a string).
     * @param context -interface that saves all system data
     * @param nameToData -Map of the name of the information definition to the name of the information in the current flow
     * @param step - The step name in the flow
     */
    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step) {
        Instant start = Instant.now();
        RelationInterface relation=context.getDataValue(nameToData.get("SOURCE"), Relation.class);
        Integer totalProperties=0;
        String result=relation.createPropertiesExporter(totalProperties);
        context.addLog(step,"About to process "+relation.numOfRows()+" lines of data");

        context.addLog(step,"Extracted total of "+totalProperties);

        context.storeValue(nameToData.get("RESULT"), result);
        if(relation.isEmpty()){
            context.setInvokeSummery(step,"There are no rows in the relation");
            context.setStepStatus(step,StepStatus.WARNING);
        }else {
            context.setInvokeSummery(step,"We created the Properties successfully");
            context.setStepStatus(step,StepStatus.SUCCESS);
        }
        context.setTotalTime(step,Duration.between(start, Instant.now()));
        return context.getStepStatus(step);
    }
}
