package com.example.semko_denys_PZPI_18_4_LB_1;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;
import com.example.semko_denys_PZPI_18_4_LB_1.db.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
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


     void filterNotes(String filerImportant, DatabaseHelper db) {
        FilterNotesAsyncTask filterNotesAsyncTask = new FilterNotesAsyncTask(filerImportant, db);
        filterNotesAsyncTask.execute();
    }

    public void removeNote(int position, DatabaseHelper databaseHelper) {
        Note note = notesList.get(position);
        databaseHelper.deleteNote(note);
        notesList.remove(position);
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

    class FilterNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        String filter;
        DatabaseHelper db;

        public FilterNotesAsyncTask(String filerImportant, DatabaseHelper db) {
            this.filter = filerImportant;
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            searchNotesList.clear();
            notesList.clear();
            searchNotesList.addAll(db.filterByImportant(filter));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            notesList.addAll(searchNotesList);
            notifyDataSetChanged();
        }
    }

}
