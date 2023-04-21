package Stepper.Flow.execute.runner;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.Flow.api.StepUsageDeclerationClass;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Flow.execute.context.StepExecutionContextClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FlowExecutor {

    public void executeFlow(FlowExecution currFlow){
        StepExecutionContext stepExecutionContext=new StepExecutionContextClass(currFlow.getFlowDefinition());
        Integer val=4;
        stepExecutionContext.storeValue("TIME_TO_SPEND",val,false);
        stepExecutionContext.storeValue("FOLDER_NAME","C:\\Users\\roni2\\IdeaProjects\\test",false);
        //stepExecutionContext.storeValue("FILTER",".txt",false);
        stepExecutionContext.storeValue("CONTENT","lalsalcsl",false);
        stepExecutionContext.storeValue("FILE_NAME","C:\\Users\\roni2\\IdeaProjects\\test\\newTes.txt",false);
        List<File> checkFilesRenamer=testFilesRenamer("C:\\Users\\roni2\\IdeaProjects\\test1");
        FilesListDataDef filesListDataDef=new FilesListDataDef(checkFilesRenamer);
        stepExecutionContext.storeValue("FILES_TO_RENAME",filesListDataDef,false);
        stepExecutionContext.storeValue("PREFIX","Bibi",false);
        stepExecutionContext.storeValue("SUFFIX","Israel",false);
        stepExecutionContext.storeValue("FILES_LIST",filesListDataDef,false);
        stepExecutionContext.storeValue("LINE",1,false);


        for (StepUsageDeclerationInterface step:currFlow.getFlowDefinition().getSteps()) {
            step.getStepDefinition().invoke(stepExecutionContext);
        }

        System.out.println(stepExecutionContext.getDataValue("RESULT",String.class));
        System.out.println(stepExecutionContext.getDataValue("RESULT2",String.class));
    }

    private static List<File> testFilesRenamer(String directoryPath) {
        // Create a File object for the directory
        File directory = new File(directoryPath);

        // Get a list of all the files in the directory
        File[] files = directory.listFiles();

        // Create a list to hold the file names
        List<File> fileNames = new ArrayList<>();

        // Iterate over the files and add their names to the list
        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file);
            }
        }
        return fileNames;
    }
}
