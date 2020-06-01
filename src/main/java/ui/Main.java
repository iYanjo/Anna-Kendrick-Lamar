package ui;

import data.Configs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        System.out.println(getClass().getResource("res/anna bae icon.png"));
//        System.out.println(getClass().getResource("/res/anna bae icon.png"));
//        System.out.println(getClass().getResource("../res/anna bae icon.png"));
//        System.out.println(getClass().getResource("/../res/anna bae icon.png"));
//        System.out.println(getClass().getResource("../../res/anna bae icon.png"));
//        System.out.println(getClass().getResource("/../../res/anna bae icon.png"));
//        System.out.println(getClass().getResource("ui/res/anna bae icon.png"));
//        System.out.println(getClass().getResource("/ui/res/anna bae icon.png"));
//        System.out.println(getClass().getResource("ui/res/anna bae icon.png"));
//        System.out.println(getClass().getResource("/ui/res/anna bae icon.png"));
//        System.out.println(getClass().getResource("main/java/ui/initial_screen.fxml"));
//        System.out.println(getClass().getResource("/main/java/ui/initial_screen.fxml"));
//        System.out.println(getClass().getResource("src/main/java/ui/initial_screen.fxml"));
//        System.out.println(getClass().getResource("/src/main/java/ui/initial_screen.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/initial_screen.fxml"));
        SigninScreenController signinScreenController = new SigninScreenController();
        fxmlLoader.setController(signinScreenController);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Anna Kendrick Lamar");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/anna bae icon.png")));
        Scene scene = new Scene(root, 1000, 1000);
        CSSHelper.maybeLoadYuglyFont();
        CSSHelper.maybeApplyCSS(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
        signinScreenController.setupSigninScene(primaryStage, root);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
