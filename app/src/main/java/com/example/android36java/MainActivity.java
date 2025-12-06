package com.example.android36java;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android36java.model.DataStore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataStore.getInstance().load(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataStore.getInstance().save(this);
    }
}
