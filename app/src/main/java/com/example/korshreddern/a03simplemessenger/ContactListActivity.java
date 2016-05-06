package com.example.korshreddern.a03simplemessenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactListActivity extends AppCompatActivity {

    final String URL_GET_CONTACT = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/getContact";
    final String URL_ADD_CONTACT = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/addContact";
    final String URL_SEARCH_CONTACT = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/searchUser";

    private String login_sessionId;
    private String login_usernameStr;
    private String login_typeStr;
    private String login_infoStr;
    private String contact_typeStr;

    String [] contacts;
    int resId;

    ListView listView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Bundle bundle = getIntent().getExtras();
        login_usernameStr = bundle.getString("Username");
        login_typeStr = bundle.getString("type");
        login_infoStr = bundle.getString("info");
        login_sessionId = bundle.getString("content");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
                intent.putExtra("login_username", login_usernameStr);
                intent.putExtra("login_type", login_typeStr);
                intent.putExtra("login_info", login_infoStr);
                intent.putExtra("login_session_id", login_sessionId);
                startActivity(intent);
                finish();
            }
        });


        resId = R.mipmap.ic_launcher;

        listView = (ListView)findViewById(R.id.listView1);

        new GetContactTask().execute(URL_GET_CONTACT);
    }

    private class GetContactTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                HTTPHelper httpHelper = new HTTPHelper();
                String param = "sessionid=" + login_sessionId;
                result = httpHelper.POST(URL_GET_CONTACT, param);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress[0]);
        }

        protected void onPostExecute(String result) {
            try {
                JSONArray contact_contactArr;
                int contactArrLength; // ["","5530334421"]

                JSONObject jObject = new JSONObject(result);
                Log.d("JSON Obj", "JSONObject: " + jObject);

                contact_typeStr = jObject.getString("type");
                contact_contactArr = jObject.getJSONArray("content");    // ["","5530334421"]

                int index_null = -1;
                for (int i = 0; i < contact_contactArr.length(); i++) {
                    if(contact_contactArr.getString(i).equalsIgnoreCase("")) {
                        index_null = i;
                    }
                }
                contact_contactArr.remove(index_null);

                // Create Contact Array
                contactArrLength = contact_contactArr.length();
                contacts = new String[contactArrLength]; // 1
                for (int i = 0; i < contact_contactArr.length(); i++) { // 2
                    contacts[i] = contact_contactArr.getString(i);
                }

                setupListView(contacts);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupListView(final String [] contacts) {
        ContactListAdapter adapter = new ContactListAdapter(getApplication(), contacts, resId);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

//                Toast.makeText(getApplicationContext(), "Index: " + arg2 + " -> " + contacts[arg2] , Toast.LENGTH_SHORT).show();
                Log.d("NO.", "No.: " + arg2);

                Intent intent = new Intent(ContactListActivity.this, ChatActivity.class);
                intent.putExtra("login_username", login_usernameStr);
                intent.putExtra("login_type", login_typeStr);
                intent.putExtra("login_info", login_infoStr);
                intent.putExtra("login_session_id", login_sessionId);
                intent.putExtra("contact_typeStr", contact_typeStr);
                intent.putExtra("contact_username_target", contacts[arg2]);
                startActivity(intent);
                finish();
            }
        });
    }
}
