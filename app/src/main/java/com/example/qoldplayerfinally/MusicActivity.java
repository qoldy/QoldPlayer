package com.example.qoldplayerfinally;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity{
    private Intent playIntent;
    private boolean musicBound=false;

    private SongAdapter adapter;

    private Music music;
    private ArrayList<Song> songList;
    private ListView songListView;


    private EditText searchEditText;
    private ArrayList<Song> searchList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        music = ((QoldPlayer)this.getApplication()).getMusic();
        music.setContext(this);
        music.setPreviewText();
        songList = music.getSongList();
        adapter = new SongAdapter(this, songList);
        adapter.setPlayingSong(music.getSongPlaying());
        songListView = findViewById(R.id.song_list);
        songListView.setAdapter(adapter);
        searchEditText = findViewById(R.id.search);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if( keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    public void onSongClick(View view){
        Integer tag;
        if(searchList!=null) {
            tag = songList.indexOf(searchList.get(Integer.parseInt(view.getTag().toString())));
        }
        else
            tag = Integer.parseInt(view.getTag().toString());
        music.setPlayingList(songList);
        music.startNewList();
        music.playSong(tag);
        adapter.setPlayingSong(music.getSongPlaying());
        adapter.notifyDataSetChanged();
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            if(music.getMusicSrv()==null)
                music.setMusicSrv(binder.getService());
            music.setPlayingList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void onPlayClick(View view){
        boolean isPlaying = music.play();

    }
    public void onNextClick(View view){
        music.playNext();
        adapter.setPlayingSong(music.getSongPlaying());
        adapter.notifyDataSetChanged();
    }
    public void onPrevClick(View view){
        music.playPrev();
        adapter.setPlayingSong(music.getSongPlaying());
        adapter.notifyDataSetChanged();
    }

    public void onSearchClick(View view){
        search();
    }

    public void onCancelSearchClick(View view){
        searchEditText.setText("");
        search();
    }

    private void search(){
        searchEditText.setEnabled(false);
        searchEditText.setEnabled(true);
        String s = searchEditText.getText().toString();
        if(!s.equals("")) {
            searchList = music.searchSongs(s);
            adapter = new SongAdapter(this, searchList);
            adapter.setPlayingSong(music.getSongPlaying());
        }
        else{
            searchList = new ArrayList<>();
            adapter = new SongAdapter(this, songList);
            adapter.setPlayingSong(music.getSongPlaying());
        }
        songListView.setAdapter(adapter);
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
}
