package com.example.mydatabase;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mydatabase.R;


public class home extends AppCompatActivity
{
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Menu menu;
    Button shut,sleep,query,url,res,lock;
    ProgressDialog pd;
    boolean[] checkConfirm=new boolean[4];
    GMailSender sender;
    Handler handler = new Handler();
    String pmail,pass,smail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Intent in = getIntent();

        sp=getSharedPreferences("check1",MODE_PRIVATE);
        editor=sp.edit();
        pmail=sp.getString("logged_pmail",null);
        pass=sp.getString("logged_password",null);
        smail=sp.getString("logged_smail",null);
        sender =new GMailSender(pmail, pass);

        for(int i=0;i<4;i++)
            checkConfirm[i]=false;
        shut=findViewById(R.id.shut);
        sleep=findViewById(R.id.sleep);
        query=findViewById(R.id.Query);
        url=findViewById(R.id.url);
        res=findViewById(R.id.restart);
        lock=findViewById(R.id.lock);
    url.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
            Intent url=new Intent(getApplicationContext(),url.class);
            startActivity(url);
        }
    });
    query.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent sea=new Intent(getApplicationContext(),search.class);
            startActivity(sea);
        }
    });

    shut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogGenerator(0);
        }
    });
    res.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogGenerator(1);
        }
    });
    sleep.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogGenerator(2);
        }
    });
    lock.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogGenerator(3);
        }
    });

    }
    public void progressD(){
        pd=new ProgressDialog(this);
        pd.setTitle("Sending Command...");
        pd.setMessage("Please wait...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(true);
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i=0;i<3;i++)
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }).start();
    }
    void create(Menu menu) {

        MenuItem mn2 = menu.add(0, 1, 1, "Check log files");
        MenuItem mn3=menu.add(0,2,2,"Change Mail");
        MenuItem mn4=menu.add(0,3,3,"Change Password");
        MenuItem mn1 = menu.add(0, 4, 4, "Logout");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        create(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        MenuChoice(item);
        return true;
    }

    public boolean MenuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 4:
                SharedPreferences sp = getSharedPreferences("check1", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(home.this, MainActivity.class);
                startActivity(i);
                return true;

            case 1:
                Intent logs= new Intent(getApplicationContext(),logs.class);
                startActivity(logs);
                return true;
            case 2 :
                Intent cm=new Intent(getApplicationContext(),changemail.class);
                startActivity(cm);
                return true;
            case 3 :
                Intent cp=new Intent(getApplicationContext(),changepass.class);
                startActivity(cp);
                return true;
            default:
                return false;
        }

    }
    public void dialogGenerator(final int id){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Confirmation").setMessage("Confirm command ?");
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkConfirm[id]=true;
                if(checkConfirm[0]==true) {
                    progressD();
                    sendCommand("shutdown","AAS_1");
                }else if(checkConfirm[1]==true){
                    progressD();
                    sendCommand("restart","AAS_1");
                }else if(checkConfirm[2]==true){
                    progressD();
                    sendCommand("sleep","AAS_1");
                }else if(checkConfirm[3]==true){
                    progressD();
                    sendCommand("lock","AAS_1");
                }
            }
        });
        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Cancelling your request...",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.create().show();
    }

    void sendCommand(String ptext, final String subject)
    {

            Encryption e=new Encryption();
            final String encryptedText=e.encrypt(ptext);
        Log.d("Encrypted : ",encryptedText);
            new Thread(new Runnable() {

                public void run() {
                    try {

                        //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");

                        sender.sendMail(subject, encryptedText,

                                pmail,

                                smail);

                        handler.post(new Runnable()
                        {
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), "command sent successfully !", Toast.LENGTH_LONG).show();

                            }
                        });


                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }).start();
    }

        //}
        //else{
          //  Toast.makeText(getApplicationContext(),"Operation Cancelled",Toast.LENGTH_SHORT).show();

        }


    //checkConfirm[id]=false;

