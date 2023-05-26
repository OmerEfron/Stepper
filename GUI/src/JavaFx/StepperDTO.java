package JavaFx;

import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionsCollector;
import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;

import StepperEngine.Stepper;
import StepperEngine.StepperReader.Exception.ReaderException;
import StepperEngine.StepperReader.XMLReadClasses.TheStepper;
import StepperEngine.StepperReader.impl.StepperReaderFromXml;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepperDTO {
    private Stepper stepper = new Stepper();
    private Map<String, FlowExecutionsCollector> flowExecutionsCollectorMap = new HashMap<>();
    private List<String> flowNames;

    private boolean isLoaded = false;

    public void load(String filePath) throws FlowBuildException, ReaderException {
        try {
            TheStepper stStepper = new StepperReaderFromXml().read(filePath);
            Stepper tempStepper= new Stepper();
            tempStepper.newFlows(stStepper);
            stepper.newFlows(stStepper);
            setupConsole();
        } catch (ReaderException | FlowBuildException | RuntimeException e ) {
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

}
