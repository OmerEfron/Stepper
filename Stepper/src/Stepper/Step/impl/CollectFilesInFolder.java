package Stepper.Step.impl;

import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

public class CollectFilesInFolder extends StepDefinitionAbstractClass {


    public CollectFilesInFolder(){
        super("Collect Files In Folder", true);
        addInput(new DataDefinitionDeclarationImpl("FOLDER_NAME", "Folder name to scan", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("FILTER", "Filter only this files", DataNecessity.OPTIONAL, DataDefinitionRegistry.STRING));
        addOutput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files list", DataNecessity.NA, DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("TOTAL_FOUND", "Total files found", DataNecessity.NA, DataDefinitionRegistry.NUMBER));
    }
    @Override
    public StepStatus invoke(StepExecutionContext context) {
        return null;
    }
}
