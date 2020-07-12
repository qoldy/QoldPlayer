package com.example.qoldplayerfinally;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;
    private LinearLayout songLay;
    private Song playingSong;

    SongAdapter(Context c, ArrayList<Song> theSongs)
    {
        songs = theSongs;
        songInf=LayoutInflater.from(c);
    }

    public void setPlayingSong(Song song){playingSong=song;}
    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        songLay = (LinearLayout)songInf.inflate
                (R.layout.song, parent, false);

        Song currSong = songs.get(position);

        TextView view = songLay.findViewById(R.id.song_title);
        view.setText(currSong.getTitle());
        view = songLay.findViewById(R.id.song_artist);
        view.setText(currSong.getArtist());
        view = songLay.findViewById(R.id.song_duration);
        view.setText(currSong.getDuration());
        view = songLay.findViewById(R.id.song_feat);
        view.setText(currSong.getFeat());
        if(currSong.equals(playingSong))
            songLay.setBackgroundColor(Color.GRAY);
        songLay.setTag(position);
        return songLay;
    }
}
