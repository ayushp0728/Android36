package com.example.android36java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;


import com.example.android36java.model.Album;
import com.example.android36java.model.DataStore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataStore.getInstance().load(this);

        RecyclerView rv = findViewById(R.id.recyclerAlbums);
        rv.setLayoutManager(new LinearLayoutManager(this));

        albumAdapter = new AlbumAdapter(DataStore.getInstance().getAlbums());
        rv.setAdapter(albumAdapter);
        albumAdapter.setOnAlbumClickListener(position -> {

            Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
            intent.putExtra("albumIndex", position);
            startActivity(intent);

        });

        Button add = findViewById(R.id.btnAddAlbum);
        add.setOnClickListener(v -> {

            ArrayList<Album> albums = DataStore.getInstance().getAlbums();

            String name = "Album " + (albums.size() + 1);
            albums.add(new Album(name));

            DataStore.getInstance().save(this);

            albumAdapter.notifyDataSetChanged();
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        DataStore.getInstance().save(this);
    }
}
