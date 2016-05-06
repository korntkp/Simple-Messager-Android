package com.example.korshreddern.a03simplemessenger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    ProgressBar mProgressDialog;
    Button loginButton;

    private String usernameStr;
    private String passwordStr;

    DBHelper db;

    final static String URL_LOGIN = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/signIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText    = (EditText) findViewById(R.id.login_input_username);
        passwordEditText    = (EditText) findViewById(R.id.login_input_password);
        mProgressDialog     = (ProgressBar) findViewById(R.id.login_progress_bar);
        loginButton         = (Button) findViewById(R.id.login_button);

        db = new DBHelper(this);
    }

    public void chkLogin(View view){
        usernameStr = usernameEditText.getText().toString();
        passwordStr = passwordEditText.getText().toString();

        new LoginTask().execute(URL_LOGIN);
        /*
        //  SQLite Database
        String message = "";
        Cursor cs = db.getDataParm(1);
        cs.moveToFirst();
        String id = cs.getString(cs.getColumnIndex(DBHelper.PARM_COLUMN_ID));
        String ver = cs.getString(cs.getColumnIndex(DBHelper.PARM_COLUMN_VER));
        message = "From parm DB -> ID: " + id + " Version: " + ver;
        */

        /*
        Intent objIntent = new Intent(getApplicationContext(), ChatActivity.class);

        objIntent.putExtra("Username", usernameStr);
        objIntent.putExtra("Password", passwordStr);

        startActivity(objIntent);
        finish();
        */
    }

    private class LoginTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }

        protected String doInBackground(String... urls) {
            String result = "";
            try {
                HTTPHelper httpHelper = new HTTPHelper();
                String param = "username=" + usernameStr + "&password=" + passwordStr;
                result = httpHelper.POST(URL_LOGIN, param);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress[0]);
            mProgressDialog.setProgress(progress[0]); // Here eclipse tell that "mProgressDialog cannot be resolved"
        }

        protected void onPostExecute(String result) {
            try {
                if (result == ""){
                    Toast.makeText(getApplicationContext(), "1Username or Password Incorrect -> ", Toast.LENGTH_SHORT).show();
                    mProgressDialog.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                } else if (result == null) {
                    Toast.makeText(getApplicationContext(), "2Username or Password Incorrect -> NULL", Toast.LENGTH_SHORT).show();
                    mProgressDialog.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                } else {
                    JSONObject jObject = new JSONObject(result);
                    String typeStr = jObject.getString("type");
                    String infoStr = jObject.getString("info");
                    String contentStr = jObject.getString("content");

                    if (typeStr.equalsIgnoreCase("error")){
                        Toast.makeText(getApplicationContext(), infoStr + " " + contentStr, Toast.LENGTH_SHORT).show();
                        mProgressDialog.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);

                    } else {
                        // To Contacts List
                        Intent intent = new Intent(LoginActivity.this, ContactListActivity.class);
                        intent.putExtra("Username", usernameStr);
                        intent.putExtra("type", typeStr);
                        intent.putExtra("info", infoStr);
                        intent.putExtra("content", contentStr);
                        mProgressDialog.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Message> loadMessage() {
        ArrayList<Message> list=new ArrayList<Message>();
        DBHelper dbhelper=new DBHelper(this);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String sql="SELECT seqno,from,to,message,datetime FROM message ORDER BY seqno";
        Cursor sqlList = db.rawQuery(sql, null);

        if (! sqlList.moveToFirst()) {
            return list;
        }
        do {
            Message msg = new Message();
            msg.seqno = sqlList.getString(0);
            msg.from =sqlList.getString(1);
            msg.to =sqlList.getString(2);
            msg.message = sqlList.getString(3);
            msg.datetime = sqlList.getString(3);
            list.add(msg);
        }
        while (sqlList.moveToNext());
        sqlList.close();
        db.close();
        return list;
    }
}
