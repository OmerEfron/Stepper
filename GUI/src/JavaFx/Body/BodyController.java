package JavaFx.Body;

import JavaFx.AppController;

import JavaFx.Body.FlowDefinition.FlowDefinition;

import JavaFx.Body.FlowExecution.FlowExecution;
import JavaFx.Body.FlowHistory.FlowHistory;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.Stepper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import javax.swing.event.ChangeListener;
import java.util.List;
import java.util.Optional;

public class BodyController {
    private AppController mainController;
    @FXML private TabPane bodyComponent;
    @FXML private Tab flowDefinitionTab;
    @FXML private FlowDefinition flowDefinitionController;
    @FXML private Tab flowExecutionTab;
    @FXML private FlowExecution flowExecutionController;
    @FXML private Tab flowHistoryTab;
    @FXML private FlowHistory flowHistoryController;
    @FXML private Tab UntitledTab;



    @FXML
    public void initialize(){
        flowDefinitionController.setMainController(this);
        flowExecutionController.setMainController(this);
        flowHistoryController.setMainController(this);
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

    public Stepper getStepper(){
        return mainController.getStepper();
    }

    public void updateFlowHistory() {
        flowHistoryController.setFlowsExecutionTable();
    }
}