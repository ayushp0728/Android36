package com.example.android36java;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android36java.model.DataStore;

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        int index = getIntent().getIntExtra("albumIndex", -1);

        if (index != -1) {
            // show album name in action bar
            setTitle(DataStore.getInstance().getAlbums().get(index).getName());
        }
    }
}