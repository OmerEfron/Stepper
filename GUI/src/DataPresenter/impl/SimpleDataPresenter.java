package DataPresenter.impl;

import DataPresenter.api.DataPresenterAbstractClass;
import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import javafx.scene.control.Label;


public class SimpleDataPresenter extends DataPresenterAbstractClass {

    public SimpleDataPresenter(IOData data){
        super(data);
        presentation.getChildren().add(new Label(data.getContent()));
    }
}
