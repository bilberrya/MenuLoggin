package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button loginBtn, signinBtn;
    EditText usernameField, passwordField;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        signinBtn = (Button) findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                Cursor logCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean logged = false;
                if (logCursor.moveToFirst()) {
                    int idIndex = logCursor.getColumnIndex(DBHelper.KEY_ID2);
                    int userIndex = logCursor.getColumnIndex(DBHelper.KEY_USER);
                    int passwordIndex = logCursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do {
                        if((usernameField.getText().toString().equals("Admin")) && passwordField.getText().toString().equals("Admin")){
                            startActivity(new Intent(this, BaseName.class));
                            logged = true;
                        }
                        else if ((usernameField.getText().toString().equals(logCursor.getString(userIndex))) && (passwordField.getText().toString().equals(logCursor.getString(passwordIndex)))) {
                            startActivity(new Intent(this, Shop.class));
                            logged = true;
                        }
                    } while (logCursor.moveToNext());
                }
                logCursor.close();
                if (!logged)
                    Toast.makeText(this, "Введённая комбинация логина и пароля не была найдена.", Toast.LENGTH_LONG).show();
                break;

            case R.id.signinBtn:
                Cursor signCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean finded = false;
                if (signCursor.moveToFirst()) {
                    int userIndex = signCursor.getColumnIndex(DBHelper.KEY_USER);
                    do{
                        if (usernameField.getText().toString().equals(signCursor.getString(userIndex))){
                            Toast.makeText(this, "Введённый вами логин уже зарегистрирован.", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    }while(signCursor.moveToNext());
                }
                if(!finded){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_USER, usernameField.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, passwordField.getText().toString());
                    database.insert(DBHelper.TABLE_USERS, null, contentValues);
                    Toast.makeText(this, "Вы успешно зарегистрированы.", Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;
        }
    }
}