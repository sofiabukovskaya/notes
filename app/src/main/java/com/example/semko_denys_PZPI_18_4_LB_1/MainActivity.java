package com.example.semko_denys_PZPI_18_4_LB_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView notesListView;

    TextView titleNote;
    TextView descriptionNote;
    TextView dateNote;
    TextView noteImportant;
    ImageView noteImage;

    static List<Note> noteList;
    static ArrayAdapter arrayAdapter;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.semko_pzpi_18_4_LB_1", Context.MODE_PRIVATE);

        notesListView = findViewById(R.id.notes_list);
        titleNote = findViewById(R.id.textView);
        descriptionNote = findViewById(R.id.textView3);
        dateNote = findViewById(R.id.textView2);
        noteImportant = findViewById(R.id.textView4);
        noteImage = findViewById(R.id.imageView);

        noteList = new ArrayList<>();
        
        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.notes_row,  noteList);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
                intent.putExtra("note_id", i);
                startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                noteList.remove(i);
                                arrayAdapter.notifyDataSetChanged();

                                HashSet<String> set = new HashSet(MainActivity.noteList);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            }
        });
    }
}