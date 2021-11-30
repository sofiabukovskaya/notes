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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
        String serializedObject = sharedPreferences.getString("com.example.semko_pzpi_18_4_LB_1", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Note>>(){}.getType();
            noteList = gson.fromJson(serializedObject, type);
        }

         notesListView = findViewById(R.id.notes_list);
        notesListView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notesListView.setLayoutManager(linearLayoutManager);

        NotesAdapter adapter = new NotesAdapter(this, noteList);
        notesListView.setAdapter(adapter);
//        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.notes_row,  noteList);
//        notesListView.setAdapter(arrayAdapter);

//        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
//                intent.putExtra("note_id", i);
//                startActivity(intent);
//            }
//        });
//
//        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                new AlertDialog.Builder(MainActivity.this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Are you sure?")
//                        .setMessage("Do you want to delete this note?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                noteList.remove(i);
//                                arrayAdapter.notifyDataSetChanged();
//
//                                HashSet<String> set = new HashSet(MainActivity.noteList);
//                                sharedPreferences.edit().putStringSet("notes", set).apply();
//                            }
//                        }).setNegativeButton("No", null).show();
//                return true;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, NoteCreate.class);
            intent.putExtra("arrayList", noteList);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}