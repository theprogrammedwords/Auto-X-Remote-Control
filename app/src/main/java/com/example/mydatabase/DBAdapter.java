package com.example.mydatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter extends AppCompatActivity{
    static final String primary_email = "p_email";
    static final String secondary_email = "s_email";
    static final String password = "password";
    static final String DB_name = "MyData";
    static final String DB_Table = "mydb";
    static final int version = 1;
    static final String create = "create table " + DB_Table + " " + "(p_email text primary key not null, " + "s_email text not null,password text not null);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    SharedPreferences sp2;
    SharedPreferences.Editor editor;
    static String login_secondaryEmail,login_primaryEmail,login_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp2=getSharedPreferences("user_details",MODE_PRIVATE);
        editor=sp2.edit();
    }

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {

            super(context, DB_name, null, version);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(create);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_Table);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() throws SQLException {
        db.close();
    }

    public long insertContact(String primary_email, String secondary_email, String password) {
        ContentValues values = new ContentValues();
        values.put(this.primary_email, primary_email);
        values.put(this.secondary_email, secondary_email);
        values.put(this.password, password);

        return db.insert(DB_Table, null, values);

    }

    public boolean getData(String primary_email, String password1) throws SQLiteException {
        db = DBHelper.getReadableDatabase();
        try {
            Cursor mCur = db.query(true, DB_Table, new String[]{this.primary_email, secondary_email, this.password}, this.primary_email + "='" + primary_email + "'", null, null, null, null, null);

            if (mCur != null) {
                mCur.moveToFirst();
                login_password = mCur.getString(2);
                if (password1.equals(login_password)) {
                    login_primaryEmail=mCur.getString(0);
                    login_secondaryEmail = mCur.getString(1);
                    Log.d("Userdetail DBADAPTER",login_primaryEmail+" "+login_secondaryEmail+" "+login_password);

                    return true;
                } else
                    return false;
            }
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }
    public boolean updatePassword(String newPassword){
        ContentValues values=new ContentValues();
        values.put(this.password,newPassword);
        return db.update(DB_Table,values,password+"='"+login_password+"'",null)>0;
    }
}