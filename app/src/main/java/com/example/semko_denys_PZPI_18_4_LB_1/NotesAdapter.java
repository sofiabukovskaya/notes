package com.example.semko_denys_PZPI_18_4_LB_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements Filterable {
    public interface LongClickByItemListener {
        void onLongClick(View itemView, int position);
    }

      LayoutInflater inflater;
      List<Note> notesList = new ArrayList<>();
      List<Note> searchNotesList;



     LongClickByItemListener longClickByItemListener;

    NotesAdapter(Context context, ArrayList<Note> notes, LongClickByItemListener longClickByItemListener) {
        this.notesList = notes;
        this.inflater = LayoutInflater.from(context);
        this.searchNotesList = new ArrayList<>(notes);
        this.longClickByItemListener = longClickByItemListener;
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

    @Override
    public Filter getFilter() {
        return notesFilter;
    }

    private Filter notesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Note> filteringList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0) {
                filteringList.addAll(searchNotesList);
            } else {
                String filteringPattern = charSequence.toString().toLowerCase().trim();
                for(Note note : searchNotesList) {
                        if(note.getDescription().toLowerCase().contains(filteringPattern)) {
                            filteringList.add(note);
                        }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteringList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notesList.clear();
            notesList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

     void filterNotes(String filerImportant) {
         searchNotesList.clear();
        for (Note note : notesList) {
            if (note.getImportance().contains(filerImportant)) {
                searchNotesList.add(note);
            }
        }
        notesList.clear();
        notesList.addAll(searchNotesList);
        notifyDataSetChanged();
    }

    public void removeNote(int position, SharedPreferences.Editor editor) {
         editor.clear();
        notesList.remove(position);
        Gson gson = new Gson();
        String json = gson.toJson(notesList);
        editor.putString("com.example.semko_pzpi_18_4_LB_1", json);
        editor.commit();
        notifyItemRemoved(position);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView title, description, date, important;

          ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
            title = view.findViewById(R.id.textView);
            description = view.findViewById(R.id.textView3);
            date = view.findViewById(R.id.textView2);
            important = view.findViewById(R.id.textView4);

              view.setOnLongClickListener(new View.OnLongClickListener() {
                  @Override
                  public boolean onLongClick(View v) {
                      longClickByItemListener.onLongClick(v, getAdapterPosition());
                      return false;
                  }
              });
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
