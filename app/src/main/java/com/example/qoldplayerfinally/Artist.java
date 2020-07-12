package com.example.qoldplayerfinally;

import java.util.ArrayList;

public class Artist {
    private String name;
    private ArrayList<Song> songArrayList;

    public Artist(String name, Song song){
        this.name = name;
        songArrayList = new ArrayList<>();
        songArrayList.add(song);
    }

    public void addSong(Song song){
        songArrayList.add(song);
    }
    public ArrayList<Song> getSongs(){return songArrayList;}
    public String getName(){return name;}
}
