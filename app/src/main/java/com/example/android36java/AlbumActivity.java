package com.example.android36java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

import com.example.android36java.model.Album;
import com.example.android36java.model.DataStore;
import com.example.android36java.model.Photo;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private Album album;
    private PhotoAdapter photoAdapter;
    private static final int REQUEST_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        int index = getIntent().getIntExtra("albumIndex", -1);
        album = DataStore.getInstance().getAlbums().get(index);

        setTitle(album.getName());

        RecyclerView rv = findViewById(R.id.recyclerPhotos);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        photoAdapter = new PhotoAdapter(album.getPhotos());
        rv.setAdapter(photoAdapter);

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
                album.addPhoto(new Photo(data.getData().toString()));
                DataStore.getInstance().save(this);
                photoAdapter.notifyDataSetChanged();
            }
        }
    }
}
