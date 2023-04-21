package Stepper;

import Stepper.Flow.api.FlowDefinition;
import Stepper.Flow.api.StepUsageDeclerationClass;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.runner.FlowExecutor;
import Stepper.Logic.ReadStepper.api.Reader.StepperReader;
import Stepper.Logic.ReadStepper.api.Reader.StepperReaderFromXml;
import Stepper.Logic.ReadStepper.TheStepper;
import Stepper.Logic.api.StepperDefinitionLogic;
import Stepper.Logic.impl.StepperDefinitionLogicImpl;
import Stepper.Step.impl.*;

public class Main {

    public static void main(String[] argv){
        FlowDefinition flowDefinition=new FlowDefinition("test","lior Bark ben sharmot");
        flowDefinition.addStep(new StepUsageDeclerationClass(new SpendSomeTimeStep()));
        flowDefinition.addStep(new StepUsageDeclerationClass(new CollectFilesInFolder()));
        //flowDefinition.addStep(new StepUsageDeclerationClass(new FilesDeleter()));
        flowDefinition.addStep(new StepUsageDeclerationClass(new FileDumper()));
        flowDefinition.addStep(new StepUsageDeclerationClass(new FilesRenamer()));
        flowDefinition.addStep(new StepUsageDeclerationClass(new FilesContentExtractor()));
        flowDefinition.addStep(new StepUsageDeclerationClass(new CSVExporter()));
        flowDefinition.addStep(new StepUsageDeclerationClass(new PropertiesExporter()));

        FlowExecution flowExecution= new FlowExecution(flowDefinition,"1");
        FlowExecutor test=new FlowExecutor();
        test.executeFlow(flowExecution);
        StepperReader stepperReader = new StepperReaderFromXml();
        TheStepper stStepper = stepperReader.read("C:\\Users\\roni2\\IdeaProjects\\ex1 (2).xml");
        StepperDefinitionLogic stepperDefinitionLogic=new StepperDefinitionLogicImpl();
        System.out.println(stepperDefinitionLogic.validateStepper(stStepper));

    }


}
