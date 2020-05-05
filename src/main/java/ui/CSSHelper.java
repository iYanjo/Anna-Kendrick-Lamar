package ui;

import data.Configs;
import javafx.scene.Scene;

public class CSSHelper {
    public CSSHelper(){}

    public static void maybeApplyCSS(Scene scene) {
        if(Configs.style == Configs.Style.NONE) {
            return;
        } else if(Configs.style == Configs.Style.DARCULA) {
            scene.getStylesheets().add(CSSHelper.class.getResource("/css/darcula.css").toExternalForm());
        }
    }
}
