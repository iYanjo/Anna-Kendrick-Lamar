package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class Controller {

    @FXML
    private Button asdfbutton;

    private int count = 0;

    public Controller() {
    }

    public void setupUI(Parent root){
        asdfbutton = (Button) root.lookup("#asdfbutton");
        asdfbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // read xsmlx
            }
        });
    }
}
