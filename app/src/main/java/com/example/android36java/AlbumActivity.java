package com.example.android36java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.android36java.model.Album;
import com.example.android36java.model.DataStore;
import com.example.android36java.model.Photo;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private Album album;
    private PhotoAdapter photoAdapter;
    private static final int REQUEST_PICK = 1;

    private void deletePhoto(int pos) {
        album.getPhotos().remove(pos);
        DataStore.getInstance().save(this);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        int index = getIntent().getIntExtra("albumIndex", -1);
        album = DataStore.getInstance().getAlbums().get(index);

        // Optional: keep action bar title in addition to header text
        setTitle(album.getName());

        // 🔹 Set the header TextView to the album's name
        TextView header = findViewById(R.id.tvAlbumHeader);
        header.setText(album.getName());

        RecyclerView rv = findViewById(R.id.recyclerPhotos);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        photoAdapter = new PhotoAdapter(album.getPhotos());
        rv.setAdapter(photoAdapter);

        photoAdapter.setOnPhotoClickListener(pos -> {
            showPhotoOptionsDialog(pos);
        });

        Button add = findViewById(R.id.btnAddPhoto);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_PICK);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) {
            if (data != null) {

                Uri uri = data.getData();

                // 1️⃣ take persistent read access
                final int flags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                getContentResolver().takePersistableUriPermission(uri, flags);

                // 2️⃣ save photo
                album.getPhotos().add(new Photo(uri.toString()));

                // 3️⃣ persist to storage
                DataStore.getInstance().save(this);

                // 4️⃣ update UI
                photoAdapter.notifyDataSetChanged();
            }
        }
    }

    private void showPhotoOptionsDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Photo Options");

        String[] options = {"View Photo", "Delete Photo", "Move Photo"};

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    // For Person B to handle later:
                    // open PhotoViewActivity (not implemented yet)
                    break;

                case 1:
                    deletePhoto(pos);
                    break;

                case 2:
                    movePhoto(pos);
                    break;
            }
        });

        builder.show();
    }

    private void movePhoto(int pos) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Move photo to:");

        ArrayList<Album> all = DataStore.getInstance().getAlbums();
        ArrayList<String> names = new ArrayList<>();
        for (Album a : all) names.add(a.getName());

        b.setItems(names.toArray(new String[0]), (dialog, which) -> {
            Photo photo = album.getPhotos().get(pos);
            Album target = all.get(which);

            if (target != album) {
                target.addPhoto(photo);
                album.removePhoto(photo);
                DataStore.getInstance().save(this);
                photoAdapter.notifyDataSetChanged();
            }
        });

        b.show();
    }
}
