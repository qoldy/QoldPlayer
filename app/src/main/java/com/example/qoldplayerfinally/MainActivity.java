package com.example.qoldplayerfinally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    ArrayList<Artist> artistList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Music music;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        music = JSONHelper.importFromJSON(this);
        if(music!=null){
            Toast.makeText(this, "Данные восстановлены", Toast.LENGTH_LONG).show();
            ((QoldPlayer)this.getApplication()).setMusic(music);
        }
        else{
            Toast.makeText(this, "Не удалось открыть данные", Toast.LENGTH_LONG).show();
            getSongList();
        }
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivityForResult(intent, 0);
    }

    //Получение списка песен на устройстве
    public void getSongList() {
        ArrayList<Song> songArrayList = new ArrayList<>();
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null,
                null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {

            //Получение информации о песнях
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int durColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            //Добавление песен в список
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisDur = musicCursor.getString(durColumn);
                Song curSong = new Song(thisId, thisTitle, thisArtist, thisDur);
                songArrayList.add(curSong);
                setArtist(curSong);

            }
            while (musicCursor.moveToNext());
        }
        for (int i = 0; i < songArrayList.size(); i++){
            Collections.sort(songArrayList, new Comparator<Song>(){
                public int compare(Song a, Song b){
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
        }
        for (int i = 0; i < artistList.size(); i++){
            Collections.sort(artistList, new Comparator<Artist>(){
                public int compare(Artist a, Artist b){
                    return a.getName().compareTo(b.getName());
                }
            });
        }
        Music music = new Music(songArrayList);
        music.setArtistList(artistList);
        ((QoldPlayer)this.getApplication()).setMusic(music);
    }

    public void setArtist(Song song){
        boolean flag = false;
        ArrayList<String> songFeats = song.getFeatArr();
        for(int i=0; i<artistList.size();i++){
            if(song.getArtist().equals(artistList.get(i).getName())) {
                artistList.get(i).addSong(song);
                flag = true;
            }
            if(songFeats.contains(artistList.get(i).getName())) {
                artistList.get(i).addSong(song);
                songFeats.remove(artistList.get(i).getName());
            }
        }
        if(!flag){
            artistList.add(new Artist(song.getArtist(), song));
        }
        if(songFeats.size()!=0){
            for(int i=0; i<songFeats.size(); i++)
                artistList.add(new Artist(songFeats.get(i), song));
        }
    }


    //Запрос разрешений
    private void requestPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
