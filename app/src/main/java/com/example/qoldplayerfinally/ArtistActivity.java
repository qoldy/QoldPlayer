package com.example.qoldplayerfinally;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ArtistActivity extends AppCompatActivity {
    private Music music;
    private Artist artist;
    private ArrayList<Song> songList;
    private TextView nameTextView, amountTextView;
    private SongAdapter adapter;
    private ListView listView;
    private ArrayList<Song> searchList;
    private EditText searchEditText;
    private Intent playIntent;
    private boolean musicBound=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        music = ((QoldPlayer)this.getApplication()).getMusic();
        music.setContext(this);
        music.setPreviewText();

        artist = music.getArtist();
        songList = artist.getSongs();
        amountTextView = findViewById(R.id.amount_view);
        nameTextView = findViewById(R.id.name_view);
        String amount;
        if(songList.size()==1)
            amount = "1 track";
        else
            amount = songList.size() + "tracks";
        amountTextView.setText(amount);
        nameTextView.setText(artist.getName());
        listView = findViewById(R.id.song_list);
        setListView();
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

    private void setListView()
    {
        adapter = new SongAdapter(this, songList);
        adapter.setPlayingSong(music.getSongPlaying());
        listView.setAdapter(adapter);
        int desireWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for(int i=0; i < adapter.getCount();i++){
            view = adapter.getView(i, view, listView);
            if(i==0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desireWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desireWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight()*adapter.getCount()-1);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void onSongClick(View view){
        int tag;
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
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ArtistsActivity.class);
        startActivityForResult(intent, 0);
    }
    public void onBackClick (View view){
        Intent intent = new Intent(this, ArtistsActivity.class);
        startActivityForResult(intent, 0);
    }
}
