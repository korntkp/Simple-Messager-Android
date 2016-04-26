package com.example.korshreddern.a03simplemessenger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);

    }

    public void chkLogin(View view){
        String message = "";

        Cursor cs = db.getDataParm(1);
        cs.moveToFirst();

        String id = cs.getString(cs.getColumnIndex(DBHelper.PARM_COLUMN_ID));
        String ver = cs.getString(cs.getColumnIndex(DBHelper.PARM_COLUMN_VER));

        message = "From parm DB -> ID: " + id + " Version: " + ver;

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent objIntent = new Intent(getApplicationContext(), ChatActivity.class);

        EditText usernameEditText = (EditText) findViewById(R.id.login_input_username);
        EditText passwordEditText = (EditText) findViewById(R.id.login_input_password);

        String username = usernameEditText.getText().toString();
        objIntent.putExtra("Username", username);

        startActivity(objIntent);
        finish();

        /*
        // To Contacts List

        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();

        Intent objIntent = new Intent(getApplicationContext(), ContactListActivity.class);

        EditText usernameEditText = (EditText) findViewById(R.id.login_input_username);
        EditText passwordEditText = (EditText) findViewById(R.id.login_input_password);

        String username = usernameEditText.getText().toString();
        objIntent.putExtra("Username", username);

        startActivity(objIntent);
        finish();
*/
    }

    public ArrayList<Message> loadMessage() {
        ArrayList<Message> list=new ArrayList<Message>();
        DBHelper dbhelper=new DBHelper(this);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String sql="SELECT seqno,fromuser,touser,message,msgdate FROM message ORDER BY seqno";
        Cursor sqlList = db.rawQuery(sql, null);

        if (! sqlList.moveToFirst()) {
            return list;
        }
        do {
            Message msg = new Message();
            msg.seqno = sqlList.getString(0);
            msg.fromuser=sqlList.getString(1);
            msg.touser=sqlList.getString(2);
            msg.message = sqlList.getString(3);
            msg.msgdate = sqlList.getString(3);
            list.add(msg);
        }
        while (sqlList.moveToNext());
        sqlList.close();
        db.close();
        return list;
    }


}
