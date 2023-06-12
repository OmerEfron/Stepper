package DataPresenter;

import DataPresenter.api.DataPresenter;
import DataPresenter.impl.ListFilePresenter;
import DataPresenter.impl.ListStringPresenter;
import DataPresenter.impl.RelationStringPresenter;
import DataPresenter.impl.SimpleDataPresenter;
import StepperEngine.DTO.FlowExecutionData.impl.IOData;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Set;

public class DataPresentationImpl implements DataPresentation {
    @Override
    public Node getDataPresent(Set<IOData> data) {
        FlowPane flowPane=new FlowPane();
        if(data.isEmpty())
            flowPane.getChildren().add(new VBox(new Label("No outputs were generated")));
        else {
            for (IOData ioData : data) {
                flowPane.getChildren().add(getSingleDataPresenter(ioData));
            }
        }
        return flowPane;
    }
    private Node getSingleDataPresenter(IOData data){
        DataPresenter dataPresenter;
        switch (data.getType()) {
            case "FilesListDataDef":
                dataPresenter=new ListFilePresenter(data);
                break;
            case "StringListDataDef":
                dataPresenter=new ListStringPresenter(data);
                break;
            case "RelationOfStringRows":
                dataPresenter=new RelationStringPresenter(data);
                break;
            default:
                dataPresenter=new  SimpleDataPresenter(data);
                break;
        }

        return dataPresenter.getPresenter();
    }
}
