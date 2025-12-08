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

    /**
     * Check if this album already contains a photo with the same URI.
     */
    public boolean containsPhoto(Photo p) {
        if (p == null || p.getUriString() == null) {
            return false;
        }
        String uri = p.getUriString();

        for (Photo existing : photos) {
            if (existing.getUriString() != null &&
                    existing.getUriString().equalsIgnoreCase(uri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a photo to this album, preventing duplicates based on URI.
     *
     * @return true if added, false if a photo with same URI already exists
     */
    public boolean addPhoto(Photo p) {
        if (p == null || p.getUriString() == null) {
            return false;
        }

        if (containsPhoto(p)) {
            // duplicate, don't add
            return false;
        }

        photos.add(p);
        return true;
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
