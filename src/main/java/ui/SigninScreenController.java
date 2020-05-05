package ui;

import data.Configs;
import data.Constants;
import excel.ExcelHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SigninScreenController {

    private Stage mPrimaryStage;
    private Button firstTimeButton;
    private Button nthTimeButton;
    private TextField nameTextField;

    private ExcelHelper excelHelper = new ExcelHelper();

    public SigninScreenController() {
    }

    private static final char[] ILLEGAL_FILENAME_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\'
            , '<', '>', '|', '\"', ':'};

    public void setupSigninScene(final Stage primaryStage, Parent root) {
        mPrimaryStage = primaryStage;
        nameTextField = (TextField) root.lookup("#nameTextField");
        firstTimeButton = (Button) root.lookup("#firstTimeButton");
        firstTimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getName().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Name");
                    alert.setHeaderText("Can't have empty name");
                    alert.setContentText("Please enter a non-empty name without invalid characters for files");
                    alert.showAndWait();
                } else if (doesStringContainInvalidFilenameCharacters(getName())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Name");
                    alert.setHeaderText("Can't have invalid characters for your name");
                    alert.setContentText("Please enter a non-empty name without invalid characters (?, *, `, /, etc)");
                    alert.showAndWait();
                } else {
                    loadAlbumsSpreadsheet();
                    if (createMatchupsSpreadsheet(getName())) {
                        setupMatchupScreen();
                    }
                }
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

                if (Constants.USE_COMPACT_EXCEL ? excelHelper.getCompactResultsSpreadsheet(spreadsheetFile) :
                        excelHelper.getResultsSpreadsheet(
                                spreadsheetFile)) {
                    setupMatchupScreen();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid File");
                    alert.setContentText("Please choose a valid results spreadsheet");
                    alert.showAndWait();
                }
            }
        });
    }

    private boolean doesStringContainInvalidFilenameCharacters(String s) {
        Set<Character> illegalCharacterSet = new HashSet<>();
        for (char c : ILLEGAL_FILENAME_CHARACTERS) {
            illegalCharacterSet.add(c);
        }
        for (int i = 0; i < s.length(); i++) {
            if (illegalCharacterSet.contains(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void loadAlbumsSpreadsheet() {
        try {
            if (!excelHelper.isAlbumsFetched()) {
                excelHelper.getAlbumsSpreadsheet();
            }
        } catch (IOException e) {
            e.printStackTrace();
            ReadErrorAlert readErrorAlert = new ReadErrorAlert();
            readErrorAlert.display(e);
        }
    }

    // return true if created successfully
    private boolean createMatchupsSpreadsheet(String name) {
        try {
            return excelHelper.createResultsSpreadsheet(name);
        } catch (IOException e) {
            e.printStackTrace();
            ReadErrorAlert readErrorAlert = new ReadErrorAlert();
            readErrorAlert.display(e);
            return false;
        }
    }

    public @Nullable
    String getName() {
        return nameTextField.getText();
    }

    public void setupMatchupScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/matchup_screen.fxml"));
        fxmlLoader.setController(this);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 1000, 1000);
        CSSHelper.maybeApplyCSS(scene);
        mPrimaryStage.setScene(scene);
        new MatchupScreenController(mPrimaryStage, root, scene, excelHelper);
    }
}
