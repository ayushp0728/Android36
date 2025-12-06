package com.example.android36java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android36java.model.Album;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Album> albums;
    private OnAlbumClickListener listener;
    public interface OnAlbumClickListener {
        void onAlbumClick(int position);
    }
    public interface OnRenameListener { void onRename(int pos); }
    public interface OnDeleteListener { void onDelete(int pos); }

    private OnRenameListener renameListener;
    private OnDeleteListener deleteListener;

    public void setOnRenameListener(OnRenameListener l) { renameListener = l; }
    public void setOnDeleteListener(OnDeleteListener l) { deleteListener = l; }


    public AlbumAdapter(List<Album> albums) {
        this.albums = albums;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(v);
    }
    public void setOnAlbumClickListener(OnAlbumClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int pos) {
        Album a = albums.get(pos);
        holder.tvName.setText(a.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onAlbumClick(pos);
        });

        if (renameListener != null) {
            holder.btnRename.setOnClickListener(v -> renameListener.onRename(pos));
        }

        if (deleteListener != null) {
            holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(pos));
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageButton btnRename;
        ImageButton btnDelete;

        public AlbumViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvAlbumName);
            btnRename = v.findViewById(R.id.btnRename);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
