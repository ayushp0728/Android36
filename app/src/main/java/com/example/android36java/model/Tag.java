package com.example.android36java.model;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a tag with type and value.
 * Only two valid types: person and location.
 */
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PERSON = "person";
    public static final String LOCATION = "location";

    private String type;   // person or location
    private String value;  // lower-case

    public Tag(String type, String value) {
        if (type == null || value == null) {
            throw new IllegalArgumentException("Tag type/value cannot be null");
        }

        // Normalize to lower-case
        this.type = type.trim().toLowerCase(Locale.ROOT);
        this.value = value.trim().toLowerCase(Locale.ROOT);

        // Must be one of the two allowed types
        if (!this.type.equals(PERSON) && !this.type.equals(LOCATION)) {
            throw new IllegalArgumentException("Invalid tag type: " + type);
        }
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tag)) return false;
        Tag other = (Tag) o;
        return type.equalsIgnoreCase(other.type)
                && value.equalsIgnoreCase(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type.toLowerCase(), value.toLowerCase());
    }

    @Override
    public String toString() {
        return type + "=" + value;
    }
}
