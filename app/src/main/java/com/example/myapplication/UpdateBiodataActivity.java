package com.example.myapplication;

import android.app.DatePickerDialog;
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

public class UpdateBiodataActivity extends AppCompatActivity {
protected Cursor cursor;
DbConfig dbHelper;
Button ton1, ton2;
EditText text1, text2, text3, text4, text5;

    private static final String TAG = "UpdateBiodataActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_biodata);



        dbHelper = new DbConfig(this);
        text1 = (EditText) findViewById(R.id.editText1);
        text2 = (EditText) findViewById(R.id.editText2);
        text3 = (EditText) findViewById(R.id.editText3);
        text4 = (EditText) findViewById(R.id.editText4);
        text5 = (EditText) findViewById(R.id.editText5);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM biodata WHERE nama = '" +
                getIntent().getStringExtra("nama") + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(0).toString());
            text2.setText(cursor.getString(1).toString());
            text3.setText(cursor.getString(2).toString());
            text4.setText(cursor.getString(3).toString());
            text5.setText(cursor.getString(4).toString());
        }
        ton1 = (Button) findViewById(R.id.button1);
        ton2 = (Button) findViewById(R.id.button2);

        //daftarkan even onClick pada btnsimpan
        ton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (validate()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("update biodata set nama='" +
                        text2.getText().toString() + "', tgl='" +
                        text3.getText().toString() + "', jk='" +
                        text4.getText().toString() + "', alamat='" +
                        text5.getText().toString() + "' where no='" +
                        text1.getText().toString() + "'");
                Toast.makeText(getApplicationContext(),"Berhasil", Toast.LENGTH_LONG).show();
                MainActivity.ma.RefreshList();
                finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Maaf, coba lagi",
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

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateBiodataActivity.this,
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
            text1.setError("Nomor harus diisi dan tidak boleh sama!");
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
            text5.setError("Alamat harus diisi!");
        }else {
            valid = true;
            text5.setError(null);
        }

        return valid;
    }
}
