package com.example.android36java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android36java.model.Album;
import com.example.android36java.model.DataStore;
import com.example.android36java.model.Photo;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private Album album;
    private PhotoAdapter photoAdapter;
    private static final int REQUEST_PICK = 1;
    private int albumIndex;

    private void deletePhoto(int pos) {
        album.getPhotos().remove(pos);
        DataStore.getInstance().save(this);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        albumIndex = getIntent().getIntExtra("albumIndex", -1);
        album = DataStore.getInstance().getAlbums().get(albumIndex);

        setTitle(album.getName());

        // Search button in toolbar
        ImageButton btnSearch = findViewById(R.id.btnToolbarSearch);
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(AlbumActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        TextView header = findViewById(R.id.tvAlbumHeader);
        header.setText(album.getName());

        RecyclerView rv = findViewById(R.id.recyclerPhotos);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        photoAdapter = new PhotoAdapter(album.getPhotos());
        rv.setAdapter(photoAdapter);

        // Open PhotoViewActivity when a photo is clicked
        photoAdapter.setOnPhotoClickListener(pos -> {
            Intent intent = new Intent(AlbumActivity.this, PhotoViewActivity.class);
            intent.putExtra("albumIndex", albumIndex);
            intent.putExtra("photoIndex", pos);
            startActivity(intent);
        });

        // Hook delete icon to deletePhoto()
        photoAdapter.setOnPhotoDeleteListener(this::deletePhoto);

        // Hook move icon to movePhoto()
        photoAdapter.setOnPhotoMoveListener(this::movePhoto);

        Button add = findViewById(R.id.btnAddPhoto);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_PICK);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // In case photos were changed while this album was in background
        if (photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            final int flags = data.getFlags() &
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            getContentResolver().takePersistableUriPermission(uri, flags);

            album.getPhotos().add(new Photo(uri.toString()));
            DataStore.getInstance().save(this);
            photoAdapter.notifyDataSetChanged();
        }
    }

    // Move photo dialog (used by move icon)
    private void movePhoto(int pos) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Move photo to:");

        ArrayList<Album> all = DataStore.getInstance().getAlbums();
        ArrayList<String> names = new ArrayList<>();
        for (Album a : all) {
            if (a != album) { // exclude current album
                names.add(a.getName());
            }
        }

        b.setItems(names.toArray(new String[0]), (dialog, which) -> {
            Photo photo = album.getPhotos().get(pos);

            Album target = null;
            String chosenName = names.get(which);
            for (Album a : all) {
                if (a.getName().equals(chosenName)) {
                    target = a;
                    break;
                }
            }

            if (target != null && target != album) {
                target.addPhoto(photo);
                album.removePhoto(photo);
                DataStore.getInstance().save(this);
                photoAdapter.notifyDataSetChanged();
            }
        });

        b.show();
    }
}
