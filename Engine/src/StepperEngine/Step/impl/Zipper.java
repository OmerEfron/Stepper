package StepperEngine.Step.impl;

import StepperEngine.DataDefinitions.Enumeration.ZipEnumerator;
import StepperEngine.DataDefinitions.Enumeration.ZipperEnumeration;
import StepperEngine.DataDefinitions.impl.DataDefinitionRegistry;
import StepperEngine.DataDefinitions.impl.StepperZipperEnumeration;
import StepperEngine.Flow.execute.context.StepExecutionContext;
import StepperEngine.Step.api.DataDefinitionDeclarationImpl;
import StepperEngine.Step.api.DataNecessity;
import StepperEngine.Step.api.StepDefinitionAbstract;
import StepperEngine.Step.api.StepStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper extends StepDefinitionAbstract {

    public Zipper(){
        super("Zipper",false);
        addInput(new DataDefinitionDeclarationImpl("SOURCE","Source", DataNecessity.MANDATORY, DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("OPERATION","Operation type", DataNecessity.MANDATORY, DataDefinitionRegistry.ZIPPER_ENUMERATION));
        addOutput(new DataDefinitionDeclarationImpl("RESULT", "Zip operation result", DataNecessity.NA, DataDefinitionRegistry.STRING));
    }

    @Override
    public StepStatus invoke(StepExecutionContext context, Map<String, String> nameToAlias, String stepName) {
        Instant start = Instant.now();
        String zipFilePath = "path/to/output/archive.zip";
        String source=context.getDataValue("SOURCE",String.class);
        ZipperEnumeration operation=context.getDataValue("OPERATION", ZipperEnumeration.class);
        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            File sourceFile = new File(source);

            if (sourceFile.isDirectory()) {
                zipDirectory(sourceFile, sourceFile.getName(), zos);
            } else {
                zipFile(sourceFile, sourceFile.getName(), zos);
            }
            zos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        context.setTotalTime(stepName, Duration.between(start, Instant.now()));
        return context.getStepStatus(stepName);
    }
    private void zipDirectory(File directory, String directoryName, ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipDirectory(file, directoryName + "/" + file.getName(), zos);
                } else {
                    zipFile(file, directoryName + "/" + file.getName(), zos);
                }
            }
        }
    }
    private void zipFile(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(fileToZip);
        zos.putNextEntry(new ZipEntry(fileName));

        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

}
