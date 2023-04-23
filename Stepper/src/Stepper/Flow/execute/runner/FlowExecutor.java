package Stepper.Flow.execute.runner;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.Flow.api.FlowDefinitionInterface;
import Stepper.Flow.api.StepUsageDeclerationInterface;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.context.StepExecutionContext;
import Stepper.Flow.execute.context.StepExecutionContextClass;
import Stepper.Step.api.DataDefinitionsDeclaration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlowExecutor {

    public void executeFlow(FlowExecution currFlow) {
        StepExecutionContext stepExecutionContext = new StepExecutionContextClass(currFlow.getFlowDefinition());
        getFreeInputs(currFlow.getFlowDefinition(),stepExecutionContext);

        for (StepUsageDeclerationInterface step : currFlow.getFlowDefinition().getSteps()) {
            stepExecutionContext.updateCustomMap(step);
            step.getStepDefinition().invoke(stepExecutionContext, step.getNameToAlias());
        }

    }
    public void getFreeInputs(FlowDefinitionInterface flow,StepExecutionContext stepExecutionContext){
        System.out.println("Before execute flow please enter the free inputs :");
        for(DataDefinitionsDeclaration dataDefinitionsDeclaration:flow.getFreeInputsFromUser()) {
            System.out.println("Enter value for : "+dataDefinitionsDeclaration.userString());
            Scanner scanner = new Scanner(System.in);
            String input=scanner.nextLine();
            Object value;
            if(dataDefinitionsDeclaration.dataDefinition().getType().isInstance(Integer.class)){
                value=Integer.parseInt(input);
            } else if (dataDefinitionsDeclaration.dataDefinition().getType().isInstance(Integer.class)) {
                value=Double.parseDouble(input);
            }else{
                value=input;
            }
            if (!stepExecutionContext.storeValue(dataDefinitionsDeclaration.getAliasName(),value)){
                System.out.println("the value is not the type");
            }

        }
    }
}

//    Integer val=4;
////        stepExecutionContext.storeValue("TIME_TO_SPEND",val);
////        stepExecutionContext.storeValue("FOLDER_NAME","C:\\Users\\roni2\\IdeaProjects\\test");
////        //stepExecutionContext.storeValue("FILTER",".txt",false);
////        stepExecutionContext.storeValue("CONTENT","lalsalcsl");
////        stepExecutionContext.storeValue("FILE_NAME","C:\\Users\\roni2\\IdeaProjects\\test\\newTes.txt");
////        List<File> checkFilesRenamer=testFilesRenamer("C:\\Users\\roni2\\IdeaProjects\\test1");
////        FilesListDataDef filesListDataDef=new FilesListDataDef(checkFilesRenamer);
////        stepExecutionContext.storeValue("FILES_TO_RENAME",filesListDataDef);
////        stepExecutionContext.storeValue("SUFFIX","Israel");
////        stepExecutionContext.storeValue("FILES_LIST",filesListDataDef);
////        stepExecutionContext.storeValue("LINE",1);
//
//    private static List<File> testFilesRenamer(String directoryPath) {
//        // Create a File object for the directory
//        File directory = new File(directoryPath);
//
//        // Get a list of all the files in the directory
//        File[] files = directory.listFiles();
//
//        // Create a list to hold the file names
//        List<File> fileNames = new ArrayList<>();
//
//        // Iterate over the files and add their names to the list
//        for (File file : files) {
//            if (file.isFile()) {
//                fileNames.add(file);
//            }
//        }
//        return fileNames;
//    }
//}
