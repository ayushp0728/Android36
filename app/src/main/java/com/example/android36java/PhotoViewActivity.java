package com.example.android36java;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android36java.model.Album;
import com.example.android36java.model.DataStore;
import com.example.android36java.model.Photo;
import com.example.android36java.model.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity {

    private Album album;
    private int albumIndex;
    private int photoIndex;

    private ImageView imageFull;
    private TextView tvPhotoTitle;
    private LinearLayout tagContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        // read which album/photo we are viewing
        Intent intent = getIntent();
        albumIndex = intent.getIntExtra("albumIndex", -1);
        photoIndex = intent.getIntExtra("photoIndex", -1);

        album = DataStore.getInstance().getAlbums().get(albumIndex);

        imageFull = findViewById(R.id.imageFull);
        tvPhotoTitle = findViewById(R.id.tvPhotoTitle);
        tagContainer = findViewById(R.id.tagContainer);

        ImageButton btnPrev = findViewById(R.id.btnPrev);
        ImageButton btnNext = findViewById(R.id.btnNext);
        Button btnAddTag = findViewById(R.id.btnAddTag);

        // show initial photo
        showCurrentPhoto();

        // slideshow navigation
        btnPrev.setOnClickListener(v -> {
            if (photoIndex > 0) {
                photoIndex--;
                showCurrentPhoto();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (photoIndex < album.getPhotos().size() - 1) {
                photoIndex++;
                showCurrentPhoto();
            }
        });

        // add tag dialog
        btnAddTag.setOnClickListener(v -> showAddTagDialog());
    }

    // convenience
    private Photo getCurrentPhoto() {
        return album.getPhotos().get(photoIndex);
    }

    private void showCurrentPhoto() {
        Photo p = getCurrentPhoto();
        Uri uri = Uri.parse(p.getUriString());

        imageFull.setImageURI(uri);

        String name = uri.getLastPathSegment();
        if (name == null || name.isEmpty()) {
            name = "Photo";
        }
        tvPhotoTitle.setText(name);

        refreshTags();
    }

    private void refreshTags() {
        tagContainer.removeAllViews();

        List<Tag> tags = new ArrayList<>(getCurrentPhoto().getTags());

        // sort: location first, then person, then by value
        Collections.sort(tags, new Comparator<Tag>() {
            @Override
            public int compare(Tag t1, Tag t2) {
                boolean t1Loc = t1.getType().equals(Tag.LOCATION);
                boolean t2Loc = t2.getType().equals(Tag.LOCATION);

                // location tags before person tags
                if (t1Loc && !t2Loc) return -1;
                if (!t1Loc && t2Loc) return 1;

                // same type -> sort by value
                if (t1.getType().equalsIgnoreCase(t2.getType())) {
                    return t1.getValue().compareToIgnoreCase(t2.getValue());
                }

                // fallback compare by type name
                return t1.getType().compareToIgnoreCase(t2.getType());
            }
        });

        // inflate rows
        for (Tag tag : tags) {
            View row = getLayoutInflater()
                    .inflate(R.layout.item_tag, tagContainer, false);

            TextView tv = row.findViewById(R.id.tvTagText);
            ImageButton btnDelete = row.findViewById(R.id.btnDeleteTag);

            tv.setText(tag.getType() + ": " + tag.getValue());

            btnDelete.setOnClickListener(v -> {
                getCurrentPhoto().removeTag(tag);
                DataStore.getInstance().save(this);
                refreshTags();
            });

            tagContainer.addView(row);
        }
    }

    private void showAddTagDialog() {
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_add_tag, null, false);

        Spinner spinnerType = dialogView.findViewById(R.id.spinnerTagType);
        EditText etValue = dialogView.findViewById(R.id.etTagValue);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{Tag.PERSON, Tag.LOCATION}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        new AlertDialog.Builder(this)
                .setTitle("Add Tag")
                .setView(dialogView)
                .setPositiveButton("Add", (d, which) -> {
                    String type = (String) spinnerType.getSelectedItem();
                    String value = etValue.getText().toString().trim();

                    if (!value.isEmpty()) {
                        Photo photo = getCurrentPhoto();

                        // If adding a location tag, remove any existing location tag
                        if (type.equals(Tag.LOCATION)) {
                            List<Tag> existing = new ArrayList<>(photo.getTags());
                            for (Tag t : existing) {
                                if (t.getType().equals(Tag.LOCATION)) {
                                    photo.removeTag(t);
                                }
                            }
                        }

                        Tag newTag = new Tag(type, value);
                        photo.addTag(newTag);

                        DataStore.getInstance().save(this);
                        refreshTags();
                    }


                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
