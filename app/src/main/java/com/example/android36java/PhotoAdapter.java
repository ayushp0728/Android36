package com.example.android36java;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android36java.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<Photo> photos;

    public PhotoAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        Photo p = photos.get(position);

        Uri uri = Uri.parse(p.getUriString());
        holder.imageView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageThumb);
        }
    }
}
