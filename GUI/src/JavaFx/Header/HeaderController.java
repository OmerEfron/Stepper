package JavaFx.Header;
import JavaFx.AppController;
import StepperEngine.Flow.FlowBuildExceptions.FlowBuildException;
import StepperEngine.StepperReader.Exception.ReaderException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HeaderController {
    private AppController mainController;
    @FXML
    private VBox headerComponent;

    @FXML
    private Button loadButton;

    @FXML
    private Label filePathLabel;
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
    @FXML
    private void handleLoadButtonClick() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        Stage stage = (Stage) loadButton.getScene().getWindow();
        // Show the file chooser dialog
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getPath();
            if(mainController.loadFile(filePath))
                filePathLabel.setText(filePath);
        }

    }
}
