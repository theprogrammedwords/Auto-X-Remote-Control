package com.example.mydatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changepass extends AppCompatActivity {
    EditText oldPassword,newPassword;
    DBAdapter db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepass);
        oldPassword = findViewById(R.id.old);
        newPassword = findViewById(R.id.newPass);
        Button btn=findViewById(R.id.submit);
        db=new DBAdapter(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                boolean check=db.updatePassword(newPassword.getText().toString());
                if(check){
                    Toast.makeText(getApplicationContext(),"updated successfully",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"sorry !",Toast.LENGTH_SHORT).show();
                db.close();
            }
        });
    }
}
