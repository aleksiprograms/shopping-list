package com.aleksiprograms.shoppinglist.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aleksiprograms.shoppinglist.R;
import com.aleksiprograms.shoppinglist.adapters.ItemsAdapter;
import com.aleksiprograms.shoppinglist.tools.DatabaseHelper;
import com.aleksiprograms.shoppinglist.tools.Item;
import com.aleksiprograms.shoppinglist.tools.List;

import java.util.ArrayList;

public class ItemsActivity extends AppCompatActivity {

    private long listId;
    private static ArrayList<Item> itemsList;
    private ListView listView;
    private EditText editTextItem;
    private Item itemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarItems);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_items));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        List list = (List) intent.getSerializableExtra(
                getResources().getString(R.string.intent_list));
        listId = list.getId();
        if (intent.getBooleanExtra(
                getResources().getString(R.string.intent_new_list),
                true)) {
            itemsList = new ArrayList<Item>();
        } else {
            itemsList = DatabaseHelper.getAllItemsOfList(listId, getApplicationContext());
        }

        listView = (ListView) findViewById(R.id.listViewItems);
        ItemsAdapter listAdapter = new ItemsAdapter(getApplicationContext(), itemsList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.textViewItemName);
                if (itemsList.get(position).isBought()) {
                    itemsList.get(position).setBought(false);
                    DatabaseHelper.updateItem(
                            itemsList.get(position),
                            getApplicationContext());
                    textView.setPaintFlags(
                            textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    textView.setTextColor(
                            getResources().getColor(R.color.colorWhite));
                } else {
                    itemsList.get(position).setBought(true);
                    DatabaseHelper.updateItem(
                            itemsList.get(position),
                            getApplicationContext());
                    textView.setPaintFlags(
                            textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    textView.setTextColor(
                            getResources().getColor(R.color.colorRed));
                }
            }
        });
        registerForContextMenu(listView);

        editTextItem = (EditText) findViewById(R.id.editTextItem);
        Button buttonSaveItem = (Button) findViewById(R.id.buttonAddItem);
        buttonSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(editTextItem.getText()).equals("")) {
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(R.string.toast_empty_item),
                            Toast.LENGTH_LONG).show();
                } else {
                    if (itemToEdit != null) {
                        itemToEdit.setName(String.valueOf(editTextItem.getText()));
                        DatabaseHelper.updateItem(
                                itemToEdit,
                                getApplicationContext());
                        itemToEdit = null;
                    } else {
                        Item item = new Item(
                                String.valueOf(editTextItem.getText()),
                                listId);
                        DatabaseHelper.insertItem(item, getApplicationContext());
                        itemsList.add(item);
                    }
                }
                listView.invalidateViews();
                editTextItem.setText("");
                listViewToEnd();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            case R.id.settingsMenuItemDeleteItems:
                DatabaseHelper.deleteAllItemsOfList(listId, getApplicationContext());
                itemsList.clear();
                listView.invalidateViews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(
            ContextMenu contextMenu,
            View view,
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        if (view.getId() == R.id.listViewItems) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.context_menu_item, contextMenu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.contextMenuItemEdit:
                itemToEdit = itemsList.get(info.position);
                editTextItem.setText(itemToEdit.getName());
                editTextItem.setSelection(itemToEdit.getName().length());
                editTextItem.setFocusable(true);
                return true;
            case R.id.contextMenuItemDelete:
                DatabaseHelper.deleteItem(
                        itemsList.get(info.position).getId(), getApplicationContext());
                itemsList.remove(info.position);
                listView.invalidateViews();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void listViewToEnd() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(listView.getCount() - 1);
            }
        });
    }
}