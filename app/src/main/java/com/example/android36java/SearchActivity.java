package com.example.android36java;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.view.View;   // <-- needed for onItemSelected(...) parameter

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android36java.model.Album;
import com.example.android36java.model.DataStore;
import com.example.android36java.model.Photo;
import com.example.android36java.model.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private Spinner spinnerFirstTagType;
    private AutoCompleteTextView etFirstValue;

    private Spinner spinnerOp;
    private Spinner spinnerSecondTagType;
    private AutoCompleteTextView etSecondValue;

    private Button btnSearch;
    private TextView tvResultCount;
    private RecyclerView recyclerResults;

    private SearchResultAdapter resultAdapter;
    private List<Photo> allPhotos = new ArrayList<>();
    private List<Photo> resultPhotos = new ArrayList<>();

    // for autocomplete
    private List<String> personValues = new ArrayList<>();
    private List<String> locationValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // toolbar title already "Photos Gallery" from XML if you set it there
        setTitle("Search Photos");

        spinnerFirstTagType = findViewById(R.id.spinnerFirstTagType);
        etFirstValue = findViewById(R.id.etFirstValue);

        spinnerOp = findViewById(R.id.spinnerOp);

        spinnerSecondTagType = findViewById(R.id.spinnerSecondTagType);
        etSecondValue = findViewById(R.id.etSecondValue);

        btnSearch = findViewById(R.id.btnSearch);
        tvResultCount = findViewById(R.id.tvResultCount);
        recyclerResults = findViewById(R.id.recyclerResults);

        recyclerResults.setLayoutManager(new GridLayoutManager(this, 3));
        resultAdapter = new SearchResultAdapter(resultPhotos);
        recyclerResults.setAdapter(resultAdapter);

        // load data for search / autocomplete
        loadAllPhotosAndTags();

        setupTagTypeSpinners();
        setupOpSpinner();
        setupAutocompleteBehaviors();

        btnSearch.setOnClickListener(v -> runSearch());
    }

    private void loadAllPhotosAndTags() {
        List<Album> albums = DataStore.getInstance().getAlbums();
        Set<String> personSet = new HashSet<>();
        Set<String> locationSet = new HashSet<>();

        for (Album a : albums) {
            for (Photo p : a.getPhotos()) {
                allPhotos.add(p);

                for (Tag t : p.getTags()) {
                    if (Tag.PERSON.equals(t.getType())) {
                        personSet.add(t.getValue());
                    } else if (Tag.LOCATION.equals(t.getType())) {
                        locationSet.add(t.getValue());
                    }
                }
            }
        }

        personValues.clear();
        personValues.addAll(personSet);

        locationValues.clear();
        locationValues.addAll(locationSet);
    }

    private void setupTagTypeSpinners() {
        ArrayAdapter<String> tagTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{Tag.PERSON, Tag.LOCATION}
        );
        tagTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFirstTagType.setAdapter(tagTypeAdapter);
        spinnerSecondTagType.setAdapter(tagTypeAdapter);
    }

    private void setupOpSpinner() {
        ArrayAdapter<String> opAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"NONE", "AND", "OR"}
        );
        opAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOp.setAdapter(opAdapter);
    }

    private void setupAutocompleteBehaviors() {
        // Two adapters: one for person values, one for location values
        ArrayAdapter<String> personAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                personValues
        );
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                locationValues
        );

        // helper to attach correct adapter based on spinner selection
        spinnerFirstTagType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String type = (String) spinnerFirstTagType.getSelectedItem();
                        if (Tag.PERSON.equals(type)) {
                            etFirstValue.setAdapter(personAdapter);
                        } else {
                            etFirstValue.setAdapter(locationAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );

        spinnerSecondTagType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String type = (String) spinnerSecondTagType.getSelectedItem();
                        if (Tag.PERSON.equals(type)) {
                            etSecondValue.setAdapter(personAdapter);
                        } else {
                            etSecondValue.setAdapter(locationAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );

        // trigger once to set initial adapters
        if (spinnerFirstTagType.getSelectedItem() != null) {
            String type = (String) spinnerFirstTagType.getSelectedItem();
            etFirstValue.setAdapter(Tag.PERSON.equals(type) ? personAdapter : locationAdapter);
        }
        if (spinnerSecondTagType.getSelectedItem() != null) {
            String type = (String) spinnerSecondTagType.getSelectedItem();
            etSecondValue.setAdapter(Tag.PERSON.equals(type) ? personAdapter : locationAdapter);
        }

        // show suggestions after 1 character
        etFirstValue.setThreshold(1);
        etSecondValue.setThreshold(1);
    }

    private void runSearch() {
        String type1 = (String) spinnerFirstTagType.getSelectedItem();
        String value1 = etFirstValue.getText().toString().trim().toLowerCase();

        if (TextUtils.isEmpty(type1) || TextUtils.isEmpty(value1)) {
            // first tag is required; you can show a Toast if you want
            tvResultCount.setText("0 results:");
            resultPhotos.clear();
            resultAdapter.notifyDataSetChanged();
            return;
        }

        Tag q1;
        try {
            q1 = new Tag(type1, value1);
        } catch (IllegalArgumentException e) {
            tvResultCount.setText("0 results:");
            resultPhotos.clear();
            resultAdapter.notifyDataSetChanged();
            return;
        }

        String op = (String) spinnerOp.getSelectedItem();

        Tag q2 = null;
        String value2 = etSecondValue.getText().toString().trim().toLowerCase();
        boolean secondUsed = !TextUtils.isEmpty(value2) && !"NONE".equals(op);

        if (secondUsed) {
            String type2 = (String) spinnerSecondTagType.getSelectedItem();
            try {
                q2 = new Tag(type2, value2);
            } catch (IllegalArgumentException e) {
                q2 = null;
                secondUsed = false;
            }
        }

        resultPhotos.clear();

        for (Photo p : allPhotos) {
            boolean m1 = matchesTag(p, q1);
            boolean m2 = secondUsed && q2 != null && matchesTag(p, q2);

            boolean match;
            if (!secondUsed || q2 == null || "NONE".equals(op)) {
                match = m1;
            } else if ("AND".equals(op)) {
                match = m1 && m2;
            } else { // "OR"
                match = m1 || m2;
            }

            if (match) {
                resultPhotos.add(p);
            }
        }

        tvResultCount.setText(resultPhotos.size() + " results:");
        resultAdapter.notifyDataSetChanged();
    }

    private boolean matchesTag(Photo p, Tag query) {
        for (Tag t : p.getTags()) {
            if (t.equals(query)) {
                return true;
            }
        }
        return false;
    }
}
