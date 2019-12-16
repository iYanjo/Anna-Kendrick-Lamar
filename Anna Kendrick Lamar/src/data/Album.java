package data;

public class Album {

    private String name;
    private String artist;
    private int year;
    private double rymScore;
    private double p4kScore;
    private double fantanoScore;
    private double adjRymScore;
    private double aggregateScore;
    private String representativeTrack;

    public Album(String name, String artist, int year, double rymScore, double p4kScore, double fantanoScore, double adjRymScore, double aggregateScore, String representativeTrack){
        name = name;
        artist = artist;
        year = year;
        rymScore = rymScore;
        p4kScore = p4kScore;
        fantanoScore = fantanoScore;
        adjRymScore = adjRymScore;
        aggregateScore = aggregateScore;
        representativeTrack = representativeTrack;
    }
}
