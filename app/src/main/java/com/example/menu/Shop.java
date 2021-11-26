package com.example.menu;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Shop extends AppCompatActivity implements View.OnClickListener {

    Button Checkout;
    EditText etSum;
    float summary = 0;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Checkout = (Button) findViewById(R.id.checkout);
        Checkout.setOnClickListener(this);

        etSum = (EditText) findViewById(R.id.sum);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }


    @SuppressLint("ResourceType")
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_MENU, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutput = findViewById(R.id.output3);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputName = new TextView(this);
                params.weight = 5.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                outputName.setTextColor(Color.argb(255, 67, 45, 19));
                dbOutputRow.addView(outputName);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                outputPrice.setTextColor(Color.argb(255, 67, 45, 19));
                dbOutputRow.addView(outputPrice);

                Button btnAdd = new Button(this);
                btnAdd.setOnClickListener(this);
                params.weight = 1.0f;
                btnAdd.setLayoutParams(params);
                btnAdd.setText("buy");
                btnAdd.setBackgroundResource(R.drawable.round_shape_btn);
                btnAdd.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(btnAdd);

                dbOutput.addView(dbOutputRow);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.checkout:
                Toast toast = Toast.makeText(getApplicationContext(), "Сумма покупки: " + String.valueOf(summary), Toast.LENGTH_LONG);
                toast.show();
                etSum.setText("");
                summary = 0;
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();

                int id = v.getId();
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_MENU, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                    do {
                        if (cursorUpdater.getInt(idIndex) == v.getId()) {
                            summary = summary + Float.valueOf(cursorUpdater.getString(priceIndex));
                            etSum.setText(String.valueOf(summary));
                        }
                    } while (cursorUpdater.moveToNext());
                }
                break;
        }
    }
}