package com.aleksiprograms.shoppinglist.tools;

import java.io.Serializable;

public class Item implements Serializable {

    private long id;
    private String name;
    private boolean bought;
    private long listId;

    public Item(String name, long listId) {
        this.name = name;
        bought = false;
        this.listId = listId;
    }

    public Item(long id, String name, boolean bought, long listId) {
        this.id = id;
        this.name = name;
        this.bought = bought;
        this.listId = listId;
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

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }
}