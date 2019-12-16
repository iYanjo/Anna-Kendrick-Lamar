package ui;

import excel.ExcelFileFilter;
import excel.ExcelHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class Controller {

    private Button asdfbutton;
    private ExcelHelper excelHelper = new ExcelHelper();

    public Controller() {
    }

    public void setupUI(Parent root){
        asdfbutton = (Button) root.lookup("#asdfbutton");
        asdfbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // read xsmlx
                //Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter(
                        "Choose the decade albums Excel", "xlsx"));

                //In response to a button click:
//                int returnVal = fc.showOpenDialog(null);
//
//                if (returnVal == JFileChooser.APPROVE_OPTION) {
//                    File file = fc.getSelectedFile();
//                    try {
//                        excelHelper.getSpreadsheet(fc.getSelectedFile().getPath());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
                    try {
                        excelHelper.getSpreadsheet("C:\\Users\\jyan1_000\\Downloads\\Albums_2010s.xlsx");
                    } catch (IOException e) {
                        e.printStackTrace();
                        ReadErrorAlert readErrorAlert = new ReadErrorAlert();
                        readErrorAlert.display(e);
                    }
            }
        });
    }
}
