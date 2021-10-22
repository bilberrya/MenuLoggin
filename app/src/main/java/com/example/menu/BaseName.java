package com.example.menu;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class BaseName extends AppCompatActivity implements View.OnClickListener {
    Button Add, Clear, DbBtn, BnBtn;
    EditText etName, etPassword;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basename);

        Add = (Button) findViewById(R.id.add2);
        Add.setOnClickListener(this);

        Clear = (Button) findViewById(R.id.clear2);
        Clear.setOnClickListener(this);

        DbBtn = (Button) findViewById(R.id.db2);
        DbBtn.setOnClickListener(this);

        BnBtn = (Button) findViewById(R.id.bn2);
        BnBtn.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.name2);
        etPassword = (EditText) findViewById(R.id.password);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }

    @SuppressLint("ResourceType")
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID2);
            int userIndex = cursor.getColumnIndex(DBHelper.KEY_USER);
            int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            TableLayout dbOutput = findViewById(R.id.output2);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputName = new TextView(this);
                params.weight = 5.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(userIndex));
                outputName.setTextColor(Color.argb(255, 67, 45, 19));
                dbOutputRow.addView(outputName);

                TextView outputPassword = new TextView(this);
                params.weight = 3.0f;
                outputPassword.setLayoutParams(params);
                outputPassword.setText(cursor.getString(passwordIndex));
                outputPassword.setTextColor(Color.argb(255, 67, 45, 19));
                dbOutputRow.addView(outputPassword);

                Button btnDelete = new Button(this);
                btnDelete.setOnClickListener(this);
                params.weight = 1.0f;
                btnDelete.setLayoutParams(params);
                btnDelete.setText("delete");
                btnDelete.setBackgroundResource(R.drawable.round_shape_btn);
                btnDelete.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(btnDelete);

                dbOutput.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bn2:
                break;

            case R.id.db2:
                Intent intent = new Intent(this, DataBase.class);
                startActivity(intent);
                break;

            case R.id.add2:
                String user = etName.getText().toString();
                String password = etPassword.getText().toString();

                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_USER, user);
                contentValues.put(DBHelper.KEY_PASSWORD, password);

                database.insert(DBHelper.TABLE_USERS, null, contentValues);
                UpdateTable();
                break;

            case R.id.clear2:
                database.delete(DBHelper.TABLE_USERS, null, null);
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();

                int id = v.getId();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_USERS, DBHelper.KEY_ID2 + " = ?", new String[]{String.valueOf((v.getId()))});

                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID2);
                    int userIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_USER);
                    int passwordIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PASSWORD);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper.KEY_ID2, realID);
                            contentValues.put(DBHelper.KEY_USER, cursorUpdater.getString(userIndex));
                            contentValues.put(DBHelper.KEY_PASSWORD, cursorUpdater.getString(passwordIndex));
                            database.replace(DBHelper.TABLE_USERS, null, contentValues);
                        }
                        realID++;
                    } while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast() && v.getId() != realID) {
                        database.delete(DBHelper.TABLE_USERS, DBHelper.KEY_ID2 + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}