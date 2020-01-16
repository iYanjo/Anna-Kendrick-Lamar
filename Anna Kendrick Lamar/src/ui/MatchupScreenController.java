package ui;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MatchupScreenController {

    private Stage mPrimaryStage;

    private VBox leftAlbumDisplay;
    private VBox rightAlbumDisplay;

    private static final String[] ALBUM_ARTWORK_EXTENSIONS = {".jpg", ".png", ".jpeg"}; //ordered by frequency

    public MatchupScreenController(final Stage primaryStage, Parent root) {
        //setup first thing and build references to all views
        leftAlbumDisplay = (VBox) root.lookup("leftAlbumDisplay");
        rightAlbumDisplay = (VBox) root.lookup("rightAlbumDisplay");
    }

    private void setupAlbum()
}
