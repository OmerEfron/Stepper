package JavaFx.Body.FlowHistory;

import JavaFx.Body.BodyController;
import JavaFx.Body.FlowHistory.ExecutionData.ExecutionData;
import JavaFx.Body.FlowHistory.ExecutionData.FlowExecutionDataImp;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FlowHistory {

    @FXML private TableView<FlowExecutionDataImpl> flowsExecutionTable;
    @FXML private TableColumn<FlowExecutionDataImpl, String> flowsExecutionsNamesCol;
    @FXML private TableColumn<FlowExecutionDataImpl, String> flowsExecutionsTimeCol;
    @FXML private TableColumn<FlowExecutionDataImpl, String> flowsExecutionsStatusCol;
    @FXML private Button rerunButton;
    @FXML private ComboBox<String> filterChoose;
    @FXML private Label filterSelectionLabel;
    @FXML private ImageView resetTable;
    @FXML private VBox MainExecutionDataVbox;
    @FXML private TreeView<String> StepsTreeVIew;

    private Map<String,VBox> vBoxMap=new HashMap<>();
    private BooleanProperty booleanProperty=new SimpleBooleanProperty();
    private BodyController bodyController;
    private ExecutionData flowExecutionData=new FlowExecutionDataImp();

    public void setMainController(BodyController bodyController) {
        this.bodyController = bodyController;

    }
    @FXML
    void initialize(){
        filterChoose.setOnAction(event -> {
            String selectedOption = filterChoose.getValue();
            setItemsInFlowsExecutionTable(FXCollections.observableList(bodyController.getStepper().getFlowExecutionDataList()
                    .stream().filter(flowExecutionData -> flowExecutionData.getExecutionResult().equals(selectedOption)).collect(Collectors.toList())));
            booleanProperty.set(true);
        });
        filterSelectionLabel.textProperty().bind(Bindings
                .when(booleanProperty)
                .then("Selected filter option :")
                .otherwise(
                        "Choose Filter :"
                )
        );

        resetTable.cursorProperty().set(Cursor.HAND);
    }

    @FXML
    void restTableFilter(MouseEvent event) {
        restTable();
        filterChoose.getSelectionModel().clearSelection();
        setItemsInFlowsExecutionTable(FXCollections.observableList(bodyController.getStepper().getFlowExecutionDataList()));
        booleanProperty.set(false);
    }

    private void restTable(){
        RotateTransition transition=new RotateTransition();
        transition.setNode(resetTable);
        transition.setDuration(Duration.seconds(0.7));
        transition.setCycleCount(1);
        transition.setByAngle(360);
        transition.play();
    }
    public void setFlowsExecutionTable() {
        if (!bodyController.getStepper().getFlowExecutionDataList().isEmpty()) {
            flowsExecutionsNamesCol.setCellValueFactory(new PropertyValueFactory<FlowExecutionDataImpl, String>("flowName"));
            flowsExecutionsTimeCol.setCellValueFactory(new PropertyValueFactory<FlowExecutionDataImpl, String>("formattedStartTime"));
            flowsExecutionsStatusCol.setCellValueFactory(new PropertyValueFactory<FlowExecutionDataImpl, String>("executionResult"));
            setAligmentToFlowsExecutionCols();
            setItemsInFlowsExecutionTable(FXCollections.observableList(bodyController.getStepper().getFlowExecutionDataList()));
            filterChoose.setItems(FXCollections.observableList(getOptionList()));
        }
    }

    private void setItemsInFlowsExecutionTable(ObservableList<FlowExecutionDataImpl> data) {
        flowsExecutionTable.setItems(data);
    }

    private List<String>  getOptionList() {
        return bodyController.getStepper().getFlowExecutionDataList()
                .stream()
                .map(FlowExecutionDataImpl::getExecutionResult)
                .filter(name -> !name.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private void setAligmentToFlowsExecutionCols() {
        flowsExecutionsNamesCol.setCellFactory(column -> new TableCell<FlowExecutionDataImpl, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(Pos.CENTER);  // Align text to the middle
            }
        });

        flowsExecutionsTimeCol.setCellFactory(column -> new TableCell<FlowExecutionDataImpl, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(Pos.CENTER);  // Align text to the middle
            }
        });

        flowsExecutionsStatusCol.setCellFactory(column -> new TableCell<FlowExecutionDataImpl, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(Pos.CENTER);  // Align text to the middle
            }
        });
    }
    @FXML
    void setFlowExecutionDetails(MouseEvent event) {
        if(event.getClickCount()==2){
            FlowExecutionDataImpl selectedItem = flowsExecutionTable.getSelectionModel().getSelectedItem();
            MainExecutionDataVbox.getChildren().clear();
            if(!vBoxMap.containsKey(selectedItem.getUniqueExecutionId()))
                updateExecutionDataVboxByFlow(selectedItem);
            MainExecutionDataVbox.getChildren().add(vBoxMap.get(selectedItem.getUniqueExecutionId()));
        }
    }

    private void updateExecutionDataVboxByFlow(FlowExecutionDataImpl selectedItem) {
        VBox newVbox=new VBox();
        newVbox.getChildren().add(flowExecutionData.setTwoLabels("Flow Name :", selectedItem.getFlowName()));
        newVbox.getChildren().add(flowExecutionData.setTwoLabels("UUID :", selectedItem.getUniqueExecutionId()));
        newVbox.getChildren().add(flowExecutionData.setTwoLabels("Flow execution status :", selectedItem.getExecutionResult()));
        newVbox.getChildren().add(flowExecutionData.setTwoLabels("Timestamp :", selectedItem.getExecutionTime()));
        newVbox.getChildren().add(flowExecutionData.getFreeInputs(selectedItem));
        vBoxMap.put(selectedItem.getUniqueExecutionId(),newVbox);
    }


}
