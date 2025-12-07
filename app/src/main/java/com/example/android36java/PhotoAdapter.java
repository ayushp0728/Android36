package com.example.android36java;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android36java.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<Photo> photos;

    public PhotoAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    // Click to open photo
    public interface OnPhotoClickListener {
        void onPhotoClick(int pos);
    }

    // Click delete icon
    public interface OnPhotoDeleteListener {
        void onPhotoDelete(int pos);
    }

    private OnPhotoClickListener clickListener;
    private OnPhotoDeleteListener deleteListener;

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnPhotoDeleteListener(OnPhotoDeleteListener listener) {
        this.deleteListener = listener;
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

        // thumbnail
        Uri uri = Uri.parse(p.getUriString());
        holder.imageView.setImageURI(uri);

        // filename from URI (last path segment)
        String name = uri.getLastPathSegment();
        if (name == null || name.isEmpty()) {
            name = "Photo";
        }
        holder.tvName.setText(name);

        // open photo when clicking card / image / text
        View.OnClickListener openListener = v -> {
            if (clickListener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    clickListener.onPhotoClick(pos);
                }
            }
        };
        holder.itemView.setOnClickListener(openListener);
        holder.imageView.setOnClickListener(openListener);
        holder.tvName.setOnClickListener(openListener);

        // delete photo when clicking trash icon
        if (deleteListener != null) {
            holder.btnDelete.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    deleteListener.onPhotoDelete(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    static class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        ImageButton btnDelete;

        public PhotoHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.imageThumb);
            tvName = v.findViewById(R.id.tvPhotoName);
            btnDelete = v.findViewById(R.id.btnDeletePhoto);
        }
    }
}
