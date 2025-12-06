package com.example.android36java.model;

import android.content.Context;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class DataStore {

    private static final String FILE_NAME = "albums.dat";
    private static DataStore instance;
    private ArrayList<Album> albums;

    private DataStore() {
        albums = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void load(Context ctx) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(ctx.openFileInput(FILE_NAME))) {
            albums = (ArrayList<Album>) ois.readObject();
        } catch (Exception e) {
            albums = new ArrayList<>();
        }
    }

    public void save(Context ctx) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE))) {
            oos.writeObject(albums);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
