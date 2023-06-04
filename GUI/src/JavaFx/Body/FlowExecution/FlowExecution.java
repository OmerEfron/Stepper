package JavaFx.Body.FlowExecution;

import JavaFx.Body.BodyController;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.DTO.FlowDetails.StepDetails.StepDetails;
import StepperEngine.DTO.FlowExecutionData.api.FlowExecutionData;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import StepperEngine.Flow.execute.ExecutionNotReadyException;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Stepper;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.stream.Collectors;

public class FlowExecution {

    @FXML
    private AnchorPane flowExecutionAnchorPane;

    @FXML
    private GridPane executionDetailsGridPane;

    @FXML
    private ListView<String> formalOutputsListView;

    @FXML
    private ListView<String> stepsInfoListView;

    @FXML
    private Label executionUuidLabel;

    @FXML
    private Label executionTimestampLabel;

    @FXML
    private Label executionResultLabel;

    @FXML
    private Separator executionDetailsDataSeperator;

    @FXML
    private Separator StepOutputSeperator;

    @FXML
    private Label stepNameDisplayNameLabel;

    @FXML
    private Label outputNameDisplayNameLabel;

    @FXML
    private TableView<FreeInputsTableRow> flowExecutionFreeInputTable;

    @FXML
    private TableColumn<FreeInputsTableRow, String> nameCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> typeCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> necessityCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> valueCol;

    @FXML
    private ImageView flowExecutionButtonImage;

    @FXML
    private ProgressBar executionProgressBar;

    @FXML
    private Label floeDetailsLabel;

    @FXML
    private GridPane flowDetailGridPane;

    @FXML
    private Label flowNameLabel;

    @FXML
    private Label floeDescriptionLabel;

    @FXML
    private Label floeStepsLabel;

    @FXML
    private Pane continuationPane;

    @FXML
    private Separator continuationDetailsSeperator;

    @FXML
    private Label continuationLabel;

    @FXML
    private ImageView continuationButtonImage;

    @FXML
    private ChoiceBox<?> continuationChoiceBox;

    private BodyController bodyController;
    private FlowDetails flowDetails;
    private FlowExecutionData flowExecutionData;
    private String lastFlowRunningUuid;
    private String currFlowExecutionUuid;

    @FXML
    void continueFlow(MouseEvent event) {

    }
    @FXML
    void executeFlow(MouseEvent event) {
        try {
            bodyController.getStepper().executeFlow(currFlowExecutionUuid);
        } catch (ExecutionNotReadyException e) {
            throw new RuntimeException(e);
        }
        new Thread(this::executeFlowTask).start();
        lastFlowRunningUuid = currFlowExecutionUuid;
    }


    void executeFlowTask(){
        String uuid = lastFlowRunningUuid;
        Stepper stepper = bodyController.getStepper();
        while(!stepper.getExecutionStatus(uuid)){
            Platform.runLater(() ->executionProgressBar.setProgress(stepper.getExecutionPartialStatus(uuid)));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(this::setExecutionDetails);
    }

    void setExecutionDetails(){
        flowExecutionData = new FlowExecutionDataImpl(bodyController.getStepper().getFlowExecutionByUuid(lastFlowRunningUuid));
        executionUuidLabel.textProperty().set(flowExecutionData.getUniqueExecutionId());
        executionTimestampLabel.textProperty().set(flowExecutionData.getExecutionTime() + " milliseconds");
        executionResultLabel.textProperty().set(flowExecutionData.getFlowExecutionFinalResult());
        setFormalOutputsAndStepsListView();
    }

    private void setFormalOutputsAndStepsListView() {
        stepsInfoListView.setItems(FXCollections.observableArrayList(flowExecutionData.getStepExecuteDataList().stream()
                .map(StepExecuteData::getFinalName)
                .collect(Collectors.toList())));
        formalOutputsListView.setItems(FXCollections.observableArrayList(flowExecutionData.getFormalOutputs().stream()
                .map(IOData::getName)
                .collect(Collectors.toList())));
    }

    @FXML
    void initialize(){
        initButtons();
        initFreeInputTable();

    }

    private void initButtons() {
        initExecuteButton();
        initContinuationButton();
    }

    private void initContinuationButton() {
        continuationButtonImage.opacityProperty().set(0.2);
        continuationButtonImage.cursorProperty().set(Cursor.DISAPPEAR);
    }

    private void initExecuteButton() {
        flowExecutionButtonImage.opacityProperty().set(0.2);
        flowExecutionButtonImage.cursorProperty().set(Cursor.DISAPPEAR);
    }

    void makeExecutionButtonEnabled(){
        flowExecutionButtonImage.opacityProperty().set(1);
        flowExecutionButtonImage.cursorProperty().set(Cursor.HAND);
    }

    public void setMainController(BodyController bodyController) {
        this.bodyController = bodyController;
    }

    public void setFlowToExecute(FlowDetails flow){
        flowDetails = flow;
        currFlowExecutionUuid = bodyController.getStepper().createNewExecution(flow.getFlowName());
        setFreeInputTable();
        setFlowDetails();
    }

    public void setFlowDetails(){
        flowNameLabel.textProperty().set(flowDetails.getFlowName());
        floeDescriptionLabel.textProperty().set(flowDetails.getFlowDescription());
        String steps = "";
        for(String step:flowDetails.getStepsNames()){
            steps = steps.concat(step + "\n");
        }
        floeStepsLabel.textProperty().set(steps);
    }

    public void initFreeInputTable(){
        nameCol.setCellValueFactory(new PropertyValueFactory<FreeInputsTableRow, String>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<FreeInputsTableRow, String>("type"));
        necessityCol.setCellValueFactory(new PropertyValueFactory<FreeInputsTableRow, String>("necessity"));
        valueCol.setCellValueFactory(new PropertyValueFactory<FreeInputsTableRow, String>("value"));
        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valueCol.setOnEditCommit(event -> {
            if (!addNewValue(event)) {
                event.getTableView().refresh();
            } else {
                event.getRowValue().setValue(event.getNewValue());
            }
            if(bodyController.getStepper().getExecutionReadyToBeExecuteStatus(currFlowExecutionUuid))
                makeExecutionButtonEnabled();
        });
    }

    public void setFreeInputTable(){
        ObservableList<FreeInputsTableRow> data = FXCollections.observableArrayList(
                flowDetails.getFreeInputs().stream()
                        .map(input -> new FreeInputsTableRow(input.getDataName(), input.getTypeName(), input.getNecessity()))
                        .collect(Collectors.toList())
        );
        flowExecutionFreeInputTable.setItems(data);
    }

    private boolean addNewValue(TableColumn.CellEditEvent<FreeInputsTableRow, String> event) {
        try {
            boolean result = bodyController.getStepper().addFreeInputToExecution(currFlowExecutionUuid, event.getRowValue().getName(),
                    convertValue(event.getNewValue(), event.getRowValue().getType()));
            if(result) {
                System.out.println("worked!");
            }
            else {
                System.out.println("not worked");
            }
            return result;
        }catch (NumberFormatException e){
            System.out.println("invalid input!");
            return false;
        }
    }

    public Object convertValue(String value, String type){
        if (type.equals("Number")) {
            return Integer.parseInt(value);
        } else if (type.equals("Double")) {
            return Double.parseDouble(value);
        } else {
            return value;
        }
    }




}
