package JavaFx;

import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;
import StepperEngine.FlowExecutionData.impl.FlowExecutionsCollector;
import StepperEngine.Stepper;
import StepperEngine.StepperReader.Exception.ReaderException;
import StepperEngine.StepperReader.XMLReadClasses.TheStepper;
import StepperEngine.StepperReader.impl.StepperReaderFromXml;
import javafx.application.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class Gui extends Application {
    private StepperDTO stepperDTO=new StepperDTO();


    @FXML
    private Button loadButton;

    @FXML
    private Label filePathLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab flowsDefinition;

    @FXML
    private TreeView<String> flowsTreeView;

    @FXML
    private Label flowsInfo;

    @FXML
    private Tab flowsExecution;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        Parent root = loader.load();
        loadButton = (Button) loader.getNamespace().get("loadButton");
        filePathLabel = (Label) loader.getNamespace().get("filePathLabel");
        loadButton.setOnAction(e -> handleLoadButtonClick());
        flowsTreeView = (TreeView<String>) loader.getNamespace().get("flowsTreeView");
        flowsInfo=(Label)loader.getNamespace().get("flowsInfo");
        flowsTreeView.setRoot(new TreeItem<>("Stepper"));
        flowsDefinition = (Tab) loader.getNamespace().get("flowsDefinition");

        flowsDefinition.setOnSelectionChanged(event -> {
            if (flowsDefinition.isSelected()) {
            }
        });
        //flowsDefinition.setContent(flowsTreeView);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

//    private void updateTreeView() {
//        stepperDTO.setFlowTree(flowsTreeView);
//    }

    private void handleLoadButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        Stage stage = (Stage) loadButton.getScene().getWindow();
        // Show the file chooser dialog
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getPath();
            try {
                stepperDTO.load(filePath);
//                updateTreeView();
            } catch (ReaderException | FlowBuildException | RuntimeException e ) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Stepper");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            filePathLabel.setText(filePath);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


    public void treeMouseClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()==2) {
            TreeItem<String> node = flowsTreeView.getSelectionModel().getSelectedItem();
            flowsInfo.setText(node.getValue());
        }
    }
}
