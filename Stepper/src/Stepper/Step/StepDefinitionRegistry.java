package Stepper.Step;

import Stepper.Step.api.StepDefinitionInterface;
import Stepper.Step.impl.*;

public enum StepDefinitionRegistry {
    TIME_TO_SPEND(new SpendSomeTimeStep()),
    FILES_COLLECTOR(new CollectFilesInFolder()),
    FILES_DELETER (new FilesDeleter()),
    FILES_RENAMER(new FilesRenamer()),
    FILES_CONTENT_EXTRACTOR(new FilesContentExtractor()),
    FILE_DUMPER(new FileDumper()),
    PROPERTIES_EXPORTER(new PropertiesExporter()),
    CSV_EXPORTER(new CSVExporter())
    ;

    private StepDefinitionInterface stepDefinition;
    StepDefinitionRegistry(StepDefinitionInterface stepDefinition) {
        this.stepDefinition = stepDefinition;
    }

    public StepDefinitionInterface getStepDefinition() {
        return stepDefinition;
    }
}
