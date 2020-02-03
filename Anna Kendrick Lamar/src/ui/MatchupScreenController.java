package ui;

import data.AlbumArtworkMap;
import data.Matchup;
import excel.ExcelHelper;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;

public class MatchupScreenController {

    private static final String ARTWORK_DIR = ".\\src\\res\\Album Artwork\\";
    private static final String ARTWORK_DEFAULT_PATH = ".\\src\\res\\nan.png";
    private static final String ARTWORK_DEFAULT_PATH2 = "../res/nan.png";

    private Stage mPrimaryStage;
    private ExcelHelper mExcelHelper;

    private VBox leftAlbumDisplay;
    private VBox rightAlbumDisplay;

    public MatchupScreenController(final Stage primaryStage, Parent root, ExcelHelper excelHelper) {
        // build references to all views
        mPrimaryStage = primaryStage;
        mExcelHelper = excelHelper;
        leftAlbumDisplay = (VBox) root.lookup("#leftAlbumDisplay");
        rightAlbumDisplay = (VBox) root.lookup("#rightAlbumDisplay");

        setupNextMatchup();
    }

    private void setupNextMatchup() {
        Matchup matchup = mExcelHelper.getNextMatchup();
        setupAlbumDisplay(leftAlbumDisplay, matchup.getAlbum1());
        setupAlbumDisplay(rightAlbumDisplay, matchup.getAlbum2());
    }

    private void setupAlbumDisplay(VBox albumDisplay, int albumIndex) {
        // image
        ImageView imageView = (ImageView) albumDisplay.lookup("#album_image");
        File image = new File(ARTWORK_DIR + AlbumArtworkMap.ALBUM_ARTWORK_FILENAMES[albumIndex]);
        if(!image.isFile()) {
//            image = new File(ARTWORK_DEFAULT_PATH);
        }
        try {
            imageView.setImage(new Image(getClass().getResource(ARTWORK_DEFAULT_PATH2).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
