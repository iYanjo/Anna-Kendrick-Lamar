package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("initial_screen.fxml"));
        SigninScreenController signinScreenController = new SigninScreenController();
        fxmlLoader.setController(signinScreenController);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Anna Kendrick Lamar");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/res/anna bae icon.png")));
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
        signinScreenController.setupSigninScene(primaryStage, root);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
