package data;

import javafx.scene.control.Alert;

public class Matchup {

    private int album1; // album1 should always be the lesser #
    private String album1String;
    private int album2;
    private String album2String;
    private int result;

    public Matchup(int album1, String album1String, int album2, String album2String, int result){
        this.album1 = album1;
        this.album1String = album1String;
        this.album2 = album2;
        this.album2String = album2String;
        this.result = result;
    }

    public Matchup(int album1, String album1String, int album2, String album2String){
        this(album1, album1String, album2, album2String, Constants.RESULT_EMPTY);
    }


    public int getAlbum1() {
        return album1;
    }

    public String getAlbum1String() {
        return album1String;
    }

    public int getAlbum2() {
        return album2;
    }

    public String getAlbum2String() {
        return album2String;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        if(result != album1 && result != album2 && result != Constants.RESULT_SKIPPED) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid result. Tried to log " + result + " as victor of matchup with album1: " + album1 + " and album2: " + album2);
            alert.showAndWait();
        }
        this.result = result;
    }
}
