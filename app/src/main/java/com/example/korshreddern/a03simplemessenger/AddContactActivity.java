package com.example.korshreddern.a03simplemessenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddContactActivity extends AppCompatActivity {

    final static String URL_SEARCH_CONTACT = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/searchUser";
    final static String URL_ADD_CONTACT = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/addContact";

    private String login_sessionId;
    private String login_usernameStr;
    private String login_typeStr;
    private String login_infoStr;


    EditText addContactEditText;
    Button addContactButton;
    String addContactStr;
    String addContactTypeStr;
    String addContactInfo;
    String addContactContent;

    EditText searchContactEditText;
    Button searchContactButton;
    String searchContactStr;
    String searchContactTypeStr;
    String searchContactInfo;
    JSONArray searchContactContent;
    String[] searchedContacts;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        addContactEditText = (EditText) findViewById(R.id.add_contact_editText);
        searchContactEditText = (EditText) findViewById(R.id.search_contact_editText);
        listView = (ListView)findViewById(R.id.listView_search_contact);
        setupAddButton();
        setupSearchButton();

        Bundle bundle = getIntent().getExtras();
        login_usernameStr = bundle.getString("login_username");
        login_typeStr = bundle.getString("login_type");
        login_infoStr = bundle.getString("login_info");
        login_sessionId = bundle.getString("login_session_id");
        Log.d("seesion_id", login_sessionId);
    }

    private void setupAddButton() {
        addContactButton = (Button) findViewById(R.id.add_contact_button);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactStr = addContactEditText.getText().toString();
                if (!addContactStr.equalsIgnoreCase("")){
                    new addContactTask().execute(URL_ADD_CONTACT);
                }
            }
        });
    }

    private void setupSearchButton() {
        searchContactButton = (Button) findViewById(R.id.search_contact_button);
        searchContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContactStr = searchContactEditText.getText().toString();
                if (!searchContactStr.equalsIgnoreCase("")){
                    new searchContactTask().execute(URL_SEARCH_CONTACT);
                }
            }
        });
    }

    private class addContactTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                HTTPHelper httpHelper = new HTTPHelper();
                String param = "sessionid=" + login_sessionId + "&username=" + addContactStr;
                result = httpHelper.POST(URL_ADD_CONTACT, param);
                JSONObject resultObject = new JSONObject(result);
                Log.d("Result", "Result: " + resultObject);// OK


                addContactTypeStr = resultObject.getString("type");
                addContactInfo = resultObject.getString("info");
                addContactContent = resultObject.getString("content"); // [{...},{...},...]
//                if (addContactTypeStr.equalsIgnoreCase("error")){
//                    Toast.makeText(getApplicationContext(), "Cannot Add Contact", Toast.LENGTH_SHORT).show();
//                }
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
                if (addContactContent.equalsIgnoreCase("false")){
                    Toast.makeText(getApplicationContext(), "Cannot Add Contact", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Add Contact Complete", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddContactActivity.this, ContactListActivity.class);
                    intent.putExtra("Username", login_usernameStr);
                    intent.putExtra("type", login_typeStr);
                    intent.putExtra("info", login_infoStr);
                    intent.putExtra("content", login_sessionId);
                    startActivity(intent);
                    finish();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class searchContactTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                HTTPHelper httpHelper = new HTTPHelper();
                String param = "sessionid=" + login_sessionId + "&keyword=" + searchContactStr;
                result = httpHelper.POST(URL_SEARCH_CONTACT, param);
                JSONObject resultObject = new JSONObject(result);
                Log.d("Result", "Result: " + resultObject);// OK


                searchContactTypeStr = resultObject.getString("type");
                if (searchContactTypeStr.equalsIgnoreCase("userList")){
                    searchContactInfo = resultObject.getString("info");
                    searchContactContent = resultObject.getJSONArray("content"); // [{...},{...},...]
                }

//                if (addContactTypeStr.equalsIgnoreCase("error")){
//                    Toast.makeText(getApplicationContext(), "Cannot Add Contact", Toast.LENGTH_SHORT).show();
//                }
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
                int contactSearchListLength = searchContactContent.length();
                searchedContacts = new String[contactSearchListLength];
                for (int i = 0; i < contactSearchListLength; i++) {
                    searchedContacts[i] = searchContactContent.getString(i);
                }
                if (contactSearchListLength > 0) Log.d("Seaech", searchedContacts[0]); // OK
                setupListView(searchedContacts);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupListView(final String [] contacts) {
        SearchContactListAdapter adapter = new SearchContactListAdapter(getApplication(), contacts);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                addContactEditText.setText(contacts[arg2]);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddContactActivity.this, ContactListActivity.class);
        intent.putExtra("Username", login_usernameStr);
        intent.putExtra("type", login_typeStr);
        intent.putExtra("info", login_infoStr);
        intent.putExtra("content", login_sessionId);
        startActivity(intent);
        finish();
    }
}
