package com.example.android36java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uriString;            // store content URI as string
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
     * Adds a tag (lowercase) only if type is person or location.
     */
    public boolean addTag(Tag tag) {
        String type = tag.getType().toLowerCase(Locale.ROOT);
        String value = tag.getValue().toLowerCase(Locale.ROOT);

        if (!type.equals(Tag.PERSON) && !type.equals(Tag.LOCATION)) {
            return false;
        }

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
        return uriString;
    }
}
