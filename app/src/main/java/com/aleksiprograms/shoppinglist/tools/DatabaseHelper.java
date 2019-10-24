package com.aleksiprograms.shoppinglist.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATABASE_SHOPPING_LIST_APP";
    private static final String TABLE_LISTS = "TABLE_LISTS";
    private static final String TABLE_ITEMS = "TABLE_ITEMS";
    private static final String COLUMN_LIST_ID   = "COLUMN_LIST_ID";
    private static final String COLUMN_LIST_TIME = "COLUMN_LIST_TIME";
    private static final String COLUMN_ITEM_ID      = "COLUMN_ITEM_ID";
    private static final String COLUMN_ITEM_NAME    = "COLUMN_ITEM_NAME";
    private static final String COLUMN_ITEM_TIME    = "COLUMN_ITEM_TIME";
    private static final String COLUMN_ITEM_LIST_ID = "COLUMN_ITEM_LIST_ID";

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_LISTS + "("
                + COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_LIST_TIME + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + "("
                + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ITEM_NAME + " TEXT,"
                + COLUMN_ITEM_TIME + " INTEGER,"
                + COLUMN_ITEM_LIST_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_ITEM_LIST_ID + ") REFERENCES "
                + TABLE_LISTS + "(" + COLUMN_LIST_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public static long insertList(List list, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST_TIME, list.getTime());
        long i = db.insert(TABLE_LISTS, null, values);
        db.close();
        return i;
    }

    public static ArrayList<List> getAllLists(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        ArrayList<List> lists = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_LISTS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            List list = new List(
                    Long.parseLong(cursor.getString(0)),
                    Long.parseLong(cursor.getString(1)));
            lists.add(list);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return lists;
    }

    public static int deleteAllLists(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_LISTS, null, null);
        db.close();
        return i;
    }

    public static int updateList(List list, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST_TIME, list.getTime());
        int i = db.update(TABLE_LISTS, values, COLUMN_LIST_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
        db.close();
        return i;
    }

    public static int deleteList(List list, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_LISTS, COLUMN_LIST_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
        db.close();
        return i;
    }

    public static long insertItem(Item item, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, item.getName());
        values.put(COLUMN_ITEM_TIME, item.getTime());
        values.put(COLUMN_ITEM_LIST_ID, item.getListId());
        long i = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return i;
    }

    public static ArrayList<Item> getAllItemsOfList(List list, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        ArrayList<Item> items = new ArrayList<>();
        Cursor cursor;
        cursor = db.query(
                TABLE_ITEMS,
                new String[] {COLUMN_ITEM_ID, COLUMN_ITEM_NAME, COLUMN_ITEM_TIME, COLUMN_ITEM_LIST_ID},
                COLUMN_ITEM_LIST_ID + " = " + list.getId(),
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = new Item(
                    Long.parseLong(cursor.getString(0)),
                    cursor.getString(1),
                    Long.parseLong(cursor.getString(2)),
                    Long.parseLong(cursor.getString(3)));
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return items;
    }

    public static int deleteAllItems(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_ITEMS, null, null);
        db.close();
        return i;
    }

    public static int updateItem(Item item, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, item.getName());
        values.put(COLUMN_ITEM_TIME, item.getTime());
        values.put(COLUMN_ITEM_LIST_ID, item.getListId());
        int i = db.update(TABLE_ITEMS, values, COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return i;
    }

    public static int deleteItem(Item item, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_ITEMS, COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return i;
    }
}