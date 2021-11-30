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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteCreate extends AppCompatActivity {

        List<Note> notes;

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
        imageNote = findViewById(R.id.imageView2);
        textNote = findViewById(R.id.title_tiet);
        descriptionNote = findViewById(R.id.descripton);
        addNoteButton = findViewById(R.id.button_add_note);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.semko_pzpi_18_4_LB_1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        notes = (ArrayList<Note>)getIntent().getSerializableExtra("arrayList");

        selectImage();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String currentTime = sdf.format(new Date());
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleNote = textNote.getText().toString();
                String descriptionNoteString = descriptionNote.getText().toString();
                notes.add(new Note(titleNote, descriptionNoteString, currentTime, priority, linkImage));
                Gson gson = new Gson();
                String json = gson.toJson(notes);

                editor.putString("com.example.semko_pzpi_18_4_LB_1", json);
                editor.commit();
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

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.a_priority:
                if (checked){
                        priority = "A";
                }
                break;
            case R.id.b_priority:
                if (checked){
                    priority = "B";
                }
                break;
            case R.id.c_priority:
                if (checked){
                    priority = "C";
                }
                break;
        }
    }
}