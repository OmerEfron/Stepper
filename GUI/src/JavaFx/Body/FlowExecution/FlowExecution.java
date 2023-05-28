package JavaFx.Body.FlowExecution;

import JavaFx.Body.BodyController;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowExecutionData.api.FlowExecutionData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FlowExecution {

    @FXML
    private BorderPane flowExecutionBorderPane;

    @FXML
    private HBox flowExecutionTopHbox;

    @FXML
    private VBox FlowExecutionFreeInputsVbox;

    @FXML
    private Button FlowExecutionFreeInputsAddButton;

    @FXML
    private TableView<FreeInputsTableRow> FlowExecutionFreeInputsTable;

    @FXML
    private TableColumn<FreeInputsTableRow, String> freeInputsNameCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> freeInputsTypeCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> freeInputsNecessityCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> freeInputsValueCol;

    @FXML
    private VBox FlowExecuteExecutionVbox;

    @FXML
    private Button FlowExecutionButton;

    @FXML
    private HBox FlowExecutionExecuteHbox;

    @FXML
    private Label FlowExecutionProgressLabel;

    @FXML
    private ProgressBar FlowExecutionProgressBar;

    @FXML
    private BorderPane FlowExectuionflowDetailsBorderPane;

    @FXML
    private GridPane FlowExecutionFlowDetailsGridPane;

    @FXML
    private Label FlowExecutionFlowNameLabel;

    @FXML
    private Label FlowExecutionFlowDescriptionLabel;

    @FXML
    private Label FlowExecutionFlowStepsLabel;

    @FXML
    private Label flowDetailsActualName;

    @FXML
    private Label flowDetailsActualDescription;

    @FXML
    private Label flowDetailsActualSteps;

    @FXML
    private Label FlowExecutionFlowDetailsLabel;

    @FXML
    private BorderPane ContinuationBorderPane;

    @FXML
    private HBox continuationHbox;

    @FXML
    private TableView<?> continuationTable;

    @FXML
    private HBox continueHbox;

    @FXML
    private Button continueButton;

    @FXML
    private Label continuationLabel;

    @FXML
    private BorderPane ExecutionDetailsBorderPane;

    @FXML
    private GridPane ExecutionDetailsGridPane;

    @FXML
    private Label ExecutionUuidLabel;

    @FXML
    private Label ExecutionTimestampLabel;

    @FXML
    private Label ExecutionFinalResultLabel;

    @FXML
    private Label ExecutionStepsLabel;

    @FXML
    private Label executionActualUuid;

    @FXML
    private Label executionActualTimestamp;

    @FXML
    private Label executionActualFormalOutputs;

    @FXML
    private Label executionActualSteps;

    @FXML
    private Label ExecutionDetailsLabel;
    private BodyController bodyController;
    private FlowDetails currFlowToExecute;
    private FlowExecutionData lastExecutionData;

    @FXML
    void initialize(){
        setInputTableCellValueFactory();
    }

    public void setInputTableCellValueFactory() {
        freeInputsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        freeInputsTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        freeInputsNecessityCol.setCellValueFactory(new PropertyValueFactory<>("necessity"));
        freeInputsValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    public void setMainController(BodyController bodyController) {
        this.bodyController = bodyController;
    }
    @FXML
    void addNewInput(ActionEvent event) {

    }

    @FXML
    void continueToFlow(ActionEvent event) {

    }

    @FXML
    void executeFlow(ActionEvent event) {

    }

    public void setFlowToExecute(FlowDetails flowDetails){
        currFlowToExecute = flowDetails;
    }


    public void setExecutionInfo(){
        setFlowDetails();
        ObservableList<FreeInputsTableRow> inputObservableList = FXCollections.observableArrayList();
        currFlowToExecute.getFreeInputs().forEach(input -> inputObservableList.add(new FreeInputsTableRow(input.getDataName(),
                input.getTypeName(), input.getNecessity())));
        FlowExecutionFreeInputsTable.setItems(inputObservableList);
    }


    private void setFlowDetails() {
        flowDetailsActualName.textProperty().setValue(currFlowToExecute.getFlowName());
        flowDetailsActualDescription.textProperty().setValue(currFlowToExecute.getFlowDescription());
        StringBuilder stepsBuilder = new StringBuilder();
        currFlowToExecute.getStepsNames().forEach(stepName -> stepsBuilder.append(stepName).append("\n"));
        String steps = stepsBuilder.toString();
        flowDetailsActualSteps.textProperty().setValue(steps);
    }

}
