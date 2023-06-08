package JavaFx.Body.FlowStats;

import JavaFx.Body.BodyController;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class FlowStats {

    @FXML
    private TableView<FlowStatsTableRow> flowStatsTableView;

    @FXML
    private TableColumn<FlowStatsTableRow, String> flowCol;

    @FXML
    private TableColumn<FlowStatsTableRow, String> flowExecutionCountCol;

    @FXML
    private TableColumn<FlowStatsTableRow, String> flowAvgTimeCol;

    @FXML
    private TableView<StepStatsTableRow> stepStatsTableView;

    @FXML
    private TableColumn<StepStatsTableRow, String> stepCol;

    @FXML
    private TableColumn<StepStatsTableRow, String> stepExecutionCountCol;

    @FXML
    private TableColumn<StepStatsTableRow, String> stepAvgTimeCol;

    @FXML
    private BarChart<?, ?> chartBar;

    private BodyController bodyController;


    @FXML
    void initialize(){

    }

    public void setFlowStatsTableView(){

    }

    public void setStepStatsTableView(){

    }

    public void setMainController(BodyController bodyController){
        this.bodyController = bodyController;
    }

    public void setFlowChart(String flowName){

    }

    public void setStepChart(String flowName, String stepName){

    }

    public void setGeneralChart(){

    }

    public void refresh(){

    }

}