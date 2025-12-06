package com.example.android36java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents a photo using a URI string.
 * Captions are not required in the Android assignment
 * — the filename will stand for caption.
 * Tags are person/location only.
 */
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    // URI string pointing to content:// or external file
    private String uriString;

    // No caption/date needed for Android assignment
    private List<Tag> tags = new ArrayList<>();

    public Photo(String uriString) {
        this.uriString = uriString;
    }

    public String getUriString() {
        return uriString;
    }

    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Adds a tag.  Tag type must be person or location.
     * Values stored lowercase for case-insensitive matching.
     */
    public boolean addTag(Tag tag) {
        // Normalize
        String type = tag.getType().toLowerCase(Locale.ROOT);
        String value = tag.getValue().toLowerCase(Locale.ROOT);

        if (!type.equals(Tag.PERSON) && !type.equals(Tag.LOCATION)) {
            return false;  // invalid tag type
        }

        // Prevent duplicates
        for (Tag t : tags) {
            if (t.getType().equalsIgnoreCase(type)
                    && t.getValue().equalsIgnoreCase(value)) {
                return false;
            }
        }

        tags.add(new Tag(type, value));
        return true;
    }

    public boolean removeTag(Tag tag) {
        return tags.removeIf(t ->
                t.getType().equalsIgnoreCase(tag.getType())
                        && t.getValue().equalsIgnoreCase(tag.getValue()));
    }

    @Override
    public String toString() {
        return uriString;   // will be displayed using filename in UI
    }
}
