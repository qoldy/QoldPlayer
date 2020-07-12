package com.example.qoldplayerfinally;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
    }

    public void toMusicClick(View view){
        Intent intent = new Intent(this, MusicActivity.class);
        startActivityForResult(intent, 0);
    }
    public void toArtistsClick(View view){
        Intent intent = new Intent(this, ArtistsActivity.class);
        startActivityForResult(intent, 0);
    }
}
