package ui;

import excel.ExcelHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;

public class N3zController {

    private Stage mPrimaryStage;
    private Button firstTimeButton;
    private Button nthTimeButton;
    private TextField nameTextField;

    private ExcelHelper excelHelper = new ExcelHelper();
    private String name;
    private String hash;

    public N3zController() {
    }

    public void setupSigninScene(final Stage primaryStage, Parent root) {
        mPrimaryStage = primaryStage;
        firstTimeButton = (Button) root.lookup("#firstTimeButton");
        firstTimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //create xslx. store data in active memory (probably?) and display if needed.
                loadAlbumsSpreadsheet();
                createMatchupsSpreadsheet();
                setupMatchupScreen();
            }
        });

        nthTimeButton = (Button) root.lookup("#nthTimeButton");
        nthTimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadAlbumsSpreadsheet();
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Choose the Excel from before", "*.xlsx"));
                File spreadsheetFile = fileChooser.showOpenDialog(mPrimaryStage);

                if(spreadsheetFile != null) {
                    setupMatchupScreen();
                }
            }
        });

    }

    public void setupMatchupScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("matchup_screen.fxml"));
        fxmlLoader.setController(this);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPrimaryStage.setScene(new Scene(root, 600, 600));
    }

    private void loadAlbumsSpreadsheet() {
        try {
            excelHelper.getAlbumsSpreadsheet("res/Albums2010s.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            ReadErrorAlert readErrorAlert = new ReadErrorAlert();
            readErrorAlert.display(e);
        }
    }

    private void createMatchupsSpreadsheet() {
        try {
            excelHelper.createResultsSpreadsheet("asdf");
        } catch (IOException e) {
            e.printStackTrace();
            ReadErrorAlert readErrorAlert = new ReadErrorAlert();
            readErrorAlert.display(e);
        }
    }

    public String getName() {
        if(name == null) {
            name = nameTextField.toString();
        }
        return name;
    }

    public String getHash() {
        if(hash == null) {
            hash = DigestUtils.sha256Hex(getName());
        }
        return hash;
    }
}
