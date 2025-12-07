package com.example.android36java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android36java.model.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Album> albums;

    // Click interfaces
    public interface OnAlbumClickListener {
        void onAlbumClick(int position);
    }
    public interface OnRenameListener {
        void onRename(int pos);
    }
    public interface OnDeleteListener {
        void onDelete(int pos);
    }

    private OnAlbumClickListener albumClickListener;
    private OnRenameListener renameListener;
    private OnDeleteListener deleteListener;

    public AlbumAdapter(List<Album> albums) {
        this.albums = albums;
    }

    public void setOnAlbumClickListener(OnAlbumClickListener listener) {
        this.albumClickListener = listener;
    }

    public void setOnRenameListener(OnRenameListener l) {
        this.renameListener = l;
    }

    public void setOnDeleteListener(OnDeleteListener l) {
        this.deleteListener = l;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int pos) {
        Album a = albums.get(pos);
        holder.tvName.setText(a.getName());

        // Open album when clicking thumbnail OR name OR whole item
        View.OnClickListener openListener = v -> {
            if (albumClickListener != null) {
                albumClickListener.onAlbumClick(holder.getAdapterPosition());
            }
        };

        holder.itemView.setOnClickListener(openListener);
        holder.tvName.setOnClickListener(openListener);
        holder.imageThumb.setOnClickListener(openListener);

        // Rename
        if (renameListener != null) {
            holder.btnRename.setOnClickListener(v ->
                    renameListener.onRename(holder.getAdapterPosition()));
        }

        // Delete
        if (deleteListener != null) {
            holder.btnDelete.setOnClickListener(v ->
                    deleteListener.onDelete(holder.getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumb;
        TextView tvName;
        ImageButton btnRename;
        ImageButton btnDelete;

        public AlbumViewHolder(View v) {
            super(v);
            imageThumb = v.findViewById(R.id.imageAlbumThumb);
            tvName = v.findViewById(R.id.tvAlbumName);
            btnRename = v.findViewById(R.id.btnRename);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
