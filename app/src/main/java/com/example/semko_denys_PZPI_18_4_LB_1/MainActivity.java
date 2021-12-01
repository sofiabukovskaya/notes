package com.example.semko_denys_PZPI_18_4_LB_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
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


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.semko_pzpi_18_4_LB_1", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        titleNote = findViewById(R.id.textView);
        descriptionNote = findViewById(R.id.textView3);
        dateNote = findViewById(R.id.textView2);
        noteImportant = findViewById(R.id.textView4);
        noteImage = findViewById(R.id.imageView);

        noteList = new ArrayList<>();

        getDataFromSharedPref();

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
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    public void getDataFromSharedPref() {
        String serializedObject = sharedPreferences.getString("com.example.semko_pzpi_18_4_LB_1", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Note>>(){}.getType();
            noteList = gson.fromJson(serializedObject, type);
        }
    }

    public void openEditActivity(Note note, int position){
        Intent intent = new Intent(this, NoteEdit.class);
        intent.putExtra("note", note);
        intent.putExtra("arrayList", noteList);
        intent.putExtra("index", position);
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
                    getDataFromSharedPref();
                    adapter = new NotesAdapter(this, noteList, new NotesAdapter.LongClickByItemListener() {
                        @Override
                        public void onLongClick(View itemView, int position) {
                            itemView.setOnCreateContextMenuListener(new ContextMenuRecyclerView(position));
                        }
                    });
                    notesListView.setAdapter(adapter);
                } else {
                    item.setChecked(true);
                    adapter.filterNotes("A");
                }
                break;
            case R.id.action_filter_b:
                if (item.isChecked()) {
                    item.setChecked(false);
                    getDataFromSharedPref();
                    adapter = new NotesAdapter(this, noteList, new NotesAdapter.LongClickByItemListener() {
                        @Override
                        public void onLongClick(View itemView, int position) {
                            itemView.setOnCreateContextMenuListener(new ContextMenuRecyclerView(position));
                        }
                    });
                    notesListView.setAdapter(adapter);
                } else {
                    item.setChecked(true);
                    adapter.filterNotes("B");
                }
                break;
            case R.id.action_filter_c:
                if (item.isChecked()) {
                    item.setChecked(false);
                    getDataFromSharedPref();
                    adapter = new NotesAdapter(this, noteList, new NotesAdapter.LongClickByItemListener() {
                        @Override
                        public void onLongClick(View itemView, int position) {
                            itemView.setOnCreateContextMenuListener(new ContextMenuRecyclerView(position));
                        }
                    });
                    notesListView.setAdapter(adapter);
                } else {
                    item.setChecked(true);
                    adapter.filterNotes("C");
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
                    openEditActivity(noteList.get(position), position);
                    break;
                case R.id.action_remove:
                    adapter.removeNote(position, editor);
                    break;
            }
            return false;
        }
    }
}