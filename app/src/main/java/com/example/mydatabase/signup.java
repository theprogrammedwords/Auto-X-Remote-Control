package com.example.mydatabase;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class signup extends AppCompatActivity {
    //SharedPreferences sp;
    DBAdapter db;
    EditText mail1, mail2, pass, pass2;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        db=new DBAdapter(this);
        mail1 = findViewById(R.id.mail1);
        mail2 = findViewById(R.id.mail2);
        pass = findViewById(R.id.password);
        pass2 = findViewById(R.id.password2);
    }

    public void signUp(View view) {
        db=db.open();
        pd=new ProgressDialog(signup.this);
        pd.setCancelable(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Processing...");
        pd.setMessage("Please Wait...");
        pd.setMax(3);

        pd.show();
        long id=db.insertContact(mail1.getText().toString(), mail2.getText().toString(), pass.getText().toString());
        if(id==-1) {
            Toast.makeText(getApplicationContext(), "Account Already Exists !", Toast.LENGTH_SHORT).show();
        }
        else
        {

            Toast.makeText(getApplicationContext(), "Account created successfully !", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("check1", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        db.close();


    }

}