package com.example.qoldplayerfinally;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Song implements Serializable {
    private long id;
    private String title, artist, duration="", feat = "Solo track";
    private ArrayList<String> featArr;
    private ArrayList<String> featFlags;

    public Song(long id, String title, String artist, String duration){
        this.id = id;
        this.title = title;
        this.artist = artist;
        setDuration(Integer.parseInt(duration)/1000);
        featArr = new ArrayList<>();
        featFlags = new ArrayList<>(Arrays.asList("feat","ft.", "Feat", "Ft.", ",", "&"));
        setArtistAndFeat();
        if(this.artist.substring(this.artist.length()-1).equals(" "))
            this.artist = this.artist.substring(0, this.artist.length()-1);
        if(this.artist.substring(0, 1).equals(" "))
            this.artist = this.artist.substring(1);
    }
    public String getArtist(){return artist;}
    public String getTitle(){return title;}
    public String getFeat(){return feat;}
    public ArrayList<String> getFeatArr(){return featArr;}
    public boolean searchFeat(String search){
        for(int i=0; i < featArr.size(); i++)
            if(featArr.get(i).toLowerCase().contains(search))
                return true;
        return false;
    }
    public String getDuration(){return duration;}
    public long getID(){return id;}

    private void setArtistAndFeat(){
        if (artist.equals("<unknown>"))
        {
            setArtist();
        }
        setFeatArr();
    }
    private void setArtist(){
        int index = title.indexOf(" - ");
        this.artist = title.substring(0,index);
        this.title = title.substring(index+3);
    }
    private void setFeatArr(){
        ArrayList<String> featBuf = new ArrayList<>();
        for(int i=0; i<featFlags.size(); i++){
            if(artist.contains(featFlags.get(i))){
                int index = artist.indexOf(featFlags.get(i));
                featBuf.add(artist.substring(index+featFlags.get(i).length()+1));
                artist = artist.substring(0,artist.indexOf(featFlags.get(i)));
            }
            if(i < 4){
                if(title.contains(featFlags.get(i))){
                    int index = title.indexOf(featFlags.get(i));
                    featBuf.add(title.substring(index+featFlags.get(i).length()+1));
                    title = title.substring(0, index);
                }
            }
        }
        if(!featBuf.isEmpty())
            correctFeat(featBuf);
        else return;
        feat="Feat: ";
        for(int i = 0; i <featArr.size()-1; i++) {
            feat = (new StringBuilder()).append(feat).append(featArr.get(i)).append(", ").toString();
        }
        feat+=featArr.get(featArr.size()-1);
        if(title.substring(this.title.length()-1).equals("("))
            title = title.substring(0, title.length()-1);
    }
    private void correctFeat(ArrayList<String> featBuf){
        for(int i=0; i<featBuf.size(); i++){
            int index = 0;
            String buf = featBuf.get(i);
            if(buf.substring(0,1).equals(" "))
                buf = buf.substring(1);
            for(int j=0; j<buf.length();j++){
                if(buf.substring(j,j+1).equals(",")){
                    featArr.add(buf.substring(index, j));
                    buf = buf.substring(j+2);
                    index = j+2;
                }
                if (buf.substring(j, j+1).equals(")")) {
                    featArr.add(buf.substring(index, j));
                    break;
                }
            }
            if( buf.substring(buf.length()-1).equals(")")){
                buf = buf.substring(0, buf.length()-1);
            }
            if(buf.substring(buf.length()-1).equals(" ")){
                buf = buf.substring(0, buf.length()-1);
            }
            featArr.add(buf);
        }
        for(int i=0; i<featArr.size()-1;i++){
            for (int j=i+1; j<featArr.size();j++)
                if(featArr.get(i).equals(featArr.get(j))){
                    featArr.remove(j);
                    j--;
                }
        }
    }

    private void setDuration(Integer duration){
        this.duration += duration/60 + ":";
        Integer seconds = duration % 60;
        if(seconds<10)
            this.duration+="0";
        this.duration+=Integer.toString(seconds);
    }
}
