package com.example.android36java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;


import com.example.android36java.model.Album;
import com.example.android36java.model.DataStore;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AlbumAdapter albumAdapter;

    private void showRenameDialog(Album album) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename album");

        final EditText input = new EditText(this);
        input.setText(album.getName());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newName = input.getText().toString().trim();

            if (newName.isEmpty()) return;

            // prevent duplicate
            for (Album other : DataStore.getInstance().getAlbums()) {
                if (other != album && other.getName().equalsIgnoreCase(newName)) {
                    Toast.makeText(this, "Album exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            album.rename(newName);
            DataStore.getInstance().save(this);
            albumAdapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


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
        albumAdapter.setOnRenameListener(pos -> {
            Album a = DataStore.getInstance().getAlbums().get(pos);
            showRenameDialog(a);
        });

        albumAdapter.setOnDeleteListener(pos -> {
            DataStore.getInstance().getAlbums().remove(pos);
            DataStore.getInstance().save(this);
            albumAdapter.notifyDataSetChanged();
        });

        Button add = findViewById(R.id.btnAddAlbum);
        add.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Album");

            final EditText input = new EditText(this);
            builder.setView(input);

            builder.setPositiveButton("Create", (dialog, which) -> {
                String name = input.getText().toString().trim();
                if (name.isEmpty()) return;

                // prevent duplicate
                for (Album a : DataStore.getInstance().getAlbums()) {
                    if (a.getName().equalsIgnoreCase(name)) {
                        Toast.makeText(this, "Album exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                DataStore.getInstance().getAlbums().add(new Album(name));
                DataStore.getInstance().save(this);
                albumAdapter.notifyDataSetChanged();
            });

            builder.setNegativeButton("Cancel", null);
            builder.show();
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        DataStore.getInstance().save(this);
    }
}
