package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    Cursor cursor; //Declaration Cursor
    EditText nama, password; //Declaration Edit
    Button login; //Declaration Button
    SharedPreferences pref,sharedpreferences;    //Declaration SharedPreferences
    DataHelper dataHelper; //Declaration SqliteHelper
    TextView txtRegister;

    ImageView bgapp;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dataHelper = new DataHelper(this);

        nama = (EditText) findViewById(R.id.nama);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //set click event of login button

                //Check user input is correct or not
                if (validate()) {
                    //Get values from EditText fields
                    String Username = nama.getText().toString();
                    String Password = password.getText().toString();
                    // Query check email dan password
                    SQLiteDatabase db = dataHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT id FROM users WHERE nama = '" + Username + "' AND password ='"+Password+"'",null);
                    cursor.moveToFirst();
                    if (cursor.getCount()>0) {

                        sharedpreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("session", cursor.getString(0).toString());
                        editor.commit();

                        Toast.makeText(getApplicationContext(), "Anda Berhasil Masuk",
                                Toast.LENGTH_LONG).show();
                        //User Logged in Successfully Launch You home screen activity
                        Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Maaf Anda Gagal Masuk , Silahkan Dicoba Kembali",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        pref = getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        String session_id =  pref.getString("session",null);
        if(session_id!=null){
            //User Logged in Successfully Launch You home screen activity
            Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);


        bgapp = (ImageView) findViewById(R.id.bgapp);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);

        bgapp.animate().translationY(-1900).setDuration(800).setStartDelay(300);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);

        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);
    }
    //This method is used to validate input given by user
    public boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Username = nama.getText().toString();
        String Password = password.getText().toString();

        //Handling validation for Password field
        if(Username.isEmpty()) {
            valid = false;
            nama.setError("Nama harus diisi!");
        }else {
            valid = true;
            nama.setError(null);
        }

        //Handling validation for Password field
        if (Password.isEmpty()) {
            valid = false;
            password.setError("Password harus diisi!");
        } else if (Password.length() < 4) {
            valid = false;
            password.setError("Minimal Password 4 Karakter!");
        } else {
            valid = true;
        }
        return valid;
    }
    public void onBackPressed() {
        AlertDialog.Builder tombolkeluar = new AlertDialog.Builder(SplashScreenActivity.this);
        tombolkeluar.setMessage("Apakah Anda Yakin Ingin Keluar Dari Aplikasi ini ?");
        tombolkeluar.setTitle("Keluar Aplikasi");
        tombolkeluar.setIcon(R.drawable.alert);
        tombolkeluar.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SplashScreenActivity.this.finish();
            }
        });
        tombolkeluar.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        tombolkeluar.setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        tombolkeluar.show();
    }
    public void menu(View view) {
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
    }
}
