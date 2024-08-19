package com.example.appsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appsqlite.helper.DbHelper;

public class AddEdit extends AppCompatActivity {
    EditText txt_id, txt_name, txt_address;
    Button btn_submit, btn_cancel;
    DbHelper SQLite;
    String id, name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        SQLite = new DbHelper(this);

        txt_id = findViewById(R.id.txt_id);
        txt_name = findViewById(R.id.txt_name);
        txt_address = findViewById(R.id.txt_address);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.TAG_ID);
        name = intent.getStringExtra(MainActivity.TAG_NAME);
        address = intent.getStringExtra(MainActivity.TAG_ADDRESS);

        if (id != null) {
            txt_id.setText(id);
            txt_name.setText(name);
            txt_address.setText(address);
        }

        btn_submit.setOnClickListener(v -> {
            if (txt_id.getText().toString().isEmpty()) {
                save();
            } else {
                edit();
            }
        });

        btn_cancel.setOnClickListener(v -> {
            blank();
            finish();
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            blank();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void blank() {
        txt_name.requestFocus();
        txt_id.setText(null);
        txt_name.setText(null);
        txt_address.setText(null);
    }

    private void save() {
        if (txt_name.getText().toString().trim().isEmpty() || txt_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please input name or address.", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.insert(txt_name.getText().toString().trim(), txt_address.getText().toString().trim());
            blank();
            finish();
        }
    }

    private void edit() {
        if (txt_name.getText().toString().trim().isEmpty() || txt_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please input name or address.", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.update(Integer.parseInt(txt_id.getText().toString().trim()), txt_name.getText().toString().trim(), txt_address.getText().toString().trim());
            blank();
            finish();
        }
    }
}
