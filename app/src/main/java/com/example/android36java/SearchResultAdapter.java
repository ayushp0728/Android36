package com.example.android36java;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android36java.model.Photo;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultHolder> {

    private List<Photo> photos;

    public SearchResultAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ResultHolder(v);
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
        Photo p = photos.get(position);
        Uri uri = Uri.parse(p.getUriString());
        holder.imageView.setImageURI(uri);

        String name = uri.getLastPathSegment();
        if (name == null || name.isEmpty()) {
            name = "Photo";
        }
        holder.tvName.setText(name);

        // no click behavior
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class ResultHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;

        public ResultHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageThumb);
            tvName = v.findViewById(R.id.tvPhotoName);
        }
    }
}
