package com.example.semko_denys_PZPI_18_4_LB_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteEdit extends AppCompatActivity {
    List<Note> notes;
    int index;
    Note note;

    ImageView imageNote;
    EditText textNote;
    EditText descriptionNote;
    Button editNoteButton;
    RadioButton radioButton_A;
    RadioButton radioButton_B;
    RadioButton radioButton_C;

    String linkImage;
    String newPriority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        note = (Note) getIntent().getSerializableExtra("note");
        notes = (ArrayList<Note>)getIntent().getSerializableExtra("arrayList");
        index = getIntent().getIntExtra("index",0);
        newPriority = note.getImportance();
        linkImage = note.getIcon();

        imageNote = findViewById(R.id.imageEditNote);
        textNote = findViewById(R.id.title_edit);
        descriptionNote = findViewById(R.id.title_desc_edit);
        editNoteButton = findViewById(R.id.button_edit_note);

        radioButton_A = findViewById(R.id.a_priority_edit);
        radioButton_B = findViewById(R.id.b_priority_edit);
        radioButton_C = findViewById(R.id.c_priority_edit);

        textNote.setText(note.getTitle());
        descriptionNote.setText(note.getDescription());

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.semko_pzpi_18_4_LB_1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(note.getIcon() != null) {
            Picasso.get()
                    .load(Uri.parse(note.getIcon()))
                    .centerCrop()
                    .fit()
                    .into(imageNote);
        }
        checkRadioButtonValue(note.getImportance());
        selectImage();


        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                notes.remove(index);
                notes.add(index, new Note(textNote.getText().toString(),descriptionNote.getText().toString(),note.getTime(),newPriority,linkImage));
                Gson gson = new Gson();
                String json = gson.toJson(notes);

                editor.putString("com.example.semko_pzpi_18_4_LB_1", json);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkRadioButtonValue(String priority) {
        switch (priority){
            case "A":
                radioButton_A.setChecked(true);
                break;
            case "B":
                radioButton_B.setChecked(true);
                break;
            case "C":
                radioButton_C.setChecked(true);
                break;
        }
    }
    private void selectImage() {
        imageNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selector = "image/*";

                Intent getIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                getIntent.setType(selector);

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType(selector);

                Intent chooserIntent = Intent.createChooser(getIntent, "Select image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, 2);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    if(selectedImage != null){
                        Picasso.get()
                                .load(selectedImage)
                                .fit()
                                .centerCrop()
                                .into(imageNote);
                        linkImage = selectedImage.toString();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.a_priority_edit:
                if (checked){
                    newPriority = "A";
                }
                break;
            case R.id.b_priority_edit:
                if (checked){
                    newPriority = "B";
                }
                break;
            case R.id.c_priority_edit:
                if (checked){
                    newPriority = "C";
                }
                break;
        }
    }
}