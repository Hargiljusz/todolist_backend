package com.iwaniuk.todolist_auth.models;

import java.util.List;

public class ToDoPage {

    private List<ToDoItem> toDoItemList;
    private int page;
    private int size;
    private boolean isNext;

    public ToDoPage() {
    }

    public ToDoPage(List<ToDoItem> toDoItemList, int page, int size, boolean isNext) {
        this.toDoItemList = toDoItemList;
        this.page = page;
        this.size = size;
        this.isNext = isNext;
    }

    public List<ToDoItem> getToDoItemList() {
        return toDoItemList;
    }

    public void setToDoItemList(List<ToDoItem> toDoItemList) {
        this.toDoItemList = toDoItemList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean next) {
        this.isNext = next;
    }
}
