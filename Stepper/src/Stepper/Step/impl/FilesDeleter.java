package Stepper.Step.impl;

import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

public class FilesDeleter extends StepDefinitionAbstractClass {
    public FilesDeleter() {
        super("Files Deleter", false);
        addInput(new DataDefinitionDeclarationImpl("FILES_LIST", "Files to delete", DataNecessity.MANDATORY, DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETED_LIST", "Files failed to be deleted", DataNecessity.NA, DataDefinitionRegistry.LIST));
        addOutput(new DataDefinitionDeclarationImpl("DELETION_STATS", "Deletion summary results", DataNecessity.NA, DataDefinitionRegistry.MAPPING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context) {
        return null;
    }
}
