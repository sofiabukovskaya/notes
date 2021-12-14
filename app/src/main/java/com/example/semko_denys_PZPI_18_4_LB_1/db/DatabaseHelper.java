package com.example.semko_denys_PZPI_18_4_LB_1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.semko_denys_PZPI_18_4_LB_1.data.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "notes_db";
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_TIME = "time";
    private static final String COL_IMPORTANCE = "importance";
    private static final String COL_ICON = "icon";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_IMPORTANCE + " TEXT, " +
                COL_ICON + " TEXT " + ")";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, note.getTitle());
        values.put(COL_DESCRIPTION, note.getDescription());
        values.put(COL_TIME, note.getTime());
        values.put(COL_IMPORTANCE, note.getImportance());
        values.put(COL_ICON, note.getIcon());

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public Note getNote(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { COL_ID,
                        COL_TITLE, COL_DESCRIPTION, COL_TIME, COL_IMPORTANCE }, COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setImportance(cursor.getString(4));
                note.setIcon(cursor.getString(5));

                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }

    public ArrayList<Note> search(String keyword) {
        ArrayList<Note> notes = new ArrayList<Note>();

            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + COL_DESCRIPTION + " like ?", new String[] { "%" + keyword + "%" });
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.setNoteId(Integer.parseInt(cursor.getString(0)));
                    note.setTitle(cursor.getString(1));
                    note.setDescription(cursor.getString(2));
                    note.setTime(cursor.getString(3));
                    note.setImportance(cursor.getString(4));
                    note.setIcon(cursor.getString(5));

                    notes.add(note);
                } while (cursor.moveToNext());
            }

        return notes;
    }

    public  ArrayList<Note> filterByImportant(String important) {
        ArrayList<Note> notes = new ArrayList<Note>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + COL_IMPORTANCE + " like ?", new String[] { "%" + important + "%" });
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setImportance(cursor.getString(4));
                note.setIcon(cursor.getString(5));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        return  notes;
    }

    public int updateNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, note.getTitle());
        values.put(COL_DESCRIPTION, note.getDescription());
        values.put(COL_TIME, note.getTime());
        values.put(COL_IMPORTANCE, note.getImportance());
        values.put(COL_ICON, note.getIcon());

        return db.update(TABLE_NAME, values, COL_ID + " = ?",
                new String[]{String.valueOf(note.getNoteId())});
    }

    public void deleteNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(note.getNoteId()) });
        db.close();
    }

}


