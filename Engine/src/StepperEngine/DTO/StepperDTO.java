package StepperEngine.DTO;

import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionsCollector;
import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;

import StepperEngine.Flow.execute.FlowExecution;
import StepperEngine.Stepper;
import StepperEngine.StepperReader.Exception.ReaderException;
import StepperEngine.StepperReader.XMLReadClasses.TheStepper;
import StepperEngine.StepperReader.impl.StepperReaderFromXml;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StepperDTO {
    private Stepper stepper = new Stepper();
    private Map<String, FlowExecutionsCollector> flowExecutionsCollectorMap = new HashMap<>();
    private List<String> flowNames;

    private boolean isLoaded = false;



    public void load(String filePath) throws FlowBuildException, ReaderException {
        try {
            if(isLoaded)
                stepper = new Stepper();
            TheStepper stStepper = new StepperReaderFromXml().read(filePath);
            Stepper tempStepper= new Stepper();
            tempStepper.newFlows(stStepper);
            stepper.newFlows(stStepper);
            setupConsole();
            isLoaded=true;
        } catch (ReaderException | FlowBuildException | RuntimeException e ) {
            if(!isLoaded)
                isLoaded=false;
            throw e;
        }

    }
    private void setupConsole() {
        flowNames = stepper.getFlowNames();
        flowExecutionsCollectorMap.clear();
        flowNames.forEach(flowName -> flowExecutionsCollectorMap.put(flowName, new FlowExecutionsCollector(flowName)));
        isLoaded = true;
    }

    public List<FlowDetails> getFlowsDetailsList() {
        return stepper.getFlowsDetails();
    }

    public boolean hasFlowName(String name){
        return stepper.isFlowExist(name);
    }
    public Optional<FlowExecution> getFlowExecution(String name){
        return Optional.ofNullable(stepper.getFlowExecution(name));
    }

    public Stepper getStepper(){
        return stepper;
    }



}
