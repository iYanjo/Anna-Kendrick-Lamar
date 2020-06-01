package ui;

import data.Configs;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CSSHelper {

    private final static String YUGLY_FONT_NAME = "/css/yuglyfont.TTF";
    private final static String DARK_THEME_NAME = "/css/darktheme.css";
    private final static String ANNA_THEME_NAME = "/css/anna.css";

    public CSSHelper(){}

    public static void maybeLoadYuglyFont() {
        if(Configs.style == Configs.Style.ANNA) {
            Font.loadFont(CSSHelper.class.getResource(YUGLY_FONT_NAME).toExternalForm(), 35);
        }
    }

    public static void maybeApplyCSS(Scene scene) {
        if(Configs.style == Configs.Style.NONE) {
            return;
        } else if(Configs.style == Configs.Style.DARK) {
            scene.getStylesheets().add(CSSHelper.class.getResource(DARK_THEME_NAME).toExternalForm());
        } else if(Configs.style == Configs.Style.ANNA) {
            scene.getStylesheets().add(CSSHelper.class.getResource(ANNA_THEME_NAME).toExternalForm());
        }
    }

    public static void maybeApplyCSS(DialogPane dialogPane) {
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image(CSSHelper.class.getResourceAsStream("/ganon_icon.png")));

        return;
        //todo: fix the alerts + css
        /*
        if(Configs.style == Configs.Style.NONE) {
            return;
        } else if(Configs.style == Configs.Style.DARK) {
            dialogPane.getStylesheets().removeAll("alert");
            dialogPane.getStylesheets().add(CSSHelper.class.getResource(DARK_THEME_NAME).toExternalForm());
        }
         */
    }
}
