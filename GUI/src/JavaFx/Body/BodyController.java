package JavaFx.Body;

import DataPresenter.DataPresentation;
import DataPresenter.DataPresentationImpl;
import JavaFx.AppController;

import JavaFx.Body.ExecutionData.ExecutionData;
import JavaFx.Body.ExecutionData.FlowExecutionDataImpUI;
import JavaFx.Body.FlowDefinition.FlowDefinition;

import JavaFx.Body.FlowExecution.FlowExecution;
import JavaFx.Body.FlowHistory.FlowHistory;
import JavaFx.Body.FlowStats.FlowStats;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperEngine.Stepper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BodyController {

    @FXML private TabPane bodyComponent;
    @FXML private Tab flowDefinitionTab;
    @FXML private FlowDefinition flowDefinitionController;
    @FXML private Tab flowExecutionTab;
    @FXML private FlowExecution flowExecutionController;
    @FXML private Tab flowHistoryTab;
    @FXML private FlowHistory flowHistoryController;
    @FXML private Tab flowStatsTab;

    @FXML private FlowStats flowStatsController;

    private Map<String, ExecutionData> executionDataMap=new HashMap<>();
    private AppController mainController;

    @FXML
    public void initialize(){
        flowDefinitionController.setMainController(this);
        flowExecutionController.setMainController(this);
        flowHistoryController.setMainController(this);
        flowStatsController.setMainController(this);

        bodyComponent.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && newTab == flowHistoryTab) {
                // Update the TableView with information
                flowHistoryController.setFlowsExecutionTable();
            }
        });
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setFlowDetailsList(List<FlowDetails> flowDetails){
        flowDefinitionController.setDataByFlowName(flowDetails);
    }

    public Optional<StepperEngine.Flow.execute.FlowExecution> getFlowExecution(String flowName){
        return mainController.getFlowExecution(flowName);
    }

    public void goToExecuteFlowTab(FlowDetails flow) {
        flowExecutionController.setFlowToExecute(flow);
        bodyComponent.getSelectionModel().select(flowExecutionTab);
    }

    public void updateStats(String flowName){
        flowStatsController.updateStats(flowName);
    }

    public Stepper getStepper(){
        return mainController.getStepper();
    }

    public void updateFlowHistory() {
        flowHistoryController.setFlowsExecutionTable();
    }

    public void initStats(List<String> flowNames){
        flowStatsController.initStats(flowNames);
    }

    public ExecutionData getFlowExecutionData(FlowExecutionDataImpl flow){
        if(!executionDataMap.containsKey(flow.getUniqueExecutionId()))
            executionDataMap.put(flow.getUniqueExecutionId(),new FlowExecutionDataImpUI(flow));
        return executionDataMap.get(flow.getUniqueExecutionId());
    }

}