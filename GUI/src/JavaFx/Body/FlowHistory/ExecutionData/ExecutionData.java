package JavaFx.Body.FlowHistory.ExecutionData;

import StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.DTO.FlowExecutionData.impl.FlowExecutionDataImpl;
import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public interface ExecutionData {

    HBox setTwoLabels(String name,String value);

    VBox getFreeInputs(FlowExecutionDataImpl flowExecutionData);
}
