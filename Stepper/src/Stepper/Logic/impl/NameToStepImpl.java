package Stepper.Logic.impl;


import Stepper.Logic.api.NameToStep;
import Stepper.Step.StepDefinitionRegistry;

public class NameToStepImpl implements NameToStep {
    private final String TimeToSpend="Spend Some Time";
    private final String CollectFilesInFolder ="Collect Files In Folder";
    private final String filesDeleter="Files Deleter";
    private final String filesRenamer ="Files Renamer";
    private final String filesContentExtractor="Files Content Extractor";
    private final String CSVExporter="CSV Exporter";
    private final String propertiesExporter="Properties Exporter";
    private final String fileDumper= "File Dumper";

    @Override
    public boolean isStepInSystem(String name) {
        switch (name) {
            case TimeToSpend:
                return true;
            case CollectFilesInFolder:
                return true;
            case filesDeleter:
                return true;
            case filesRenamer:
                return true;
            case filesContentExtractor:
                return true;
            case CSVExporter:
                return true;
            case propertiesExporter:
                return true;
            case fileDumper:
                return true;
        }
        return false;
    }

    @Override
    public StepDefinitionRegistry getDataDefinitionRegistry(String name) {
        switch (name) {
            case TimeToSpend:
                return StepDefinitionRegistry.TIME_TO_SPEND;
            case CollectFilesInFolder:
                return StepDefinitionRegistry.FILES_COLLECTOR;
            case filesDeleter:
                return StepDefinitionRegistry.FILES_DELETER;
            case filesRenamer:
                return StepDefinitionRegistry.FILES_RENAMER;
            case filesContentExtractor:
                return StepDefinitionRegistry.FILES_CONTENT_EXTRACTOR;
            case CSVExporter:
                return StepDefinitionRegistry.CSV_EXPORTER;
            case propertiesExporter:
                return StepDefinitionRegistry.PROPERTIES_EXPORTER;
            case fileDumper:
                return StepDefinitionRegistry.FILE_DUMPER;
            default:
                return null;
        }
    }
}
