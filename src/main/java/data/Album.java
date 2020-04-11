package data;

public class Album {

    private String name;
    private String artist;
    private int year;
    private String rymScore;
    private String p4kScore;
    private String fantanoScore;
    private String adjRymScore;
    private String aggregateScore;
    private String representativeTrack;

    public Album(String name, String artist, int year, String rymScore, String p4kScore, String fantanoScore, String adjRymScore, String aggregateScore, String representativeTrack){
        this.name = name;
        this.artist = artist;
        this.year = year;
        this.rymScore = rymScore;
        this.p4kScore = p4kScore;
        this.fantanoScore = fantanoScore;
        this.adjRymScore = adjRymScore;
        this.aggregateScore = aggregateScore;
        this.representativeTrack = representativeTrack;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public int getYear() {
        return year;
    }

    public String getRymScore() {
        return rymScore;
    }

    public String getP4kScore() {
        return p4kScore;
    }

    public String getFantanoScore() {
        return fantanoScore;
    }

    public String getAdjRymScore() {
        return adjRymScore;
    }

    public String getAggregateScore() {
        return aggregateScore;
    }

    public String getRepresentativeTrack() {
        return representativeTrack;
    }
}
