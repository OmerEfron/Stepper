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

public class CSVExporter extends StepDefinitionAbstract {
    public CSVExporter (){
        super("CSV Exporter", true);
        addInput(new DataDefinitionDeclarationImpl("SOURCE", "Source data", DataNecessity.MANDATORY, DataDefinitionRegistry.RELATION));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "CSV export result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }


    /***
     *Given some tabular information, converts it to CSV format (in string format)
     * @param context -interface that saves all system data
     * @param nameToData -Map of the name of the information definition to the name of the information in the current flow
     * @param step - The step name in the flow
     */
    @Override
    public StepStatus invoke(StepExecutionContext2 context, Map<String, DataDefinitionsDeclaration> nameToData, StepUsageDecleration step) {
        Instant start = Instant.now();
        RelationInterface relation=context.getDataValue(nameToData.get("SOURCE"), Relation.class);//get tabular information
        String result=relation.relationToCSV();//Converts the relation to CSV string
        context.addLog(step,"About to process "+relation.numOfRows().toString()+" lines of data");

        context.storeValue(nameToData.get("RESULT"), result);
        if(relation.numOfRows()==0){
            context.addLog(step,"There are no rows in the relation.");
            context.setStepStatus(step,StepStatus.WARNING);
            context.setInvokeSummery(step,"There are no rows in the relation.");
        }
        else {
            context.setInvokeSummery(step, "We created the CSV successfully.");
            context.setStepStatus(step, StepStatus.SUCCESS);
        }
        context.setTotalTime(step, Duration.between(start, Instant.now()));
        return context.getStepStatus(step);
    }
}
