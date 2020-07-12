package com.example.qoldplayerfinally;


import android.content.Context;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONHelper{
    private static String file_name = "music.json";

    static void setFileName(String name){file_name = name;}
    static boolean exportToJSON(Context context, Music music) {

        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setmusic(music);
        String jsonString = gson.toJson(dataItems);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(file_name, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    static Music importFromJSON(Context context) {

        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = context.openFileInput(file_name);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return dataItems.getmusic();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static class DataItems {
        private Music music;

        Music getmusic() {
            return music;
        }
        void setmusic(Music m) {
            music = m;
        }
    }
}