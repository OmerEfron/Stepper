package StepperConsole;

import StepperConsole.Execute.Flow.impl.IOData;
import StepperConsole.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperConsole.FlowDetails.StepDetails.FlowIODetails.Output;
import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;
import StepperEngine.StepperReader.XMLReadClasses.TheStepper;
import StepperEngine.StepperReader.Exception.ReaderException;
import StepperEngine.StepperReader.impl.StepperReaderFromXml;
import StepperEngine.Stepper;
import StepperConsole.Execute.ExecutionsStatistics.api.FlowExecutionStatsDefinition;
import StepperConsole.Execute.ExecutionsStatistics.impl.FlowExecutionStatsDefinitionImpl;
import StepperConsole.Execute.ExecutionsStatistics.api.StepExecutionStats;
import StepperConsole.Execute.api.Executor;
import StepperConsole.Execute.impl.ExecutorImpl;
import StepperConsole.Execute.Flow.api.FlowExecutionData;
import StepperConsole.Execute.Flow.impl.FlowExecutionsCollector;
import StepperConsole.FlowDetails.FlowDetails;
import StepperConsole.FlowDetails.StepDetails.StepDetails;
import StepperConsole.Scanner.InputFromUser;
import StepperConsole.Scanner.InputFromUserImpl;
import javafx.util.Pair;

import java.util.*;

public class StepperConsoleDefinitionImpl implements StepperConsoleDefinition{
    private final Stepper stepper = new Stepper();
    private final InputFromUser inputFromUser=new InputFromUserImpl();
    private Map<String,FlowExecutionsCollector> flowExecutionsCollectorMap = new HashMap<>();
    private List<String> flowNames;
    private ConsoleStatus consoleStatus = ConsoleStatus.RUN;
    private boolean isLoaded = false;
    public static void main(String[] args){
        StepperConsoleDefinition stepperConsoleDefinition=new StepperConsoleDefinitionImpl();
        stepperConsoleDefinition.run();
    }

    /**
     * runs the console until the user exits
     */
    @Override
    public void run() {
        welcomeUser();
        while(consoleStatus == ConsoleStatus.RUN){
            StepperConsoleOptions choice = getUserChoice();
            if(!askToLoadIfNotLoaded(choice)) {
                switch (choice) {
                    case LOAD:
                        load();
                        break;
                    case SHOW_FLOW:
                        showFlowDetails();
                        break;
                    case EXECUTE_FLOW:
                        executeFlow();
                        break;
                    case SHOW_EXECUTE_HISTORY:
                        showExecuteHistory();
                        break;
                    case SHOW_STATS:
                        showStats();
                        break;
                    case EXIT:
                        exit();
                }
            }
        }

    }

    /**
     * prints a welcome greeting to the user.
     */
    private void welcomeUser(){
        System.out.println("Welcome to Stepper. A place that all of your steps connects in a glorious way to one " +
                "marvelous flow.");
    }

    /**
     * a method to check the validity of the user's choice.
     * If the user chose to see history executions or flow details and so, but there are no flows in the
     * system, returns true. else false.
     * @param choice - the option the user chose
     * @return true if there are no flows in the system.
     */
    private boolean askToLoadIfNotLoaded(StepperConsoleOptions choice) {
        if((choice != StepperConsoleOptions.LOAD && choice != StepperConsoleOptions.EXIT) && (!isLoaded || stepper.getNumOfFlows() == 0)){
            System.out.println("There are no flows yet in the system. Try load some from main menu and then try again. ");
            return true;
        }
        return false;
    }


    /**
     * a method to get the user choice from the main menu.
     * prints the options from the Enum StepperConsoleOptions, and returns the one he chose.
     * @return StepperConsoleOption value that the user chose.
     */
    private StepperConsoleOptions getUserChoice() {
        System.out.println("Please choose one of the following options, by entering the number near the desired option:");
        for(StepperConsoleOptions option:StepperConsoleOptions.values()){
            System.out.println(option.getValue() + ". " + option.getUserString());
        }
        return StepperConsoleOptions.getOption(inputFromUser.getIntByRange(StepperConsoleOptions.values().length));
    }

    /**
     * a method to load a new xml file to the system.
     * Uses the TheStepper class to first read it from the xml file
     * then, converts it to a Stepper, if it's valid.
     * If it is not an application-valid file, prints a message with the problem in the file and the file has not been
     * loaded.
     */
    @Override
    public void load() {
        System.out.println("Please enter file path to load an xml application-wise file.\n" +
                "Make sure it's a valid .xml file!");
        String filePath = getFilePath();

        try {
            TheStepper stStepper = new StepperReaderFromXml().read(filePath);
            stepper.newFlows(stStepper);
            setupConsole();
        } catch (ReaderException | FlowBuildException | RuntimeException e ) {
            lineSpace();
            System.out.println(e.getMessage());
            seperateBlocksOfContent();
        }
    }

    /**
     * a private method to set up the console after it has been loaded.
     * sets the flowNames and a collection of executions.
     */
    private void setupConsole() {
        flowNames = stepper.getFlowNames();
        flowExecutionsCollectorMap.clear();
        flowNames.forEach(flowName -> flowExecutionsCollectorMap.put(flowName, new FlowExecutionsCollector(flowName)));
        isLoaded = true;
        System.out.println("Stepper has been loaded successfully!");
    }

    /**
     * a private method to get from user the file path for the xml file
     * @return String - the file path from user.
     */
    private static String getFilePath() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * The method to show a flow details from system.
     */
    @Override
    public void showFlowDetails() {
        FlowDetails flowDetails = stepper.buildShowFlow(getFlowFromUser());
        printFlowDetails(flowDetails);
    }

    /**
     * Prints the flow details, from the FlowDetails instance.
     * @param flowDetails - the flow to show
     */
    private static void printFlowDetails(FlowDetails flowDetails) {
        seperateBlocksOfContent();
        printFlowNameAndDescription(flowDetails);
        lineSpace();
        printFormalOutputs(flowDetails.getFormalOutputs());
        lineSpace();
        printIfFlowReadOnly(flowDetails);
        lineSpace();
        printSteps(flowDetails.getSteps());
        lineSpace();
        printFreeInputs(flowDetails.getFreeInputs());
        lineSpace();
        printOutputs(flowDetails);
        seperateBlocksOfContent();
    }

    /**
     * prints the flowDetails outputs
     * @param flowDetails - the flow to display it outputs
     */
    private static void printOutputs(FlowDetails flowDetails) {
        System.out.println("The outputs produced by the flow: ");
        for(Output output: flowDetails.getOutputs()){
            System.out.println("Name:" + output.getDataName() + ", Type: " + output.getTypeName() +
                    ", Step related: " + output.getStepRelated());
        }
    }


    /**
     * prints the flowDetails name and description
     * @param flowDetails - the flow to display it outputs
     */
    private static void printFlowNameAndDescription(FlowDetails flowDetails) {
        System.out.println("Flow Name: " + flowDetails.getFlowName());
        System.out.println("Flow description: "+ flowDetails.getFlowDescription());
    }

    /**
     * prints the flowDetails formal outputs
     * @param formalOutputs the list of the formal outputs
     */
    private static void printFormalOutputs(List<String> formalOutputs) {
        if(formalOutputs.isEmpty()){
            System.out.println("Flow has no formal outputs");
        }
        else{
            System.out.println("Flow's formal outputs: ");
            formalOutputs.forEach(System.out::println);
        }
    }

    /**
     * prints the flowDetails readOnly definition
     * @param flowDetails - the flow to display it outputs
     */
    private static void printIfFlowReadOnly(FlowDetails flowDetails) {
        System.out.println("The flow is "+ (flowDetails.isFlowReadOnly()? "":"not " + "read only"));
    }

    /**
     * prints the flowDetails steps
     * @param steps - the steps list of the flow
     */
    private static void printSteps(List<StepDetails> steps) {
        if(steps.isEmpty()){
            System.out.println("The flow has no steps.");
        }
        else {
            System.out.println("The flow's steps are:");
            steps.forEach(step -> System.out.println(step.getStepName() + ", " + (step.isReadOnly() ?
                    "is read only" : "is not read only")));
        }
    }

    /**
     * prints the flowDetails freeInputs
     * @param freeInputs - the inputs list of the flow
     */
    private static void printFreeInputs(List<Input> freeInputs) {
        System.out.println("The free inputs are: ");
        freeInputs.forEach(input -> {
            System.out.println("Name: " + input.getDataName() + ", type: " + input.getTypeName() + ", " + input.getNecessity());
            System.out.println("Related to steps: ");
            input.getRelatedSteps().forEach(System.out::println);
        });
    }


    /**
     * The method that executes a flow.
     * First it gets from the user which flow he would like to execute
     * Then executes the flow using the Executor instance.
     */
    @Override
    public void executeFlow() {
        Executor executor = new ExecutorImpl(stepper);
        String flowName = getFlowFromUser();
        execute(executor, flowName);
    }

    /**
     * Execute the flow that it's name is flowName, using executor instance.
     * @param executor - an Executor instance.
     * @param flowName - the flow name to execute.
     */
    private void execute(Executor executor, String flowName) {
        Optional<FlowExecutionData> optionalFlowExecutionData = executor.executeFlow(flowName,inputFromUser);
        printExecutionDetailsAfterExecution(optionalFlowExecutionData);
    }

    /**
     * prints an Execution details, if accured. else does nothing.
     * @param optionalFlowExecutionData - an optional instance of FlowExecutionData. if param.isPresent null,
     *                                  then the execution did not happened for some reason.
     */
    private void printExecutionDetailsAfterExecution(Optional<FlowExecutionData> optionalFlowExecutionData) {
        if(optionalFlowExecutionData.isPresent()){
            FlowExecutionData flowExecutionData = optionalFlowExecutionData.get();
            flowExecutionsCollectorMap.get(optionalFlowExecutionData.get().getFlowName()).addFlowExecutionData(optionalFlowExecutionData.get());
            seperateBlocksOfContent();
            printExecutionMetaData(flowExecutionData);
            lineSpace();
            printExecutionResult(flowExecutionData);
            lineSpace();
            printFlowFormalOutputsContentWithUserString(flowExecutionData);
            seperateBlocksOfContent();
        }
    }

    /**
     * Prints a flow execution's formal output with their content.
     * @param flowExecutionData - the execution to print
     */
    private static void printFlowFormalOutputsContentWithUserString(FlowExecutionData flowExecutionData) {
        for(IOData output: flowExecutionData.getFormalOutputs()){
            System.out.println(output.getUserString());
            System.out.println(output.getContent());
        }
    }

    /**
     * The method to let the user see a history of a flow execution.
     * recieve from the user the flow to show it history, and the execution of it to show
     * prints it to the screen.
     */
    @Override
    public void showExecuteHistory() {
        System.out.println("Please choose which flow you want to see an execution of it:\n");
        FlowExecutionsCollector flowExecutionsCollector = getFlowExecutionsCollector();
        if(notExecuted(flowExecutionsCollector)){
            printNoExecutionYet(flowExecutionsCollector);
        }
        else {
            String uuid = getExecutionUUIDFromUser(flowExecutionsCollector);
            printFlowExecutionHistory(flowExecutionsCollector.getFlowExecutionData(uuid));
        }
    }

    private static boolean notExecuted(FlowExecutionsCollector flowExecutionsCollector) {
        return flowExecutionsCollector.getNumOfExecutions() == 0;
    }

    private static void printNoExecutionYet(FlowExecutionsCollector flowExecutionsCollector) {
        System.out.println("Flow "+ flowExecutionsCollector.getFlowName() + " has not been executed yet." +
                "Try execute and then you can see it's details here.");
    }

    /**
     * Gets from the user the flow he wants to watch one of it past executions.
     * Then gets the collection of the flow's executions.
     * @return an Instance that represents the flow's past executions.
     */
    private FlowExecutionsCollector getFlowExecutionsCollector() {
        String flowName = getFlowFromUser();
        return flowExecutionsCollectorMap.get(flowName);
    }

    /**
     * prints to the user the list of the past executions, so He could choose one, and gets it from him.
     * @param flowExecutionsCollector the flow to get from the user it uuid of the execution he wants.
     * @return the chosen uuid of the execution the user asked.
     */
    private String getExecutionUUIDFromUser(FlowExecutionsCollector flowExecutionsCollector) {
        flowExecutionsCollector.getFlowExecutionByNumber().forEach((id, name) -> System.out.println(id + ". " + name));
        return getUUID(flowExecutionsCollector);
    }

    /**
     * Gets from a user a valid uuid of a past execution.
     * @param flowExecutionsCollector the flow's collection of executions
     * @return the uuid chosen by the user.
     */
    private String getUUID(FlowExecutionsCollector flowExecutionsCollector) {
        return flowExecutionsCollector.getFlowExecutionByNumber().get(inputFromUser.getIntByRange(
                flowExecutionsCollector.getFlowExecutionByNumber().size()));
    }


    /**
     * prints all the details of a flow past execution.
     * @param flowExecutionData an instance of a past flow execution to print.
     */
    private static void printFlowExecutionHistory(FlowExecutionData flowExecutionData) {
        seperateBlocksOfContent();
        printExecutionMetaData(flowExecutionData);
        lineSpace();
        printExecutionResult(flowExecutionData);
        lineSpace();
        printFlowMainData(flowExecutionData);
        seperateBlocksOfContent();
    }

    private static void printExecutionMetaData(FlowExecutionData flowExecutionData) {
        System.out.println("UUID: "+ flowExecutionData.getUniqueExecutionId());
        System.out.println("Name: " + flowExecutionData.getFlowName());
    }

    private static void printExecutionResult(FlowExecutionData flowExecutionData) {
        System.out.println("execution final result: "+ flowExecutionData.getFlowExecutionFinalResult());
    }

    private static void printFlowMainData(FlowExecutionData flowExecutionData) {
        if(!flowExecutionData.getFlowExecutionFinalResult().equals("NOT_INVOKED")) {
            printFlowDuration(flowExecutionData);
            lineSpace();
            printFlowInputs(flowExecutionData);
            lineSpace();
            printFlowOutputs(flowExecutionData);
            lineSpace();
            printSteps(flowExecutionData);
        }
    }

    private static void printSteps(FlowExecutionData flowExecutionData) {
        System.out.println("Steps execution data: ");
        flowExecutionData.getStepExecuteDataList().forEach(step -> {
            System.out.println("Name: "+step.getFinalName());
            System.out.println("Duration (in milliseconds): " + step.getTotalTime().toMillis());
            System.out.println("Result: " + step.getStepStatus());
            System.out.println("Summery line: " + step.getInvokeSummery());
            System.out.println("Logs: ");
            for(Pair<String, String> log : step.getLogs()){
                System.out.println(log.getKey()+ " - " + log.getValue());
            }
            lineSpace();
        });
    }

    private static void printFlowOutputs(FlowExecutionData flowExecutionData) {
        System.out.println("Outputs:");
        flowExecutionData.getOutputs().forEach(output ->{
            System.out.println("Name: "+output.getName());
            System.out.println("Type: " + output.getType());
            System.out.println("Content: " + output.getContent());
        });
    }

    private static void printFlowDuration(FlowExecutionData flowExecutionData) {
        System.out.println("Duration (in milliseconds): " + flowExecutionData.getFlowExecutionDuration());
    }

    private static void printFlowInputs(FlowExecutionData flowExecutionData) {
        System.out.println("Inputs:");
        flowExecutionData.getFreeInputs()
                .forEach(data -> {
                            System.out.println("Name: " + data.getName());
                            System.out.println("Content: " + data.getContent());
                            System.out.println("Type: " + data.getType());
                            System.out.println("Necessity: " + data.getNecessity());
                        }
                );
    }


    /**
     * shows a flow's statistics of past executions.
     * gets from user the flow he wants to see it statistics, and then prints it.
     */
    @Override
    public void showStats() {
        String flowName = getFlowFromUser();
        if (notExecuted(flowExecutionsCollectorMap.get(flowName))){
            printNoExecutionYet(flowExecutionsCollectorMap.get(flowName));
        }
        else {
            FlowExecutionStatsDefinition flowExecutionStatsDefinition = new FlowExecutionStatsDefinitionImpl(
                    flowExecutionsCollectorMap.get(flowName));
            printStats(flowExecutionStatsDefinition);
        }
    }

    private static void printStats(FlowExecutionStatsDefinition flowExecutionStatsDefinition) {
        seperateBlocksOfContent();
        printFlowStats(flowExecutionStatsDefinition);
        lineSpace();
        printFlowStepsStats(flowExecutionStatsDefinition);
        seperateBlocksOfContent();
    }

    private static void printFlowStats(FlowExecutionStatsDefinition flowExecutionStatsDefinition) {
        System.out.println(flowExecutionStatsDefinition.getFlowName() + " executions stats: ");
        System.out.println("Number of executions:  "+ flowExecutionStatsDefinition.getNumOfExecutions());
        System.out.println("Average time of execution duration: " + flowExecutionStatsDefinition.getAvgTimeOfExecutions());
    }

    private static void printFlowStepsStats(FlowExecutionStatsDefinition flowExecutionStatsDefinition) {
        System.out.println("Steps stats: ");
        for(StepExecutionStats stepExecutionStats: flowExecutionStatsDefinition.getStepExecutionsStats()){
            System.out.println("Step name: " + stepExecutionStats.getStepName());
            System.out.println("Step number of executions: " + stepExecutionStats.getNumOfExecutions());
            System.out.println("Step Average time of execution duration: " + stepExecutionStats.getAvgTimeOfExecutions());
        }
    }

    /**
     * changes the console status to EXIT so the program will end.
     */
    @Override
    public void exit() {
        System.out.println("Goodbye!");
        consoleStatus = ConsoleStatus.EXIT;
    }

    /**
     * gets a flow name from the user, by the matching index of it in the flow list.
     * @return the name of the chosen flow
     */
    private String getFlowFromUser() {
        System.out.println("Please choose a flow, by his number.");
        printFlowsOrderList();
        return getFlowName();
    }

    /**
     * gets from the user the index of the wanted flow, and extract the name of it.
     * @return the name of the chosen flow
     */
    private String getFlowName(){
        Integer choice = inputFromUser.getIntByRange(stepper.getNumOfFlows());
        return stepper.getFlowsByNumber().get(choice);
    }

    private void printFlowsOrderList() {
        stepper.getFlowsByNumber().forEach((place, name) -> System.out.println(place + ". " + name));
    }

    private static void seperateBlocksOfContent() {
        System.out.println("----------------------------------------------------");
    }

    private static void lineSpace() {
        System.out.println();
    }

}
