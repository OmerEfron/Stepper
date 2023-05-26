package JavaFx.Body.FlowExecution;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FlowExecution {

    @FXML
    private ScrollPane flowExecutionScrollPane;

    @FXML
    private BorderPane flowExecutionBorderPane;

    @FXML
    private HBox flowExecutionTopHbox;

    @FXML
    private VBox FlowExecutionFreeInputsVbox;

    @FXML
    private Button FlowExecutionFreeInputsAddButton;

    @FXML
    private TableView<?> FlowExecutionFreeInputsTable;

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
    private Label ExecutionDetailsLabel;

    @FXML
    void addNewInput(ActionEvent event) {

    }

    @FXML
    void continueToFlow(ActionEvent event) {

    }

    @FXML
    void executeFlow(ActionEvent event) {

    }

}
