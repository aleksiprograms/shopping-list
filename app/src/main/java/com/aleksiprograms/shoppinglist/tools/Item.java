package com.aleksiprograms.shoppinglist.tools;

import java.io.Serializable;

public class Item implements Serializable {

    private long id;
    private String name;
    private long time;
    private long listId;
    private boolean selected;

    public Item(String name, long time, long listId) {
        this.name = name;
        this.time = time;
        this.listId = listId;
        selected = false;
    }

    public Item(long id, String name, long time, long listId) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.listId = listId;
        selected = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}