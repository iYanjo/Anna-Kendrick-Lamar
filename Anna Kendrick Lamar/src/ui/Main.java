package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("initial_screen.fxml"));
        N3zController n3zController = new N3zController();
        fxmlLoader.setController(n3zController);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("asdf");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
        n3zController.setupSigninScene(primaryStage, root);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
