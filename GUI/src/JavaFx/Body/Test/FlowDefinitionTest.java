package JavaFx.Body.Test;

import JavaFx.Body.BodyController;
import JavaFx.Body.Test.Tabels.FlowDefinitionTable;
import JavaFx.Body.Test.Tabels.StepDetailsTable;
import StepperEngine.FlowDetails.FlowDetails;
import StepperEngine.FlowDetails.StepDetails.StepDetailsImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlowDefinitionTest {
    private List<FlowDetails> flowDetails;
    ObservableList<FlowDefinitionTable> flowDefinitionTableObservableList;
    Map<String,ObservableList<StepDetailsTable>> stepsData=new HashMap<>();
    private BodyController bodyController;
    @FXML
    private TableView<FlowDefinitionTable> flowTable;

    @FXML
    private Label flowName;
    @FXML
    private TableColumn<FlowDefinitionTable, String> flowNameCol;
    @FXML
    private TableColumn<FlowDefinitionTable, String> DescriptionCol;
    @FXML
    private TableColumn<FlowDefinitionTable,Integer> freeInputs;

    @FXML
    private TableColumn<FlowDefinitionTable, Integer> stepsNumber;

    @FXML
    private TableColumn<FlowDefinitionTable, Integer> ContinuationsNumber;


    @FXML
    private VBox flowsInfo;

    @FXML
    private Label flowDescription;

    @FXML
    private Label isReadOnlyFlow;

    @FXML
    private ListView<String> formalOutputs;
    @FXML
    private TableView<StepDetailsTable> stepsTable;

    @FXML
    private TableColumn<StepDetailsTable, String> stepCol;

    @FXML
    private TableColumn<StepDetailsTable, String> readOnlyCol;
    @FXML
    void tableMouseClick(MouseEvent event) {
        if(event.getClickCount()==2) {
            FlowDetails currFlow = flowDetails.get(flowTable.getSelectionModel().getSelectedIndex());
            setFlowDetails(currFlow);
            createFormalOutputsList(currFlow);
            createStepTable(currFlow);
        }
    }

    private void setFlowDetails(FlowDetails currFlow) {
        flowName.setText(currFlow.getFlowName());
        flowDescription.setText(currFlow.getFlowDescription());
        isReadOnlyFlow.setText(currFlow.isFlowReadOnlyString());
    }

    private void createFormalOutputsList(FlowDetails currFlow) {
        ObservableList<String> observableListFormalOutputs = FXCollections.observableList(currFlow.getFormalOutputs());
        formalOutputs.setItems(observableListFormalOutputs);
    }

    private void createStepTable(FlowDetails currFlow) {
        stepCol.setCellValueFactory(new PropertyValueFactory<StepDetailsTable,String>("stepName"));
        readOnlyCol.setCellValueFactory(new PropertyValueFactory<StepDetailsTable,String>("isReadOnly"));
        stepsTable.setItems(stepsData.get(currFlow.getFlowName()));
    }

    public void updateTableView(){
        flowNameCol.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,String>("flowName"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,String>("description"));
        stepsNumber.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,Integer>("stepsNumber"));
        freeInputs.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,Integer>("freeInputsNumber"));
        ContinuationsNumber.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,Integer>("continuationsNumber"));
        flowTable.setItems(flowDefinitionTableObservableList);

    }
    public void setFlowDetails(List<FlowDetails> flowDetails) {
        this.flowDetails = flowDetails;
        List<FlowDefinitionTable> flowDefinitionTableList= new ArrayList<>();
        for (FlowDetails flow :flowDetails){
            flowDefinitionTableList.add(new FlowDefinitionTable(flow));
            stepsData.put(flow.getFlowName(),
                    FXCollections.observableList(
                            flow.getSteps().stream().map(StepDetailsTable::new).collect(Collectors.toList())));
        }
        this.flowDefinitionTableObservableList= FXCollections.observableList(flowDefinitionTableList);
        updateTableView();

    }
    public void setMainController(BodyController bodyController) {
        this.bodyController = bodyController;

    }
    public FlowDetails getFlowDetails(String flowName){
        return flowDetails.get(IntStream.range(0, flowDetails.size())
                .filter(i -> flowDetails.get(i).getFlowName().equals(flowName))
                .findFirst()
                .orElse(-1));
    }

}
