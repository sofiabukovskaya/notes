package com.example.semko_denys_PZPI_18_4_LB_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;
import com.example.semko_denys_PZPI_18_4_LB_1.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView notesListView;

    TextView titleNote;
    TextView descriptionNote;
    TextView dateNote;
    TextView noteImportant;
    ImageView noteImage;

     ArrayList<Note> noteList;
    NotesAdapter adapter;

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleNote = findViewById(R.id.textView);
        descriptionNote = findViewById(R.id.textView3);
        dateNote = findViewById(R.id.textView2);
        noteImportant = findViewById(R.id.textView4);
        noteImage = findViewById(R.id.imageView);

        noteList = new ArrayList<>();
        db = new DatabaseHelper(this);

        new LoadDataFromDatabase().execute();


        notesListView = findViewById(R.id.notes_list);
        notesListView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notesListView.setLayoutManager(linearLayoutManager);

         adapter = new NotesAdapter(this, noteList, new NotesAdapter.LongClickByItemListener() {
             @Override
             public void onLongClick(View itemView, int position) {
                 itemView.setOnCreateContextMenuListener(new ContextMenuRecyclerView(position));
             }
         });
        notesListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchNotesByDescription(s);
                return false;
            }
        });

        return true;
    }

    private void searchNotesByDescription(String keyword) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<Note> notes = databaseHelper.search(keyword);
        if (notes != null) {
            notesListView.setAdapter(new NotesAdapter(this, notes, new NotesAdapter.LongClickByItemListener() {
                @Override
                public void onLongClick(View itemView, int position) {
                    itemView.setOnCreateContextMenuListener(new ContextMenuRecyclerView(position));
                }
            }));
        }
    }

    public void openEditActivity(Note note){
        Intent intent = new Intent(this, NoteEdit.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, NoteCreate.class);
                intent.putExtra("arrayList", noteList);
                startActivity(intent);
                break;
            case R.id.action_filter_a:
                if (item.isChecked()) {
                    item.setChecked(false);
                    noteList.clear();
                    new LoadDataFromDatabase().execute();
                } else {
                    item.setChecked(true);
                    adapter.filterNotes("A", db);
                }
                break;
            case R.id.action_filter_b:
                if (item.isChecked()) {
                    item.setChecked(false);
                    noteList.clear();
                    new LoadDataFromDatabase().execute();

                } else {
                    item.setChecked(true);
                    adapter.filterNotes("B", db);
                }
                break;
            case R.id.action_filter_c:
                if (item.isChecked()) {
                    item.setChecked(false);
                    noteList.clear();
                    new LoadDataFromDatabase().execute();
                } else {
                    item.setChecked(true);
                    adapter.filterNotes("C", db);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public class ContextMenuRecyclerView implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private int position;

        ContextMenuRecyclerView(int position) {
            this.position = position;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater menuInflater = MainActivity.this.getMenuInflater();
            menuInflater.inflate(R.menu.menu_edit_note, menu);
            for (int index = 0; index < menu.size(); ++index) {
                menu.getItem(index).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    openEditActivity(noteList.get(position));
                    break;
                case R.id.action_remove:
                    adapter.removeNote(position, db);
                    break;
            }
            return false;
        }
    }

    class LoadDataFromDatabase extends AsyncTask<Void, Void, List<Note>> {
        List<Note> list;
        @Override
        protected List<Note> doInBackground(Void... voids) {
           list=  db.getAllNotes();
           return  list;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            noteList.addAll(list);
            adapter.notifyDataSetChanged();
            super.onPostExecute(notes);
        }
    }
}