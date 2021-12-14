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
import com.example.semko_denys_PZPI_18_4_LB_1.db.DatabaseHelper;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteCreate extends AppCompatActivity {
     Note note;

     ImageView imageNote;
     EditText textNote;
     EditText descriptionNote;
     Button addNoteButton;
     String linkImage;
     String priority = "A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        imageNote = findViewById(R.id.imageEditNote);
        textNote = findViewById(R.id.title_edit);
        descriptionNote = findViewById(R.id.title_desc_edit);
        addNoteButton = findViewById(R.id.button_edit_note);

        DatabaseHelper db = new DatabaseHelper(this);

        selectImage();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss ", Locale.ENGLISH);
        String currentTime = sdf.format(new Date());

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleNote = textNote.getText().toString();
                String descriptionNoteString = descriptionNote.getText().toString();
                note = new Note(titleNote, descriptionNoteString, currentTime, priority, linkImage);

                db.addNote(note);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
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

    public void onRadioButtonClickedCreate(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.a_priority_create:
                if (checked){
                        priority = "A";
                }
                break;
            case R.id.b_priority_create:
                if (checked){
                    priority = "B";
                }
                break;
            case R.id.c_priority_create:
                if (checked){
                    priority = "C";
                }
                break;
        }
    }
}