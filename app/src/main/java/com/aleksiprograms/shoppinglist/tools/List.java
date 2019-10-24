package com.aleksiprograms.shoppinglist.tools;

import java.io.Serializable;
import java.util.ArrayList;

public class List implements Serializable {

    private long id;
    private long time;
    private ArrayList<Item> items;

    public List(long time) {
        this.time = time;
        items = new ArrayList<Item>();
    }

    public List(long id, long time) {
        this.id = id;
        this.time = time;
        items = new ArrayList<Item>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}