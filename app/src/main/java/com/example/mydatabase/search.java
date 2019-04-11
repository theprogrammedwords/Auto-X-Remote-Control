package com.example.mydatabase;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class search extends AppCompatActivity {
    EditText editText;
    GMailSender sender;
    Handler handler = new Handler();
    ProgressDialog pd;
    boolean check=true;
    String search_query="";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String logged_pemail,logged_semail,logged_password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        sp=getSharedPreferences("check1",MODE_PRIVATE);
        editor=sp.edit();

        logged_pemail=sp.getString("logged_pmail","abc");
        logged_semail=sp.getString("logged_smail","abc");
        logged_password=sp.getString("logged_password","abc");

        sender= new GMailSender(logged_pemail,logged_password);



        Log.d("Username and password: ",logged_pemail+ " " + logged_semail);
        editText=findViewById(R.id.search);
        Button btn=findViewById(R.id.searchbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_query=editText.getText().toString();
                AlertDialog.Builder dialog=new AlertDialog.Builder(search.this);
                dialog.setTitle("Confirmation").setMessage("Confirm request ?");
                dialog.setPositiveButton("Confirm",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Body: ",search_query);
                        Toast.makeText(getApplicationContext(),search_query,Toast.LENGTH_SHORT).show();
                    sendCommand(search_query,"AAS_3");
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
                    while (check)
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }).start();
    }
    void sendCommand(final String ptext, final String subject)
    {
        Log.d("Body: ",ptext);
        Toast.makeText(getApplicationContext(),ptext,Toast.LENGTH_SHORT).show();
        progressD();
        Log.d("Query : ",ptext);
        new Thread(new Runnable() {

            public void run() {
                try {
                    //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");
                    sender.sendMail(subject, ptext

                            ,logged_pemail

                            ,logged_semail);

                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            check=false;
                            Toast.makeText(getApplicationContext(), "command sent successfully !", Toast.LENGTH_LONG).show();
                        }
                    });


                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }

}

