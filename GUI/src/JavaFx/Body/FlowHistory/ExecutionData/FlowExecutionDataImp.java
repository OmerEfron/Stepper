package JavaFx.Body.FlowHistory.ExecutionData;

import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.ArrayList;


public class FlowExecutionDataImp implements ExecutionData{
    @Override
    public HBox setTwoLabels(String name, String value) {
        HBox hBox=new HBox();
        Label nameLabel=new Label(name);
        Label valueLabel=new Label(value);
        hBox.getChildren().add(nameLabel);
        hBox.getChildren().add(valueLabel);
        hBox.setSpacing(5);
        hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        hBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        return hBox;
    }

    public TableView<IOData> getFreeInputTable(FlowExecutionDataImpl flowExecutionData) {
        TableView<IOData> res=new TableView<>();
        TableColumn<IOData, String> freeInputName=new TableColumn<>("Name");
        TableColumn<IOData, String> freeInputType=new TableColumn<>("Type");
        TableColumn<IOData, String> freeInputContent=new TableColumn<>("Content");
        TableColumn<IOData, String> freeInputNecessity=new TableColumn<>("Necessity");

        double totalWidth = 0.15 + 0.2 + 0.2 + 0.2; // Calculate the total sum of the column ratios
        freeInputName.prefWidthProperty().bind(res.widthProperty().multiply(0.15 / totalWidth));
        freeInputType.prefWidthProperty().bind(res.widthProperty().multiply(0.2 / totalWidth));
        freeInputContent.prefWidthProperty().bind(res.widthProperty().multiply(0.2 / totalWidth));
        freeInputNecessity.prefWidthProperty().bind(res.widthProperty().multiply(0.2 / totalWidth));
        freeInputName.setCellValueFactory(new PropertyValueFactory<IOData,String>("name"));
        freeInputType.setCellValueFactory(new PropertyValueFactory<IOData,String>("type"));
        freeInputContent.setCellValueFactory(new PropertyValueFactory<IOData,String>("content"));
        freeInputNecessity.setCellValueFactory(new PropertyValueFactory<IOData,String>("necessity"));
        res.getColumns().addAll(freeInputName,freeInputType,freeInputContent,freeInputNecessity);
        res.setItems(FXCollections.observableList(new ArrayList<>(flowExecutionData.getFreeInputs())));
        res.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                res.setMaxWidth(newValue.doubleValue());
            }
        });
        return res;
    }

    @Override
    public VBox getFreeInputs(FlowExecutionDataImpl flowExecutionData) {
        VBox vBox=new VBox();
        Label freeInputsLabel=new Label("Free inputs:");
        vBox.getChildren().add(freeInputsLabel);
        TableView<IOData> freeInputTable = getFreeInputTable(flowExecutionData);
        vBox.getChildren().add(freeInputTable);
        vBox.setSpacing(5);
        VBox.setVgrow(freeInputTable, Priority.ALWAYS);

        return vBox;
    }
}
