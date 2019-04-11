package com.example.mydatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    boolean flag=false;
    static int progress=0;
    ProgressBar progressBar;
    ProgressDialog pd;
    EditText et,et1;
    Button signIn;

    DBAdapter db;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et=findViewById(R.id.editText4);
        et1=findViewById(R.id.editText5);
        db=new DBAdapter(this);
        signIn=findViewById(R.id.btn);
        Button signUp = findViewById(R.id.signup);

        pd= new ProgressDialog(MainActivity.this);
        pd.setTitle("Processing...");
        pd.setMessage("Please Wait...");
        pd.setMax(2);
        pd.setCancelable(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        sp=getSharedPreferences("check1",MODE_PRIVATE);
        editor=sp.edit();

        boolean isFirstTime=sp.getBoolean("isFirstTime",false);
        if(isFirstTime) {
            Intent i = new Intent(MainActivity.this, home.class);
            startActivity(i);
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            for(int i=0;i<2;i++) {
                                pd.incrementProgressBy(1);
                                Thread.sleep(1000);
                            }

                            String mail=String.valueOf(et.getText());
                            String pass= String.valueOf(et1.getText());
                            db.open();

                            boolean v=db.getData(mail,pass);
                            if(!v) {
                                pd.dismiss();
                                handler.post(new Runnable()
                                {
                                    public void run()
                                    {
                                        Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                            else {
                                //Signing in//
                                pd.dismiss();
                                handler.post(new Runnable()
                                {
                                    public void run()
                                    {
                                        Toast.makeText(getApplicationContext(), "Welcome to AUTO-X !", Toast.LENGTH_LONG).show();
                                    }
                                });
                                db.close();
                                editor.putBoolean("isFirstTime", true);

                                editor.putString("logged_pmail",DBAdapter.login_primaryEmail);
                                editor.putString("logged_smail",DBAdapter.login_secondaryEmail);
                                editor.putString("logged_password",DBAdapter.login_password);

                                editor.commit();

                                Log.d("user details:",DBAdapter.login_primaryEmail+" "+DBAdapter.login_secondaryEmail+" "+DBAdapter.login_password);
                                Intent i = new Intent(MainActivity.this, home.class);
                                startActivity(i);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            pd.dismiss();
                        }
                    }
                }).start();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent si;
                si = new Intent(getApplicationContext(),signup.class);
                startActivity(si);
            }
        });
    }}

