package com.example.semko_denys_PZPI_18_4_LB_1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
      LayoutInflater inflater;
      List<Note> notesList = new ArrayList<>();


    NotesAdapter(Context context, ArrayList<Note> notes) {
        this.notesList = notes;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notes_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.bindData(note);

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView title, description, date, important;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
            title = view.findViewById(R.id.textView);
            description = view.findViewById(R.id.textView3);
            date = view.findViewById(R.id.textView2);
            important = view.findViewById(R.id.textView4);
        }

        void bindData (Note note) {
            if (note.getIcon() == null) {
                Picasso.get()
                        .load(R.drawable.ic_baseline_photo_24)
                        .centerCrop()
                        .fit()
                        .into(imageView);
            }
            else {
                Picasso.get()
                        .load(Uri.parse(note.getIcon()))
                        .centerCrop()
                        .fit()
                        .into(imageView);
            }
            title.setText(note.getTitle());
            if (note.getTitle() == null) {
                title.setText("");
            }
            else {
                title.setText(note.getTitle());
            }
            if (note.getDescription() == null) {
                description.setText("");
            }
            else {
                description.setText(note.getDescription());
            }
            date.setText(note.getTime());
            important.setText(note.getImportance());
        }
    }
}
