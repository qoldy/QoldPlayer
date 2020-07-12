package com.example.qoldplayerfinally;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Music {
    private ArrayList<Song> songArrayList;
    private ArrayList<Song> playingList;
    private MusicService musicSrv = null;
    private boolean isPlaying = false;
    private Artist artist;

    private ArrayList<Song> currentPlaylist;
    private Context context;

    private TextView previewTextView;
    private TextView durationTextView, psnTextView;

    private CountDownTimer timer;
    private SeekBar psnBar;

    private ArrayList<Artist> artistList = new ArrayList<>();

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }

    public Music(ArrayList<Song> songArrayList){
        this.songArrayList = songArrayList;
        playingList = songArrayList;
        currentPlaylist = new ArrayList<>();
    }

    public ArrayList<Song> getPlayingList() {
        return playingList;
    }

    public void setPlayingList(ArrayList<Song> list) {
        playingList = list;
    }

    public ArrayList<Song> getSongList(){return songArrayList;}



    public void setContext(Context c){
        context = c;
        previewTextView = ((Activity)context).findViewById(R.id.preview_text);
        durationTextView = ((Activity)context).findViewById(R.id.duration_text);
        psnTextView = ((Activity)context).findViewById(R.id.psn_text);
        psnBar = ((Activity)context).findViewById(R.id.psn_bar);
        previewTextView.setSelected(true);
        psnBar.setEnabled(false);
    }
    public void setPreviewText(){
        Song currSong = getSongPlaying();
        previewTextView.setText( currSong.getArtist()+ " - " + currSong.getTitle()+"("
                + currSong.getFeat() + ")");
        durationTextView.setText(currSong.getDuration());
        int duration = Integer.parseInt(currSong.getDuration().substring(0, currSong.getDuration().indexOf(":")))*60;
        duration += Integer.parseInt(currSong.getDuration().substring(currSong.getDuration().indexOf(":")+1));
        psnBar.setMax(duration);
    }


    public void startNewList()
    {
        musicSrv.setList(playingList);
    }

    public MusicService getMusicSrv()
    {
        return musicSrv;
    }
    public void setMusicSrv(MusicService srv)
    {
        musicSrv = srv;
    }

    public void playSong(int song)
    {
        isPlaying = true;
        musicSrv.setSong(song);
        musicSrv.playSong();
        setPreviewText();
        try {
            timer.cancel();
        }
        catch (Exception e){
            setTimer();
            return;
        }
        setTimer();
    }

    public boolean play()
    {
        if(!isPlaying) {
            isPlaying = true;
            musicSrv.go();
        }
        else{
            isPlaying = false;
            musicSrv.pausePlayer();
        }
        return isPlaying;
    }


    public void playNext()
    {
        isPlaying =true;
        musicSrv.playNext();
        setPreviewText();
        try {
            timer.cancel();
        }
        catch (Exception e){
            setTimer();
            return;
        }
        setTimer();
    }

    public void playPrev()
    {
        isPlaying =true;
        musicSrv.playPrev();
        setPreviewText();
        try {
            timer.cancel();
        }
        catch (Exception e){
            setTimer();
            return;
        }
        setTimer();
    }

    public Song getSongPlaying()
    {
        try{
            return musicSrv.getSong();
        }
        catch (Exception e){
            return songArrayList.get(0);
        }
    }

    public int getPsnInList(){
        return musicSrv.getSongPosn();
    }


    public void seek(int psn)
    {
        musicSrv.seek(psn);
    }

    public int getPsn(){return musicSrv.getPsn()/1000;}

    public void setTimer()
    {
            timer = new CountDownTimer
                    (1000,500) {
                @Override
                public void onTick(long l) {
                    int psn = getPsn();
                    if(psn%60<10)
                        psnTextView.setText(psn/60+":0"+psn%60);
                    else
                        psnTextView.setText(psn/60+":"+psn%60);
                psnBar.setProgress(psn);
                }

                @Override
                public void onFinish() {
                    setTimer();
                }
            };
            timer.start();
    }

    public void setCurrentPlaylist(ArrayList<Song> currentPlaylist) {this.currentPlaylist = currentPlaylist;}
    public ArrayList<Song> searchSongs(String search){
        ArrayList<Song> searchList = new ArrayList<>();
        for(int i=0; i<currentPlaylist.size(); i++){
            Song s = currentPlaylist.get(i);
            if(s.getTitle().toLowerCase().contains(search.toLowerCase())||
            s.getArtist().toLowerCase().contains(search.toLowerCase())||
            s.searchFeat(search.toLowerCase()))
                searchList.add(s);
        }
        return searchList;
    }

    public void setArtistList(ArrayList<Artist> list){artistList = list;}
    public ArrayList<Artist> getArtistList(){return artistList;}
}
