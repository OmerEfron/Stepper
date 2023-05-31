package JavaFx.Body.FlowExecution;

import JavaFx.Body.BodyController;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowExecutionData.api.FlowExecutionData;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Flow.execute.StepData.StepIOData;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlowExecution {

    @FXML
    private BorderPane flowExecutionBorderPane;

    @FXML
    private HBox flowExecutionTopHbox;

    @FXML
    private VBox FlowExecutionFreeInputsVbox;


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
    private ListView<String> executionFormalOutputsListView;

    @FXML
    private Label ExecutionDetailsLabel;
    private BodyController bodyController;
    private FlowDetails currFlowToExecute;
    private FlowExecutionData lastExecutionData;
    private StepperEngine.Flow.execute.FlowExecution flowExecution;

    private final BooleanProperty canBeExecutedProperty = new SimpleBooleanProperty(false);

    private boolean hasExecuted;



    @FXML
    void initialize(){
        setInputTableCellValueFactory();
        FlowExecutionButton.disableProperty().bind(canBeExecutedProperty.not());
        setTableListeners();
    }



    private void setTableListeners() {
        FlowExecutionFreeInputsTable.editingCellProperty().addListener((obs, oldVal, newVal) -> isAllMandatoryFreeInputsFilled());
    }

    void isAllMandatoryFreeInputsFilled(){
        canBeExecutedProperty.setValue(FlowExecutionFreeInputsTable.getItems().stream()
                .filter(input -> input.getNecessity().equals("MANDATORY"))
                .noneMatch(row -> Objects.equals(row.getValue(), "")) &&  !hasExecuted);
    }

    public void setInputTableCellValueFactory() {
        freeInputsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        freeInputsTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        freeInputsNecessityCol.setCellValueFactory(new PropertyValueFactory<>("necessity"));
        freeInputsValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        freeInputsValueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        freeInputsValueCol.setOnEditCommit(event -> {
            TablePosition<FreeInputsTableRow, String> position = event.getTablePosition();
            String editedValue = event.getNewValue();
            Object newVal = convertValue(editedValue, event.getRowValue().getType());
            flowExecution.addFreeInput(event.getRowValue().getName(), newVal);
            FreeInputsTableRow row = position.getTableView().getItems().get(position.getRow());
            row.setValue(editedValue);
        });
    }

    Object convertValue(String val, String type){
        try {
            if (type.equals("Number")) {
                return Integer.parseInt(val);
            } else if (type.equals("Double")) {
                return Double.parseDouble(val);
            } else {
                return val;
            }
        }catch (NumberFormatException e){
            return "";
        }
    }


    public void setMainController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    @FXML
    void continueToFlow(ActionEvent event) {

    }

    @FXML
    void executeFlow(ActionEvent event) {
        Thread thread = new Thread(this::executeFlowTask);
        thread.start();
    }

    void executeFlowTask(){
        bodyController.getStepper().ExecuteFlow(flowExecution);
        Platform.runLater(this::checkExecutionStatusTask);
    }

    public void checkExecutionStatusTask(){
        while(!flowExecution.hasExecuted()){
            System.out.println("still running");
            double progress = (double)flowExecution.getNumOfStepsExecuted() / flowExecution.getNumOfSteps();
            FlowExecutionProgressBar.setProgress(progress);
        }
        System.out.println("done running!");
        hasExecuted = true;
        lastExecutionData = new FlowExecutionDataImpl(flowExecution);
        executionActualUuid.textProperty().setValue(lastExecutionData.getUniqueExecutionId());
        executionActualTimestamp.textProperty().setValue(lastExecutionData.getExecutionTime());
        updateFormalOutputs();
        updateSteps();
    }

    public void setFlowToExecute(FlowDetails flowDetails) {
        hasExecuted = false;
        currFlowToExecute = flowDetails;
        flowExecution = bodyController.getFlowExecution(currFlowToExecute.getFlowName()).orElse(null);
    }



    public void updateFormalOutputs(){
        if(flowExecution.hasExecuted()){
            executionFormalOutputsListView.setItems(FXCollections.observableList(lastExecutionData.getFormalOutputs().stream()
                    .map(IOData::getName)
                    .collect(Collectors.toList())));
        }
    }

    public void updateSteps(){
        String steps = "";
        for(int i = 0; i < flowExecution.getNumOfSteps(); i++){
            StepExecuteData step = lastExecutionData.getStepExecuteDataList().get(i);
            String stepPresentation = String.format("%d. step %s started at ____, finished at _____, total time %s milliseconds, final status %s\n",
                    i + 1,step.getFinalName(), step.getTotalTime().toMillis(), step.getStepStatus());
            steps = steps.concat(stepPresentation);
        }
        executionActualSteps.textProperty().setValue(steps);
    }



    public void setExecutionInfo(){
        setFlowDetails();
        setFreeInputTable();
    }

    private void setFreeInputTable() {
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
