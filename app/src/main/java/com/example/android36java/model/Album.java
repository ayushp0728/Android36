package com.example.android36java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a photo album.
 * Holds a list of photos and provides operations
 * for adding and removing photos.
 *
 * Dates/captions not required in Android port.
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<Photo> photos = new ArrayList<>();

    public Album(String name) {
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void rename(String newName) {
        this.name = newName.trim();
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public boolean containsPhoto(Photo p) {
        return photos.contains(p);
    }

    public boolean addPhoto(Photo p) {
        // prevent duplicate objects within this album
        if (containsPhoto(p)) return false;
        return photos.add(p);
    }

    public boolean removePhoto(Photo p) {
        return photos.remove(p);
    }

    public int getNumPhotos() {
        return photos.size();
    }

    @Override
    public String toString() {
        return name;
    }
}
