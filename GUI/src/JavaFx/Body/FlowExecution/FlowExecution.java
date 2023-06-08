package JavaFx.Body.FlowExecution;


import DataPresenter.api.StringDataPresenter;
import JavaFx.Body.BodyController;
import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails.Input;

import StepperEngine.DTO.FlowDetails.StepDetails.StepDetails;
import StepperEngine.DTO.FlowExecutionData.api.FlowExecutionData;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import StepperEngine.DataDefinitions.Enumeration.ZipEnumerator;
import StepperEngine.Flow.execute.ExecutionNotReadyException;
import StepperEngine.Flow.execute.StepData.StepExecuteData;
import StepperEngine.Flow.execute.StepData.StepIOData;
import StepperEngine.Step.api.DataNecessity;
import StepperEngine.Stepper;

import javafx.animation.FadeTransition;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class FlowExecution {

    private static final int INPUT_NAME_COLUMN = 0;
    private static final int INPUT_MANDATORY_COLUMN = 1;
    private static final int INPUT_DATA_DISPLAY_COLUMN = 2;


    @FXML
    private AnchorPane flowExecutionAnchorPane;

    @FXML
    private GridPane executionDetailsGridPane;

    @FXML
    private ListView<String> stepInputListView;

    @FXML
    private ListView<String> stepOutputListView;

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
    private GridPane freeInputsGridPane;
    @FXML
    private ScrollPane freeInputsScrollPane;
    @FXML
    private TableView<FreeInputsTableRow> freeInputsTableView;
    @FXML
    private TableColumn<FreeInputsTableRow, String> freeInputNameCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> freeInputValueCol;

    @FXML
    private TableColumn<FreeInputsTableRow, String> freeInputApiNameCol;
    @FXML
    private Label outputNameDisplayNameLabel;


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
    private ChoiceBox<String> continuationChoiceBox;

    @FXML
    private Label stepNameTitleLabel;
    @FXML
    private GridPane stepDetailsGridPane;

    @FXML
    private AnchorPane outputPresentationAnchorPane;
    @FXML
    private Label stepLogsLabel;

    @FXML
    private Label stepResutLabel;
    @FXML
    private AnchorPane stepIODataDisplayAnchorPane;

    @FXML
    private Label stepTimeLabel;


    private BodyController bodyController;
    private FlowDetails flowDetails;
    private FlowExecutionData flowExecutionData;

    private String lastFlowRunningUuid;
    private String currFlowExecutionUuid;

    @FXML
    void initialize(){
        initButtons();
        freeInputNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        freeInputValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        //freeInputApiNameCol.setCellValueFactory((new PropertyValueFactory<>("apiName")));
        addFreeInputsFirstRow();
    }
    @FXML
    void continueFlow(MouseEvent event) {

    }
    @FXML
    void executeFlow(MouseEvent event) {
        if(flowExecutionButtonImage.opacityProperty().get() == 1) {
            try {
                executionProgressBar.setProgress(0);
                bodyController.getStepper().executeFlow(currFlowExecutionUuid);
                initExecuteButton();
                new Thread(this::executeFlowTask).start();
                lastFlowRunningUuid = currFlowExecutionUuid;
            } catch (ExecutionNotReadyException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }


    void executeFlowTask() {
        synchronized (this) {
            Stepper stepper = bodyController.getStepper();
            while (!stepper.getExecutionStatus(lastFlowRunningUuid)) {
                Platform.runLater(() -> executionProgressBar.setProgress(stepper.getExecutionPartialStatus(lastFlowRunningUuid)));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Platform.runLater(() ->{
            executionProgressBar.setProgress(1);
            setContinuation();
        });
    }

    void setExecutionDetails(){
        executionProgressBar.setProgress(1);
        flowExecutionData = new FlowExecutionDataImpl(bodyController.getStepper().getFlowExecutionByUuid(lastFlowRunningUuid));
        executionUuidLabel.textProperty().set(flowExecutionData.getUniqueExecutionId());
        executionTimestampLabel.textProperty().set(flowExecutionData.getExecutionTime() + " milliseconds");
        executionResultLabel.textProperty().set(flowExecutionData.getFlowExecutionFinalResult());
        setFormalOutputsAndStepsListView();
        bodyController.updateFlowHistory();
    }


    private void setStepDetails(String stepName)  {
        StepExecuteData stepExecuteData = flowExecutionData.getStepData(stepName);
        stepResutLabel.textProperty().setValue(stepExecuteData.getStepStatus().toString());
        try {
            stepTimeLabel.textProperty().setValue(String.format("started at %s\nended at %s\ntotal time %s",
                    stepExecuteData.getStartTime(), stepExecuteData.getEndTime(), stepExecuteData.getTotalTime()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String logsLabel = "";
        for(Pair<String, String> log: stepExecuteData.getLogs()){
            logsLabel = logsLabel.concat(log.getKey() + " - " + log.getValue());
        }
        stepLogsLabel.textProperty().setValue(logsLabel);
        setStepInputListView(stepExecuteData);
    }

    private void setFormalOutputsAndStepsListView() {
        stepsInfoListView.setItems(FXCollections.observableArrayList(flowExecutionData.getStepExecuteDataList().stream()
                .map(StepExecuteData::getFinalName)
                .collect(Collectors.toList())));
        formalOutputsListView.setItems(FXCollections.observableArrayList(flowExecutionData.getFormalOutputs().stream()
                .map(IOData::getName)
                .collect(Collectors.toList())));
    }



    private void addFreeInputsFirstRow() {
        freeInputsGridPane.add(new Label("Name"), INPUT_NAME_COLUMN, 0);
        freeInputsGridPane.add(new Label("Is madantory?"), INPUT_MANDATORY_COLUMN, 0);
        freeInputsGridPane.add(new Label(), INPUT_DATA_DISPLAY_COLUMN, 0);
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
        cleanUpScreen();
        flowDetails = flow;
        currFlowExecutionUuid = bodyController.getStepper().createNewExecution(flow.getFlowName());
        setFlowDetails();
        setFreeInputsDisplay();
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

    public void setFreeInputsDisplay(){
        for(int i = 0; i < flowDetails.getFreeInputs().size(); i++){
            setInputRowData(flowDetails.getFreeInputs().get(i), i + 1);
            freeInputsScrollPane.setVvalue(freeInputsScrollPane.getVmax());
            freeInputsTableView.getItems().add(new FreeInputsTableRow(flowDetails.getFreeInputs().get(i), "not provided"));
        }
    }

    public void setInputRowData(Input input, int row){
        freeInputsGridPane.add(new Label(input.getUserString()), INPUT_NAME_COLUMN, row);
        freeInputsGridPane.add(new Label(
                DataNecessity.valueOf(input.getNecessity()).equals(DataNecessity.MANDATORY)? "Yes":"NO"),
                INPUT_MANDATORY_COLUMN,
                row);
        freeInputsGridPane.add(getInputDataDisplayer(input), INPUT_DATA_DISPLAY_COLUMN, row);
        freeInputsGridPane.getRowConstraints().get(freeInputsGridPane.getRowConstraints().size() - 1).setPrefHeight(Region.USE_COMPUTED_SIZE);
    }

    public Node getInputDataDisplayer(Input input){
        String typeName = input.getTypeName();
        if(typeName.equals("Enumerator")){
            return getEnumeratorChoiceBox(input);
        }
        else if(typeName.equals("File path") || typeName.equals("Folder path")){
            return getFileChooserButton(input);
        }
        else{
            return getTextFieldChooser(input);
        }
    }

    private ChoiceBox<String> getEnumeratorChoiceBox(Input input) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(EnumSet.allOf(ZipEnumerator.class).stream()
                .map(Enum::toString)
                .collect(Collectors.toList())));
        choiceBox.setOnAction(event -> {
            if(choiceBox.getValue() != null){
                boolean isAdded = addNewValue(input, choiceBox.getValue());
                if(!isAdded){
                    choiceBox.setValue(null);
                }
                else {
                    addInputToTable(input, choiceBox.getValue());
                }
            }
        });
        return choiceBox;
    }



    public HBox getFileChooserButton(Input input){
        HBox hBox = new HBox();
        ImageView fileChooserButton = new ImageView();
        hBox.getChildren().add(fileChooserButton);
        EventHandler<MouseEvent> directoryHandler = event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File folderChoose = directoryChooser.showDialog(fileChooserButton.getScene().getWindow());
            if(folderChoose != null)
                if(addNewValue(input, folderChoose.getAbsolutePath())){
                    addInputToTable(input, folderChoose.getName());
                }
        };
        EventHandler<MouseEvent> fileHandler = event -> {
            FileChooser fileChooser = new FileChooser();
            File fileChoose = fileChooser.showOpenDialog(fileChooserButton.getScene().getWindow());
            if(fileChoose!=null){
                if(addNewValue(input, fileChoose.getAbsolutePath())){
                    addInputToTable(input, fileChoose.getName());
                }
            }
        };
        fileChooserButton.setImage(new Image(getClass().getResourceAsStream("folder-management.png")));
        fileChooserButton.setFitHeight(30);
        fileChooserButton.setFitWidth(30);
        fileChooserButton.setOnMouseClicked(input.getTypeName().equals("File path") ? fileHandler:directoryHandler);
        return hBox;
    }
    public HBox getTextFieldChooser(Input input){
        HBox hBox = new HBox();
        TextField textField = new TextField();
        Button addButton = new Button("+");
        Label invalidInputLabel = new Label();
        invalidInputLabel.setTextFill(Color.RED);
        invalidInputLabel.setPadding(new Insets(0, 0, 0, 10));
        hBox.getChildren().add(textField);
        hBox.getChildren().add(addButton);
        hBox.getChildren().add(invalidInputLabel);
        EventHandler<ActionEvent> eventHandler = event -> {
            if(!addNewValue(input, textField.getText())){
                invalidInputLabel.textProperty().setValue("invalid input");
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), invalidInputLabel);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setCycleCount(5);
                fadeTransition.play();
            }
            else{
                invalidInputLabel.textProperty().setValue("");
                addInputToTable(input, textField.getText());
            }
        };
        addButton.setOnAction(eventHandler);
        textField.setOnAction(eventHandler);
        return hBox;
    }





    private boolean addNewValue(Input input, String value) {
        try {
            boolean result = bodyController.getStepper().addFreeInputToExecution(currFlowExecutionUuid, input.getDataName(),
                    convertValue(value, input.getTypeName()));
            if(result && bodyController.getStepper().getExecutionReadyToBeExecuteStatus(currFlowExecutionUuid)){
                makeExecutionButtonEnabled();
            }
            return result;
        }catch (NumberFormatException e){
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

    public void addInputToTable(Input input, String newVal){
        ObservableList<FreeInputsTableRow> freeInputsTableRows = freeInputsTableView.getItems();
        for(int i=0; i<freeInputsTableRows.size(); i++){
            if(freeInputsTableRows.get(i).getApiName().equals(input.getDataName())){
                freeInputsTableRows.get(i).setValue(newVal);
                freeInputsTableView.getItems().set(i, freeInputsTableRows.get(i));
                return;
            }
        }
    }

    public void cleanUpScreen(){
        freeInputsTableView.getItems().remove(0, freeInputsTableView.getItems().size());
        freeInputsGridPane.getChildren().clear();
        addFreeInputsFirstRow();
    }

    public void setStepInputListView(StepExecuteData step){
        stepInputListView.setItems(FXCollections.observableArrayList(step.getDataMap().keySet()));
    }

    public void setContinuation(){
        continuationChoiceBox.setItems(FXCollections.observableArrayList(flowDetails.getContinuationNames()));
    }


}
