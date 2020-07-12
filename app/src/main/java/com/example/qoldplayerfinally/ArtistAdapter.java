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

public class ArtistAdapter extends BaseAdapter {

    private ArrayList<Artist> artists;
    private LayoutInflater artistInf;
    private LinearLayout artistLay;

    ArtistAdapter(Context c, ArrayList<Artist> theArtists)
    {
        artists = theArtists;
        artistInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return artists.size();
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
        artistLay = (LinearLayout)artistInf.inflate
                (R.layout.artist, parent, false);

        Artist currArtist = artists.get(position);

        TextView view = artistLay.findViewById(R.id.artist_name);
        view.setText(currArtist.getName());
        artistLay.setTag(position);
        return artistLay;
    }
}
