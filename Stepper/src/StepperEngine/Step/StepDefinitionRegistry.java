package StepperEngine.Step;

import StepperEngine.Step.api.StepDefinition;
import StepperEngine.Step.impl.*;

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

    private final StepDefinition stepDefinition;
    StepDefinitionRegistry(StepDefinition stepDefinition) {
        this.stepDefinition = stepDefinition;
    }

    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }

}
