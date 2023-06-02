package StepperEngine;

import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowDetails.FlowDetailsImpl;
import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;
import StepperEngine.Flow.api.FlowDefinitionImpl;
import StepperEngine.Flow.api.FlowDefinition;
import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Flow.execute.runner.FlowExecutor;
import StepperEngine.DTO.FlowExecutionData.api.FlowExecutionData;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperEngine.StepperReader.XMLReadClasses.Continuation;
import StepperEngine.StepperReader.XMLReadClasses.ContinuationMapping;
import StepperEngine.StepperReader.XMLReadClasses.Flow;
import StepperEngine.StepperReader.XMLReadClasses.TheStepper;


import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * a class that repesents the engine of the system.
 */
public class Stepper implements Serializable {
    private List<FlowDefinition> flows = new ArrayList<>();

    private List<String> flowNames = new ArrayList<>();

    private Map<String, FlowDefinition> flowsMap = new HashMap<>();

    private Map<Integer, String> flowsByNumber = new LinkedHashMap<>();

    private Map<String, FlowExecution> executionsMap = new HashMap<>();
    private Map<String,List<String>> continuationMap=new HashMap<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(6);


    public Stepper(){

    }

    public void newFlows(TheStepper stepper) throws FlowBuildException {
        List<FlowDefinition> newFlows = new ArrayList<>();
        Map<String, FlowDefinition> newFlowsMap = new HashMap<>();
        checkForDuplicateNames(stepper);
        checkForDuplicateOutputs(stepper);
        stepper.getFlows().getFlows().stream()
                .forEach(flow -> {
                    try {
                        newFlows.add(new FlowDefinitionImpl(flow));
                    } catch (FlowBuildException e) {
                        throw new RuntimeException(e);
                    }
                });
        flows = newFlows;
        flows.forEach(flow-> newFlowsMap.put(flow.getName(), flow));
        flowsMap = newFlowsMap;
        flowsByNumber  = IntStream.range(0, flows.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, i -> flows.get(i).getName()));
        flowNames = flows.stream()
                .map(FlowDefinition::getName)
                .collect(Collectors.toList());

        try {
            checkContinuation();
        } catch (FlowBuildException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * The function checks that the continuity setting is correct(steps names ,inputs names,outputs names)
     * @throws FlowBuildException
     */
    private void checkContinuation() throws FlowBuildException{
        for (FlowDefinition flow:flows){
            if (flow.hasContinuation())
            {
                for(Continuation continuation:flow.getContinuation()) {
                    try {
                        checkIfContinuationValid(flow, continuation);
                        if(!continuationMap.containsKey(flow.getName()))
                            continuationMap.put(flow.getName(),new ArrayList<>());
                        continuationMap.get(flow.getName()).add(continuation.getTargetFlow());
                    }catch (FlowBuildException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void checkIfContinuationValid(FlowDefinition flow, Continuation continuation) throws FlowBuildException {
        if (!flowsMap.containsKey(continuation.getTargetFlow()))
            throw new FlowBuildException("The flow " + continuation.getTargetFlow() + " doesn't exist", flow.getName());
        else
        {
            try {
                flowsMap.get(flow.getName()).isOutputsExist(continuation.getContinuationMappings());
                flowsMap.get(continuation.getTargetFlow()).isInputsExist(continuation.getContinuationMappings());
                for(ContinuationMapping continuationMapping:continuation.getContinuationMappings()){
                    if(!flow.getAllDataDefinitions().get(continuationMapping.getSourceData()).dataDefinition().getType()
                            .equals(flowsMap.get(continuation.getTargetFlow()).getFreeInputByName(continuationMapping.getTargetData()).dataDefinition().getType())){
                        throw new FlowBuildException(flow.getName(),"Can't create continuation mapping between: "
                        + continuationMapping.getSourceData()+" and "+continuationMapping.getTargetData()+".\n" +
                                "Are not the same type!");
                    }
                    else {
                        flowsMap.get(continuation.getTargetFlow()).addContinuationMapping(continuationMapping.getSourceData(),continuationMapping.getTargetData());
                    }
                }
            }catch (FlowBuildException e) {
                throw e;
            }
        }
    }

    public boolean isFlowExist(String name){
        return flowNames.contains(name);
    }

    public List<String> getContinuationList (FlowExecution flowExecution){
        return continuationMap.get(flowExecution.getFlowDefinition().getName());
    }
    public FlowExecution applyContinuation(FlowExecution pastFlow,String continuationFlow) {
        FlowExecution flowExecution=new FlowExecution(flowsMap.get(continuationFlow));
        flowExecution.applyContinuation(pastFlow);
        return flowExecution;
    }
        public List<String> getFlowNames() {
        return flowNames;
    }

    public Integer getNumOfFlows(){
        return flows.size();
    }

    public Map<Integer, String> getFlowsByNumber() {
        return flowsByNumber;
    }

    public String flowNameByNumber(Integer i){
        return flows.get(i - 1).getName();
    }

    private void checkForDuplicateOutputs(TheStepper stepper) throws FlowBuildException {
        String duplicateOutput;
        for(Flow flow: stepper.getFlows().getFlows()){
            if((duplicateOutput = checkForDuplicatesOutputsInFlow(flow)) != null)
                throw new FlowBuildException("Duplicate output names: "+duplicateOutput, flow.getName());
        }
    }

    public String checkForDuplicatesOutputsInFlow(Flow flow){
        Set<String> uniqueOutputs = new HashSet<>();
        for(String output: flow.getFlowOutput().split(",")){
            if(!uniqueOutputs.add(output))
                return output;
        }
        return null;
    }



    private static void checkForDuplicateNames(TheStepper stepper) throws FlowBuildException {
        String duplicate;
        if((duplicate = findDuplicateFlowName(stepper)) != null){
            throw new FlowBuildException("Duplicate flow error. " + duplicate + " is already exist", duplicate);
        }
    }

    private static String findDuplicateFlowName(TheStepper stepper) {
        Set<String> flowNames = new HashSet<>();
        for (Flow flow : stepper.getFlows().getFlows()) {
            if (!flowNames.add(flow.getName())) {
                return flow.getName();
            }
        }
        return null;
    }


    public FlowExecution getFlowExecution(int flowNumber){
        FlowExecution flowExecution=new FlowExecution(flows.get(flowNumber-1) );
        return flowExecution;
    }

    public FlowExecution getFlowExecution(String flowName){
        if(!isFlowExist(flowName)){
            return null;
        }
        return new FlowExecution(flowsMap.get(flowName));
    }

    public FlowExecution buildFlowExecution(String flowName){
        return new FlowExecution(flowsMap.get(flowName));
    }

    public void ExecuteFlow(FlowExecution flowExecution){
        FlowExecutor flowExecutor=new FlowExecutor();
        executorService.submit(() -> {
            flowExecutor.executeFlow(flowExecution);
            synchronized (this){
                executionsMap.put(flowExecution.getFlowDefinition().getName(), flowExecution);
            }
        });

    }
    public FlowExecutionData ExecuteFlow2(FlowExecution flowExecution){
        FlowExecutor flowExecutor=new FlowExecutor();
        flowExecutor.executeFlow(flowExecution);
        executionsMap.put(flowExecution.getFlowDefinition().getName(), flowExecution);
        return new FlowExecutionDataImpl(flowExecution);
    }
    public FlowDetails showFlowByName(String flowName){
        Optional<FlowDefinition> flowByName = flows.stream()
                .filter(flow->flow.getName().equals(flowName))
                .findFirst();
        return flowByName.map(FlowDetailsImpl::new).orElse(null);
    }
    public FlowDetails showFlowByNumber(int flowNumber){

        return new FlowDetailsImpl(flows.get(flowNumber-1));
    }

    public List<FlowDetails> getFlowsDetails(){
        return flows.stream().map(FlowDetailsImpl::new).collect(Collectors.toList());
    }

    public FlowDetails buildShowFlow(String flowName){
        return new FlowDetailsImpl(flowsMap.get(flowName));
    }
    public String getNamesOfFlowsToPrint(){
        return IntStream.range(0, flows.size())
                .mapToObj(i -> (i + 1) + "." + flows.get(i).getName() + (i == flows.size() - 1 ? "" : "\n"))
                .collect(Collectors.joining());
    }


    public boolean isFlowExsitByNumber(int flowNumber) {
        return flows.size() > flowNumber-1 && flowNumber >0;
    }
}
