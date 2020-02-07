package ui;

import data.Album;
import data.AlbumArtworkMap;
import data.Constants;
import data.Matchup;
import excel.ExcelHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;

public class MatchupScreenController {

    private static final String ARTWORK_DIR = ".\\src\\res\\Album Artwork\\";
    private static final String ARTWORK_DEFAULT_PATH = ".\\src\\res\\nan.png";
    private static final String CLASS_ARTWORK_DEFAULT_PATH = "../res/nan.png";
    private static final String CLASS_ARTWORK_DIR = "../res/Album Artwork/";

    private static Matchup mCurrentMatchup;

    private Stage mPrimaryStage;
    private ExcelHelper mExcelHelper;

    private VBox leftAlbumDisplay;
    private VBox rightAlbumDisplay;
    private Button albumLeftChoiceButton;
    private Button albumSkipChoiceButton;
    private Button albumRightChoiceButton;
    private Button helpToolbarButton;
    private Button historyToolbarButton;
    private Button saveToolbarButton;

    //todo: make a history/changelog users can see. double-click on a matchup to revisit
    //todo: have text at the bottom. telling either previous score, or shortcuts/hotkeys
    public MatchupScreenController(final Stage primaryStage, Parent root, ExcelHelper excelHelper) {
        // build references to all views
        mPrimaryStage = primaryStage;
        mExcelHelper = excelHelper;
        leftAlbumDisplay = (VBox) root.lookup("#leftAlbumDisplay");
        rightAlbumDisplay = (VBox) root.lookup("#rightAlbumDisplay");

        //results listeners
        albumLeftChoiceButton = (Button) root.lookup("#albumLeftChoiceButton");
        albumRightChoiceButton = (Button) root.lookup("#albumRightChoiceButton");
        albumSkipChoiceButton = (Button) root.lookup("#albumSkipChoiceButton");

        albumLeftChoiceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recordLeftAlbumWin();
            }
        });
        albumRightChoiceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recordRightAlbumWin();
            }
        });
        albumSkipChoiceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recordAlbumSkip();
            }
        });

        mPrimaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT) {
                    recordLeftAlbumWin();
                } else if(event.getCode() == KeyCode.RIGHT) {
                    recordRightAlbumWin();
                } else if(event.getCode() == KeyCode.DOWN) {
                    recordAlbumSkip();
                }
            }
        });

        //toolbar buttons
        helpToolbarButton = (Button) root.lookup("#helpToolbarButton");
        historyToolbarButton = (Button) root.lookup("#historyToolbarButton");
        saveToolbarButton = (Button) root.lookup("#saveToolbarButton");
        helpToolbarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // todo: display help dialog
            }
        });
        historyToolbarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // todo: display history dialog
            }
        });
        saveToolbarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mExcelHelper.saveResults();
            }
        });

        setupNextMatchup();
    }

    private synchronized void recordLeftAlbumWin() {
        mExcelHelper.setResult(mCurrentMatchup.getAlbum1());
        setupNextMatchup();
    }

    private synchronized void recordRightAlbumWin() {
        mExcelHelper.setResult(mCurrentMatchup.getAlbum2());
        setupNextMatchup();
    }

    private synchronized void recordAlbumSkip() {
        mCurrentMatchup.setResult(Constants.RESULT_SKIPPED);
        setupNextMatchup();
    }

    private synchronized void setupNextMatchup() {
        mCurrentMatchup = mExcelHelper.getNextMatchup();
        if(mCurrentMatchup == null) {
            mPrimaryStage.getScene().setOnKeyPressed(null);
            //todo: it's over
            return;
        }
        setupAlbumDisplay(leftAlbumDisplay, mCurrentMatchup.getAlbum1());
        setupAlbumDisplay(rightAlbumDisplay, mCurrentMatchup.getAlbum2());
    }

    private void setupAlbumDisplay(VBox albumDisplay, int albumIndex) {
        // image
        ImageView imageView = (ImageView) albumDisplay.lookup("#album_image");
        final String albumFileString = AlbumArtworkMap.ALBUM_ARTWORK_FILENAMES[albumIndex];
        File image = new File(ARTWORK_DIR + albumFileString);
        try {
            imageView.setImage(new Image(getClass().getResource(isValidAlbumFileString(albumFileString) ? CLASS_ARTWORK_DIR + albumFileString : CLASS_ARTWORK_DEFAULT_PATH).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // text
        Album album = mExcelHelper.getAlbum(albumIndex);
        Label albumTitle = (Label) albumDisplay.lookup("#album_title");
        Label albumArtist = (Label) albumDisplay.lookup("#album_artist");
        Label albumTrack = (Label) albumDisplay.lookup("#album_track");
        Label albumYear = (Label) albumDisplay.lookup("#album_year");

        albumTitle.setText("Title: " + album.getName());
        albumArtist.setText("Artist: " + album.getArtist());
        albumTrack.setText("Representative Track: " + album.getRepresentativeTrack());
        albumYear.setText("Year: " + album.getYear());

        setupAlbumScoreForDisplay(albumDisplay, "#album_rym_score", "RYM Score", album.getRymScore());
        setupAlbumScoreForDisplay(albumDisplay, "#album_p4k_score", "P4K Score", album.getP4kScore());
        setupAlbumScoreForDisplay(albumDisplay, "#album_fantano_score", "Fantano Score", album.getFantanoScore());
        setupAlbumScoreForDisplay(albumDisplay, "#album_adjrym_score", "Adj. RYM Score", album.getAdjRymScore());
        setupAlbumScoreForDisplay(albumDisplay, "#album_aggregate_score", "Aggregate Score", album.getAggregateScore());
    }

    private void setupAlbumScoreForDisplay(Node root, String identifier, String scoreTypeString, String scoreValueString) {
        VBox albumScoreNode = (VBox) root.lookup(identifier);
        Label scoreType = (Label) albumScoreNode.lookup("#score_type");
        Label scoreValue = (Label) albumScoreNode.lookup("#score_value");
        scoreType.setText(scoreTypeString);
        if(scoreValueString.length() > 4) {
            scoreValueString = scoreValueString.substring(0, 5);
        }
        scoreValue.setText(scoreValueString);
    }

    private boolean isValidAlbumFileString(String fileString) {
        if(fileString.length() < 5) {
            return false;
        }
        String extension = fileString.substring(fileString.length()-5);
        return extension.substring(1).equals(".jpg") || extension.substring(1).equals(".png") || extension.equals(".jpeg");
    }
}
