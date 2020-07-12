package com.example.qoldplayerfinally;

import android.app.Application;

public class QoldPlayer extends Application {
    private Music music;

    public void setMusic(Music music) {
        this.music = music;
    }
    public Music getMusic(){return music;}
}
