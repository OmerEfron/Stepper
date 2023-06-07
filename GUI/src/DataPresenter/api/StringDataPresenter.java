package DataPresenter.api;

import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class StringDataPresenter implements DataPresenter{

    private VBox presentation;
    @Override
    public Node getPresenter() {
        return presentation;
    }

    public StringDataPresenter(IOData data, int height, int width){
        presentation = new VBox();
        presentation.getChildren().add(new Label(data.getUserString()));
        presentation.getChildren().add(new Label(data.getContent()));
    }
}
