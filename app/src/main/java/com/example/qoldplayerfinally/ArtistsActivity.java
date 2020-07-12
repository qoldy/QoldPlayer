package com.example.qoldplayerfinally;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ArtistsActivity extends AppCompatActivity {
    private ArrayList<Artist> artistList;
    private ArtistAdapter adapter;
    private GridView artistView;
    private Music music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        artistView = findViewById(R.id.artist_list);
        artistList = ((QoldPlayer)this.getApplication()).getMusic().getArtistList();
        adapter = new ArtistAdapter(this, artistList);
        artistView.setAdapter(adapter);
        music = ((QoldPlayer)this.getApplication()).getMusic();
        music.setContext(this);
        music.setPreviewText();
    }
    public void onPlayClick(View view){
        boolean isPlaying = music.play();

    }
    public void onNextClick(View view){
        music.playNext();
    }
    public void onPrevClick(View view){
        music.playPrev();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivityForResult(intent, 0);
    }
    public void onBackClick (View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivityForResult(intent, 0);
    }
    public void onArtistClick(View view){
        Intent intent = new Intent(this, ArtistActivity.class);
        music.setArtist(artistList.get(Integer.parseInt(view.getTag().toString())));
        startActivityForResult(intent, 0);
    }
}
