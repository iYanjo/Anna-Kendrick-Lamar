package ui;

import data.*;
import excel.ExcelHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class MatchupScreenController {

    private static final String ARTWORK_DIR = ".\\src\\res\\Album Artwork\\";
    private static final String ARTWORK_DEFAULT_PATH = ".\\src\\res\\nan.png";
    private static final String CLASS_ARTWORK_DEFAULT_PATH = "../res/nan.png";
    private static final String CLASS_ARTWORK_DIR = "../res/Album Artwork/";
    private static final String JAR_CLASS_ARTWORK_DIR = "/Album Artwork/";

    private static Matchup mCurrentMatchup;

    private Stage mPrimaryStage;
    private ExcelHelper mExcelHelper;

    private VBox leftAlbumDisplay;
    private VBox rightAlbumDisplay;
    private Button albumLeftChoiceButton;
    private Button albumSkipChoiceButton;
    private Button albumRightChoiceButton;
    private Label albumLeftChoiceLabel;
    private Label albumSkipChoiceLabel;
    private Label albumRightChoiceLabel;
    private Button helpToolbarButton;
    private Button historyToolbarButton;
    private Button saveToolbarButton;

    //anna
    private float mVolume = 0;
    private MediaPlayer mMediaPlayer;


    public MatchupScreenController(final Stage primaryStage, Parent root, Scene scene, ExcelHelper excelHelper) {
        // build references to all views
        mPrimaryStage = primaryStage;
        mExcelHelper = excelHelper;
        leftAlbumDisplay = (VBox) root.lookup("#leftAlbumDisplay");
        rightAlbumDisplay = (VBox) root.lookup("#rightAlbumDisplay");

        //results listeners
        albumLeftChoiceButton = (Button) root.lookup("#albumLeftChoiceButton");
        albumRightChoiceButton = (Button) root.lookup("#albumRightChoiceButton");
        albumSkipChoiceButton = (Button) root.lookup("#albumSkipChoiceButton");
        albumLeftChoiceLabel = (Label) root.lookup("#albumLeftChoiceLabel");
        albumRightChoiceLabel = (Label) root.lookup("#albumRightChoiceLabel");
        albumSkipChoiceLabel = (Label) root.lookup("#albumSkipChoiceLabel");

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

        ImageView leftImageView = (ImageView) leftAlbumDisplay.lookup("#album_image");
        leftImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                recordLeftAlbumWin();
            }
        });

        ImageView rightImageView = (ImageView) rightAlbumDisplay.lookup("#album_image");
        rightImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                recordRightAlbumWin();
            }
        });

        mPrimaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT) {
                    recordLeftAlbumWin();
                } else if (event.getCode() == KeyCode.RIGHT) {
                    recordRightAlbumWin();
                } else if (event.getCode() == KeyCode.DOWN) {
                    recordAlbumSkip();
                } else if (event.getCode() == KeyCode.UP) {
                    loadPrevMatchup();
                }
            }
        });

        mPrimaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        //toolbar buttons
        helpToolbarButton = (Button) root.lookup("#helpToolbarButton");
        historyToolbarButton = (Button) root.lookup("#historyToolbarButton");
        saveToolbarButton = (Button) root.lookup("#saveToolbarButton");
        helpToolbarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openHelpPopup();
            }
        });
        historyToolbarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // todo: display history dialog
                openHistoryPopup();
            }
        });
        saveToolbarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Constants.USE_COMPACT_EXCEL) {
                    mExcelHelper.saveCompactResults();
                } else {
                    mExcelHelper.saveResults();
                }
            }
        });

        loadNextMatchup();

        if(Configs.style == Configs.Style.ANNA) {
            Media media = new Media(getClass().getResource("/pitch_perfect_audition.mp3").toExternalForm());
            mMediaPlayer = new MediaPlayer(media);
//            mMediaPlayer.setAutoPlay(true);
            mMediaPlayer.setVolume(mVolume);
            mMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    private synchronized void recordLeftAlbumWin() {
        mExcelHelper.setResult(mCurrentMatchup.getAlbum1());
        loadNextMatchup();
    }

    private synchronized void recordRightAlbumWin() {
        mExcelHelper.setResult(mCurrentMatchup.getAlbum2());
        loadNextMatchup();
    }

    private synchronized void recordAlbumSkip() {
        mExcelHelper.setResult(Constants.RESULT_SKIPPED);
        loadNextMatchup();
    }

    private synchronized void loadNextMatchup() {
        mCurrentMatchup = mExcelHelper.getNextMatchup();
        if (mCurrentMatchup == null) {
            mPrimaryStage.getScene().setOnKeyPressed(null);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/victory_screen.fxml"));
            fxmlLoader.setController(this);
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mPrimaryStage.setScene(new Scene(root, 1000, 1000));
            //todo: it's over
            return;
        }
        setupCurrentMatchup();
        maybeDoAnnaStuff();
    }

    private synchronized void loadPrevMatchup() {
        Matchup matchup = mExcelHelper.getPrevMatchup();
        if (matchup == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No previous matchups");
            alert.setContentText(String.format("This is the first matchup"));
            CSSHelper.maybeApplyCSS(alert.getDialogPane());
            alert.initOwner(mPrimaryStage.getOwner());
            Optional<ButtonType> res = alert.showAndWait();
            return;
        }
        mCurrentMatchup = matchup;
        setupCurrentMatchup();
        maybeDoAnnaStuff();
    }

    private synchronized void maybeDoAnnaStuff() {
        //todo expand dong
        if(Configs.style != Configs.Style.ANNA) {
            return;
        }

        if(mMediaPlayer != null && mVolume <= 1) {
            mVolume += 0.1;
            mMediaPlayer.setVolume(mVolume);
        }


    }

    private synchronized void setupCurrentMatchup() {
        setupAlbumDisplay(leftAlbumDisplay, mCurrentMatchup.getAlbum1() - 1);
        setupAlbumDisplay(rightAlbumDisplay, mCurrentMatchup.getAlbum2() - 1);

        albumLeftChoiceLabel.setText("");
        albumSkipChoiceLabel.setText("");
        albumRightChoiceLabel.setText("");

        if (mCurrentMatchup.getResult() == Constants.RESULT_SKIPPED) {
            albumSkipChoiceLabel.setText("This is your current choice");
        } else if (mCurrentMatchup.getResult() == mCurrentMatchup.getAlbum1()) {
            albumLeftChoiceLabel.setText("This is your current choice");
        }
        if (mCurrentMatchup.getResult() == mCurrentMatchup.getAlbum2()) {
            albumRightChoiceLabel.setText("This is your current choice");
        }
    }

    private void setupAlbumDisplay(VBox albumDisplay, int albumIndex) {
        // image
        ImageView imageView = (ImageView) albumDisplay.lookup("#album_image");
        final String albumFileString = AlbumArtworkMap.ALBUM_ARTWORK_FILENAMES[albumIndex];
        imageView.setImage(new Image(getClass().getResourceAsStream(JAR_CLASS_ARTWORK_DIR + albumFileString)));

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

        setupAlbumScoreForDisplay(albumDisplay, "#album_adjrym_score", "Adj. RYM Score", album.getAdjRymScore());
        setupAlbumScoreForDisplay(albumDisplay, "#album_p4k_score", "P4K Score", album.getP4kScore());
        setupAlbumScoreForDisplay(albumDisplay, "#album_fantano_score", "Fantano Score", album.getFantanoScore());
        setupAggregateAlbumScoreForDisplay(albumDisplay, album.getAggregateScore());
    }

    private void setupAlbumScoreForDisplay(Node root, String identifier, String scoreTypeString,
            String scoreValueString) {
        VBox albumScoreNode = (VBox) root.lookup(identifier);
        Label scoreType = (Label) albumScoreNode.lookup("#score_type");
        Label scoreValue = (Label) albumScoreNode.lookup("#score_value");
        scoreType.setText(scoreTypeString);
        if (scoreValueString.length() > 4) {
            scoreValueString = scoreValueString.substring(0, 5);
        }
        scoreValue.setText(scoreValueString);
    }

    private void setupAggregateAlbumScoreForDisplay(Node root, String scoreValueString) {
        HBox albumScoreNode = (HBox) root.lookup("#album_aggregate_score");
        Label scoreType = (Label) albumScoreNode.lookup("#score_type");
        Label scoreValue = (Label) albumScoreNode.lookup("#score_value");
        scoreType.setText("Aggregate Score: ");
        if (scoreValueString.length() > 4) {
            scoreValueString = scoreValueString.substring(0, 5);
        }
        scoreValue.setText(scoreValueString);
    }

    private void closeWindowEvent(WindowEvent event) {
        if (!mExcelHelper.hasUnsavedChanges()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("Quit application");
        alert.setContentText(String.format("Detected unsaved changes. Continue closing?"));
        CSSHelper.maybeApplyCSS(alert.getDialogPane());
        alert.initOwner(mPrimaryStage.getOwner());
        Optional<ButtonType> res = alert.showAndWait();

        if (res.isPresent()) {
            if (res.get().equals(ButtonType.CANCEL))
                event.consume();
        }
    }

    private void openHelpPopup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/misc/help_popup.fxml"));
        fxmlLoader.setController(this);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            ReadErrorAlert readErrorAlert = new ReadErrorAlert();
            readErrorAlert.display(e);
        }

        if (root == null) {
            return;
        }
        final Stage helpStage = new Stage();
        helpStage.initModality(Modality.APPLICATION_MODAL);
        helpStage.initOwner(mPrimaryStage);
        helpStage.setTitle("T R U E R A G E");
        helpStage.getIcons().add(new Image(getClass().getResourceAsStream("/ganon_icon.png")));

        Scene dialogScene = new Scene(root, 600, 600);
        CSSHelper.maybeApplyCSS(dialogScene);
        helpStage.setScene(dialogScene);
        helpStage.show();
    }

    private void openHistoryPopup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/misc/history_popup.fxml"));
//        fxmlLoader.setController(new HistoryPopupController());
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            ReadErrorAlert readErrorAlert = new ReadErrorAlert();
            readErrorAlert.display(e);
        }

        if (root == null) {
            return;
        }
        final Stage helpStage = new Stage();
        helpStage.initModality(Modality.APPLICATION_MODAL);
        helpStage.initOwner(mPrimaryStage);
        helpStage.setTitle("CHAOS REIGNS");
        helpStage.getIcons().add(new Image(getClass().getResourceAsStream("/conor_icon.png")));

        Scene dialogScene = new Scene(root, 600, 600);
        CSSHelper.maybeApplyCSS(dialogScene);
        helpStage.setScene(dialogScene);
        helpStage.show();
    }
}
