package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText nama, password, confirmpassword; //Declaration EditTexts
    Button buttonRegister; //Declaration Button
    Cursor cursor; //Declaration Cursor
    DataHelper dataHelper; //Declaration SqliteHelper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dataHelper = new DataHelper(this);

        nama = (EditText) findViewById(R.id.nama);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        TextView txtBacktoLogin = (TextView) findViewById(R.id.txtBacktoLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String UserName = nama.getText().toString();
                    String Password = password.getText().toString();

                    // Query check email
                    SQLiteDatabase db = dataHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT id FROM users WHERE nama = '" + UserName + "'",null);
                    cursor.moveToFirst();
                    if (cursor.getCount()>0) {
                        //Email exists with email input provided so show error user already exist
                        Toast.makeText(getApplicationContext(), "Nama sudah ada",
                                Toast.LENGTH_LONG).show();
                    }else{

                        SQLiteDatabase query = dataHelper.getWritableDatabase();
                        query.execSQL("insert into users(nama, password) values('" +
                                UserName + "','" +
                                Password + "')");
                        Toast.makeText(getApplicationContext(), "Nama berhasil dibuat! Silahkan Masuk",
                                Toast.LENGTH_LONG).show();

                        //User Logged in Successfully Launch You home screen activity
                        Intent intent=new Intent(getApplicationContext(),SplashScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        txtBacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String UserName = nama.getText().toString();
        String Password = password.getText().toString();
        String ConfirmPassword = confirmpassword.getText().toString();

        //Handling validation for UserName field
        if (UserName.isEmpty()) {
            valid = false;
            nama.setError("Nama harus diisi!");
        } else {
            valid = true;
            nama.setError(null);
        }


        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            password.setError("Password harus diisi!");
        } else if (Password.length() < 4 ) {
            valid = false;
            password.setError("Password Terlalu pendek, minimal 5 karakter!");
        } else {
            valid = true;
            password.setError(null);

        }
        //Handling validation for Confirm Password field
        if (ConfirmPassword.isEmpty() || !ConfirmPassword.equals(Password)) {
            valid = false;
            confirmpassword.setError("Konfirmasi Password harus diisi!");
        } else {
            valid = true;
            confirmpassword.setError(null);
        }

        return valid;
    }
}
