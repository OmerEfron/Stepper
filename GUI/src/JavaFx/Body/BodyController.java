package JavaFx.Body;

import JavaFx.AppController;

import JavaFx.Body.FlowDefinition.FlowDefinition;

import JavaFx.Body.FlowExecution.FlowExecution;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;

public class BodyController {
    private AppController mainController;
    @FXML private TabPane bodyComponent;
    @FXML private Tab flowDefinitionTab;
    @FXML private FlowDefinition flowDefinitionController;
    @FXML private Tab flowExecutionTab;
    @FXML private FlowExecution flowExecutionController;
    @FXML private Tab UntitledTab;



    @FXML
    public void initialize(){
        flowDefinitionController.setMainController(this);
        flowExecutionController.setMainController(this);
        flowExecutionController.setInputTableCellValueFactory();
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setFlowDetailsList(List<FlowDetails> flowDetails){
        flowDefinitionController.setDataByFlowName(flowDetails);
    }


    public void goToExecuteFlowTab(FlowDetails Flow) {
        flowExecutionController.setFlowToExecute(Flow);
        flowExecutionController.setExecutionInfo();
        bodyComponent.getSelectionModel().select(flowExecutionTab);
    }
}