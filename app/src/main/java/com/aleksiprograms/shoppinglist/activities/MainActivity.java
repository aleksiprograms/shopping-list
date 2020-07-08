package com.aleksiprograms.shoppinglist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aleksiprograms.shoppinglist.R;
import com.aleksiprograms.shoppinglist.adapters.ListsAdapter;
import com.aleksiprograms.shoppinglist.tools.DatabaseHelper;
import com.aleksiprograms.shoppinglist.tools.List;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<List> listsList;
    private Intent intent;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLists);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_lists));
        }

        // Delete empty list from database.
        ArrayList<List> tmpList = DatabaseHelper.getAllLists(getApplicationContext());
        for (int i = 0; i < tmpList.size(); i++) {
            if (DatabaseHelper.getAllItemsOfList(
                    tmpList.get(i).getId(),
                    getApplicationContext()).size() == 0) {
                DatabaseHelper.deleteList(tmpList.get(i).getId(), getApplicationContext());
            }
        }

        listsList = DatabaseHelper.getAllLists(getApplicationContext());
        for (int i = 0; i < listsList.size(); i++) {
            listsList.get(i).setItems(DatabaseHelper.getAllItemsOfList(
                    listsList.get(i).getId(),
                    getApplicationContext()));
        }

        listView = (ListView) findViewById(R.id.listViewLists);
        ListsAdapter listAdapter = new ListsAdapter(getApplicationContext(), listsList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                List list = listsList.get(position);
                Intent intent = new Intent(getApplicationContext(), ItemsActivity.class);
                intent.putExtra(getResources().getString(R.string.intent_list), list);
                intent.putExtra(getResources().getString(R.string.intent_new_list), false);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        registerForContextMenu(listView);

        Button buttonAddList = (Button) findViewById(R.id.buttonAddList);
        buttonAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List list = new List(System.currentTimeMillis());
                list.setId(DatabaseHelper.insertList(list, getApplicationContext()));
                intent = new Intent(getApplicationContext(), ItemsActivity.class);
                intent.putExtra(getResources().getString(R.string.intent_list), list);
                intent.putExtra(getResources().getString(R.string.intent_new_list), true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settingsMenuListDeleteLists) {
            DatabaseHelper.deleteAllLists(getApplicationContext());
            listsList.clear();
            listView.invalidateViews();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(
            ContextMenu contextMenu,
            View view,
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        if (view.getId() == R.id.listViewLists) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.context_menu_list, contextMenu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.contextMenuListDelete) {
            DatabaseHelper.deleteList(
                    listsList.get(info.position).getId(), getApplicationContext());
            listsList.remove(info.position);
            listView.invalidateViews();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}