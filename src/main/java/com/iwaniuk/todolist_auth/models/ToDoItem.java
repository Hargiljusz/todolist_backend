package com.iwaniuk.todolist_auth.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class ToDoItem {
    @Id
    private String id;

    private String title;

    private String note;

    private String userId;

    private String rgbColorBackground;

    private String rgbColorText;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isOneDay;

    public ToDoItem() {
    }

    public ToDoItem(String title, String note, String userId, String rgbColorBackground, String rgbColorText, LocalDate startDate, LocalDate endDate, boolean isOneDay) {
        this.title = title;
        this.note = note;
        this.userId = userId;
        this.rgbColorBackground = rgbColorBackground;
        this.rgbColorText = rgbColorText;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOneDay = isOneDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRgbColorBackground() {
        return rgbColorBackground;
    }

    public void setRgbColorBackground(String rgbColorBackground) {
        this.rgbColorBackground = rgbColorBackground;
    }

    public String getRgbColorText() {
        return rgbColorText;
    }

    public void setRgbColorText(String rgbColorText) {
        this.rgbColorText = rgbColorText;
    }

    public boolean isOneDay() {
        return isOneDay;
    }

    public void setOneDay(boolean oneDay) {
        isOneDay = oneDay;
    }
}
