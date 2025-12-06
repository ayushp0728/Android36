package com.example.android36java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android36java.model.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Album> albums;
    private OnAlbumClickListener listener;
    public interface OnAlbumClickListener {
        void onAlbumClick(int position);
    }


    public AlbumAdapter(List<Album> albums) {
        this.albums = albums;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new AlbumViewHolder(v);
    }
    public void setOnAlbumClickListener(OnAlbumClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int pos) {
        Album a = albums.get(pos);
        holder.textView.setText(a.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlbumClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public AlbumViewHolder(View v) {
            super(v);
            textView = v.findViewById(android.R.id.text1);
        }
    }
}
