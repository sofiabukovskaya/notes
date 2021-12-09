package com.example.semko_denys_PZPI_18_4_LB_1.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable {
    private int noteId;
    private String title;
    private String description;
    private String time;
    private String importance;
    private String icon;

    public Note() {

    }
    public Note(int noteId, String title, String description, String time, String importance, String icon) {
        this.noteId = noteId;
        this.title = title;
        this.description = description;
        this.time = time;
        this.importance = importance;
        this.icon = icon;
    }

    public Note(String titleNote, String descriptionNote, String currentTime, String priority, String linkImage) {
        this.title = titleNote;
        this.description = descriptionNote;
        this.time = currentTime;
        this.importance = priority;
        this.icon = linkImage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }




}
