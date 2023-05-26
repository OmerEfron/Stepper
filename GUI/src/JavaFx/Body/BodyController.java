package JavaFx.Body;

import JavaFx.AppController;

import JavaFx.Body.FlowDefinition.FlowDefinition;

import JavaFx.Body.FlowExecution.FlowExecution;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import javafx.scene.control.TabPane;

import java.util.List;

public class BodyController {
    private AppController mainController;
    @FXML private TabPane bodyComponent;
    @FXML private Tab flowDefinition;
    @FXML private FlowDefinition flowDefnitionController;
    @FXML private Tab flowExecution;
    @FXML private FlowExecution flowExecutionController;
    @FXML private Tab UntitledTab;



    @FXML
    public void initialize(){
        flowDefnitionController.setMainController(this);
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setFlowDetailsList(List<FlowDetails> flowDetails){
        flowDefnitionController.setDataByFlowName(flowDetails);
    }


    public void goToExecuteFlowTab(FlowDetails Flow) {
        bodyComponent.getSelectionModel().select(flowExecution);
    }
}