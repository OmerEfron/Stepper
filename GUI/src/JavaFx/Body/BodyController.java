package JavaFx.Body;

import JavaFx.AppController;
import JavaFx.Body.FlowDefnition.FlowDefnitionController;
import JavaFx.Body.Test.FlowDefinitionTest;
import StepperEngine.FlowDetails.FlowDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class BodyController {
    private AppController mainController;
    @FXML
    private TabPane bodyComponent;

    @FXML
    private Tab flowDefinition;
//    @FXML
//    private FlowDefnitionController flowDefnitionController;
    @FXML
    private FlowDefinitionTest flowDefnitionController;

    @FXML
    private Tab flowsExecution;

    @FXML
    public void initialize(){
//        flowDefnitionController.setMainController(this);
        flowDefnitionController.setMainController(this);
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

//    public void setFlowDetailsList(List<FlowDetails> flowDetails){
//        flowDefnitionController.setFlowDetails(flowDetails);
//    }
public void setFlowDetailsList(List<FlowDetails> flowDetails){
        flowDefnitionController.setFlowDetails(flowDetails);
    }


}