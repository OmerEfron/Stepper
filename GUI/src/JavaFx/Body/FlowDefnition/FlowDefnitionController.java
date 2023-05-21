package JavaFx.Body.FlowDefnition;


import JavaFx.AppController;
import JavaFx.Body.BodyController;
import StepperEngine.FlowDetails.FlowDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class FlowDefnitionController {
    private List<FlowDetails> flowDetails;
    private BodyController bodyController;

    @FXML
    private VBox flowsInfo;
    @FXML
    private Label flowDescription;

    @FXML
    private Label flowName;

    @FXML
    private TreeView<String> flowsTreeView;
    @FXML
    private Label isReadOnlyFlow;
    @FXML
    private ListView<String> formalOutputs;

    @FXML
    void treeMouseClick(MouseEvent event) {

        if(event.getClickCount()==2) {
            TreeItem<String> node = flowsTreeView.getSelectionModel().getSelectedItem();
            if(!node.isLeaf()) {
                FlowDetails currFlow=getFlowDetails(node.getValue());
                flowName.setText(node.getValue());
                flowDescription.setText(currFlow.getFlowDescription());
                isReadOnlyFlow.setText(currFlow.isFlowReadOnlyString());
                ObservableList<String> observableList = FXCollections.observableList(currFlow.getFormalOutputs());
                formalOutputs.setItems(observableList);
            }
        }
    }
    public void setMainController(BodyController bodyController) {
        this.bodyController = bodyController;
        flowsTreeView.setRoot(new TreeItem<>("Stepper"));

    }
    public void updateTreeView(){
        TreeItem<String> rootItem = flowsTreeView.getRoot();
        for(FlowDetails flow :flowDetails){
            TreeItem<String> descriptionItem=new TreeItem<>(flow.getFlowDescription());
            TreeItem<String> numOfStepsItem=new TreeItem<>("Number of steps : "+flow.getSteps().size());
            TreeItem<String> freeInputsItem=new TreeItem<>("Number of free inputs : "+flow.getFreeInputs().size());
            TreeItem<String> flowName=new TreeItem<>(flow.getFlowName());
            flowName.getChildren().addAll(descriptionItem,numOfStepsItem,freeInputsItem);
            rootItem.getChildren().add(flowName);

        }

    }
    public void setFlowDetails(List<FlowDetails> flowDetails) {
        flowsTreeView.getRoot().getChildren().clear();
        this.flowDetails = flowDetails;
        updateTreeView();
    }
    public FlowDetails getFlowDetails(String flowName){
        return flowDetails.get(IntStream.range(0, flowDetails.size())
                .filter(i -> flowDetails.get(i).getFlowName().equals(flowName))
                .findFirst()
                .orElse(-1));
    }

}
