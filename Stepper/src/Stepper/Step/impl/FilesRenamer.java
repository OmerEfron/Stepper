package Stepper.Step.impl;

import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionAbstractClass;
import Stepper.Step.api.StepStatus;

public class FilesRenamer extends StepDefinitionAbstractClass {
    public FilesRenamer() {
        super("Files Renamer", false);
        this.addInput(new DataDefinitionDeclarationImpl("FILES_TO_RENAME","Files to rename", DataNecessity.MANDATORY, DataDefinitionRegistry.LIST));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context) {
        return null;
    }
}
