package com.example.appsqlite;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.appsqlite.adapter.Adapter;
import com.example.appsqlite.helper.DbHelper;
import com.example.appsqlite.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private AlertDialog.Builder dialog;
    private List<Data> itemList = new ArrayList<>();
    private Adapter adapter;
    private DbHelper SQLite;

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SQLite = new DbHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        listView = findViewById(R.id.list_view);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEdit.class);
            startActivity(intent);
        });

        adapter = new Adapter(MainActivity.this, itemList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            final String idx = itemList.get(position).getId();
            final String name = itemList.get(position).getName();
            final String address = itemList.get(position).getAddress();

            final CharSequence[] dialogItems = {"Edit", "Delete"};
            dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setCancelable(true);
            dialog.setItems(dialogItems, (dialog, which) -> {
                switch (which) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, AddEdit.class);
                        intent.putExtra(TAG_ID, idx);
                        intent.putExtra(TAG_NAME, name);
                        intent.putExtra(TAG_ADDRESS, address);
                        startActivity(intent);
                        break;
                    case 1:
                        SQLite.delete(Integer.parseInt(idx));
                        itemList.clear();
                        getAllData();
                        break;
                }
            }).show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        getAllData();
    }

    private void getAllData() {
        ArrayList<HashMap<String, String>> row = SQLite.getAllData();
        for (int i = 0; i < row.size(); i++) {
            String id = row.get(i).get(TAG_ID);
            String name = row.get(i).get(TAG_NAME);
            String address = row.get(i).get(TAG_ADDRESS);

            Data data = new Data();
            data.setId(id);
            data.setName(name);
            data.setAddress(address);

            itemList.add(data);
        }
        adapter.notifyDataSetChanged();
    }
}
