package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class BuatBiodataActivity extends AppCompatActivity {

    Cursor cursor; //Declaration Cursor
    SharedPreferences pref,sharedpreferences;    //Declaration SharedPreferences
    DbConfig dbHelper;
    Button ton1, ton2;
    EditText text1, text2, text3, text4, text5;

    private static final String TAG = "BuatBiodataActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_biodata);

        dbHelper = new DbConfig(this);
        text1 = (EditText) findViewById(R.id.editText1);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        text4 = (EditText) findViewById(R.id.editText4);
        text5 = (EditText) findViewById(R.id.editText5);
        ton1 = (Button) findViewById(R.id.button1);
        ton2 = (Button) findViewById(R.id.button2);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT no from biodata'" +
                getIntent().getStringExtra("Nomor")+ "'", null);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            cursor.moveToLast();
            Integer nomor =cursor.getInt(0)+1;
            text1.setText(nomor.toString());
        }

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BuatBiodataActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                text3.setText(date);
            }
        };

        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (validate()) {
                    String Text1 = text1.getText().toString();

                    // Query check email
                    SQLiteDatabase data = dbHelper.getReadableDatabase();
                    cursor = data.rawQuery("SELECT no FROM biodata WHERE no = '" + Text1 + "'",null);
                    cursor.moveToFirst();
                    if (cursor.getCount()>0) {
                        //Email exists with email input provided so show error user already exist
                        Toast.makeText(getApplicationContext(), "Nomor sudah ada!",
                                Toast.LENGTH_LONG).show();
                    }else{
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("insert into biodata(no, nama, tgl, jk, alamat) values('" +
                            text1.getText().toString() + "','" +
                            text2.getText().toString() + "','" +
                            text3.getText().toString() + "','" +
                            text4.getText().toString() + "','" +
                            text5.getText().toString() + "')");
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                    MainActivity.ma.RefreshList();
                    finish();}
                }else{
                    Toast.makeText(getApplicationContext(), "Data tidak boleh kosong!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        ton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Text1 = text1.getText().toString();
        String Text2 = text2.getText().toString();
        String Text3 = text3.getText().toString();
        String Text4 = text4.getText().toString();
        String Text5 = text5.getText().toString();

        //Handling validation for Text1 field
        if(Text1.isEmpty()) {
            valid = false;
            text1.setError("Nomor harus diisi!");
        }else {
            valid = true;
            text1.setError(null);
        }

        //Handling validation for Text2 field
        if(Text2.isEmpty()) {
            valid = false;
            text2.setError("Nama harus diisi!");
        }else {
            valid = true;
            text2.setError(null);
        }
        //Handling validation for Text3 field
        if(Text3.isEmpty()) {
            valid = false;
            text3.setError("Tanggal harus diisi!");
        }else {
            valid = true;
            text3.setError(null);
        }
        //Handling validation for Text4 field
        if(Text4.isEmpty()) {
            valid = false;
            text4.setError("Jenis Kelamin harus diisi!");
        }else {
            valid = true;
            text4.setError(null);
        }
        //Handling validation for Text5 field
        if(Text5.isEmpty()) {
            valid = false;
            text5.setError("Alamat  harus diisi!");
        }else {
            valid = true;
            text5.setError(null);
        }

        return valid;
    }
}
