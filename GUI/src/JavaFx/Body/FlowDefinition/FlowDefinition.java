package JavaFx.Body.FlowDefinition;

import JavaFx.Body.BodyController;
import JavaFx.Body.FlowDefinition.Tabels.FlowDefinitionTable;
import JavaFx.Body.FlowDefinition.Tabels.StepDetailsTable;

import StepperEngine.DTO.FlowDetails.FlowDetails;
import StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails.Input;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlowDefinition {
    @FXML private TableView<FlowDefinitionTable> flowTable;
    @FXML private Label flowName;
    @FXML private TableColumn<FlowDefinitionTable, String> flowNameCol;
    @FXML private TableColumn<FlowDefinitionTable, String> DescriptionCol;
    @FXML private TableColumn<FlowDefinitionTable,Integer> freeInputs;
    @FXML private TableColumn<FlowDefinitionTable, Integer> stepsNumber;
    @FXML private TableColumn<FlowDefinitionTable, Integer> ContinuationsNumber;
    @FXML private VBox flowsInfo;
    @FXML private Label flowDescription;
    @FXML private Label isReadOnlyFlow;
    @FXML private ListView<String> formalOutputs;
    @FXML private TableView<StepDetailsTable> stepsTable;
    @FXML private TableColumn<StepDetailsTable, String> stepCol;
    @FXML private TableColumn<StepDetailsTable, String> readOnlyCol;
    @FXML private TableView<Input> freeInputsTable;
    @FXML private TableColumn<Input, String> freeInputName;
    @FXML private TableColumn<Input, String> freeInputType;
    @FXML private TableColumn<Input, String> freeInputNecessity;
    @FXML private TableColumn<Input, String> freeInputSteps;

    @FXML private Button executeFlow;

    private List<FlowDetails> flowDetails;
    private ObservableList<FlowDefinitionTable> flowDefinitionTableObservableList;
    private Map<String,ObservableList<StepDetailsTable>> stepsTableData =new HashMap<>();
    private Map<String,ObservableList<Input>> freeInputsData=new HashMap<>();
    private BodyController bodyController;





    @FXML
    void tableMouseClick(MouseEvent event) {
        if(event.getClickCount()==2) {
            FlowDetails currFlow = flowDetails.get(flowTable.getSelectionModel().getSelectedIndex());
            setDataByFlowName(currFlow);
            createFormalOutputsList(currFlow);
            createStepTable(currFlow);
            creatFreeInputsTable(currFlow);
        }
    }


    private void creatFreeInputsTable(FlowDetails currFlow) {
        freeInputName.setCellValueFactory(new PropertyValueFactory<Input,String>("dataName"));
        freeInputName.setPrefWidth((freeInputsData.get(currFlow.getFlowName()).stream()
                .mapToDouble(input -> input.getDataName().length())
                .max().orElse(-1))*9.5);
        freeInputType.setCellValueFactory(new PropertyValueFactory<Input,String>("typeName"));
        freeInputNecessity.setCellValueFactory(new PropertyValueFactory<Input,String>("necessity"));
        freeInputSteps.setCellValueFactory(new PropertyValueFactory<Input,String>("relatedStepsString"));
        freeInputSteps.setPrefWidth((freeInputsData.get(currFlow.getFlowName()).stream()
                .mapToDouble(input -> input.getRelatedStepsString().length())
                .max().orElse(-1))*7.5);
        freeInputsTable.setItems(freeInputsData.get(currFlow.getFlowName()));
    }





    private void setDataByFlowName(FlowDetails currFlow) {
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
        stepsTable.setItems(stepsTableData.get(currFlow.getFlowName()));
    }
/*
Updates the flows table (the main table in the left)
 */
    public void updateTableView(){
        flowNameCol.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,String>("flowName"));
        setColumnWidthToMaxNameLength(flowNameCol);
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,String>("description"));
        setColumnWidthToMaxDescriptionLength(DescriptionCol);
        stepsNumber.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,Integer>("stepsNumber"));
        freeInputs.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,Integer>("freeInputsNumber"));
        ContinuationsNumber.setCellValueFactory(new PropertyValueFactory<FlowDefinitionTable,Integer>("continuationsNumber"));
        flowTable.setItems(flowDefinitionTableObservableList);


    }
    /*
    sets thw width of the description column in flow main table
     */
    private void setColumnWidthToMaxDescriptionLength(TableColumn<FlowDefinitionTable, String> column) {
        int maxWidth=-1;
        column.setPrefWidth(maxWidth);
        for(FlowDetails flow:flowDetails){
            if(flow.getFlowDescription().length()>maxWidth)
                maxWidth=flow.getFlowDescription().length();

        }
        column.setPrefWidth(maxWidth*7.0);
    }
    /*
    sets thw width of the name column in flow main table
 */
    private void setColumnWidthToMaxNameLength(TableColumn<FlowDefinitionTable, String> column) {
        double maxWidth=-1;
        for(FlowDetails flow:flowDetails){
            if(flow.getFlowName().length()>maxWidth)
                maxWidth=flow.getFlowName().length();

        }
        column.setPrefWidth(maxWidth*7.5);
    }
    /*
    The method updates data on each flow according to its name to map.
    For each flow, a mapping of the list of steps, a mapping of the free inputs
     */
    public void setDataByFlowName(List<FlowDetails> flowDetails) {
        this.flowDetails = flowDetails;
        List<FlowDefinitionTable> flowDefinitionTableList= new ArrayList<>();
        for (FlowDetails flow :flowDetails){
            flowDefinitionTableList.add(new FlowDefinitionTable(flow));
            freeInputsData.put(flow.getFlowName(),FXCollections.observableList(flow.getFreeInputs()));
            stepsTableData.put(flow.getFlowName(),
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
