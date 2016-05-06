package com.example.korshreddern.a03simplemessenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {
    ListView listView_message;
    EditText chatEditText;
    Button sendBotton;
    String message = "";
    Message messageObj;

    final static String URL_POST_MESSAGE = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/postMessage";
    final static String URL_GET_MESSAGE = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/getMessage";
    String get_message_limit = "500";
    final static String GET_MESSAGE_SEQNO = "1";
    final static String URL_DEL_MESSAGE = "https://mis.cp.eng.chula.ac.th/mobile/service.php?q=api/delMessage";

    private String login_sessionId;
    private String login_usernameStr;
    private String login_typeStr;
    private String login_infoStr;
    private String contact_typeStr;
    private String contact_username_target;
    private String getMessageTypeStr;
    private String getMessageInfo;
    private String postMessageTypeStr;
    private String postMessageInfo;
    private String postMessageContent;
//    private String contact_username_target;
    JSONArray getMessageArr;

    String[] eachrow_from;
    String[] eachrow_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listView_message = (ListView) findViewById(R.id.chat_listView);
        chatEditText = (EditText) findViewById(R.id.chat_editText);

        Bundle bundle = getIntent().getExtras();
        login_usernameStr = bundle.getString("login_username");
        login_typeStr = bundle.getString("login_type");
        login_infoStr = bundle.getString("login_info");
        login_sessionId = bundle.getString("login_session_id");
        contact_typeStr = bundle.getString("contact_typeStr");
        contact_username_target = bundle.getString("contact_username_target");

        new GetMessageTask().execute(URL_GET_MESSAGE);

        setTitle("Chat with " + contact_username_target);
        setupSendButton();
    }

    private class GetMessageTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                HTTPHelper httpHelper = new HTTPHelper();
                String param = "sessionid=" + login_sessionId + "&seqno=" + GET_MESSAGE_SEQNO + "&limit=" + get_message_limit;
                result = httpHelper.POST(URL_GET_MESSAGE, param);
//                Log.d("Result", "Result: " + result);// OK
                JSONObject resultObject = new JSONObject(result);
                getMessageTypeStr = resultObject.getString("type");
                getMessageInfo = resultObject.getString("info");
                getMessageArr = resultObject.getJSONArray("content"); // [{...},{...},...]
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
                int getMessageArrLength = getMessageArr.length(); // [{...},{...},...]
                int countTarget = 0;
                int[] temp_index = new int[getMessageArrLength];
                for (int i = 0; i < getMessageArr.length(); i++) {
                    JSONObject messageJSONObject = getMessageArr.getJSONObject(i);
                    String from = messageJSONObject.getString("from");
                    String to = messageJSONObject.getString("to");
                    if (from.equalsIgnoreCase(contact_username_target) || to.equalsIgnoreCase(contact_username_target)){
                        temp_index[countTarget] = i;
                        countTarget++;
                    }
                }

                eachrow_from = new String[countTarget];
                eachrow_message = new String[countTarget];
                int countNew = 0;
                for (int i = temp_index[0]; i < getMessageArr.length(); i++) {
                    JSONObject messageJSONObject = getMessageArr.getJSONObject(i);
                    String from = messageJSONObject.getString("from");
                    String to = messageJSONObject.getString("to");
                    if (countTarget == countNew) break;
                    if (from.equalsIgnoreCase(contact_username_target) || to.equalsIgnoreCase(contact_username_target)){
                        String message = messageJSONObject.getString("message");
                        eachrow_from[countNew] = from;
                        eachrow_message[countNew] = message;
                        countNew++;
                    }
                }

                ChatMessageListAdapter chatMessageListAdapter = new ChatMessageListAdapter(getApplication(), eachrow_from, eachrow_message);
                listView_message.setAdapter(chatMessageListAdapter);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupSendButton() {
        sendBotton = (Button) findViewById(R.id.chat_send_button);
        sendBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = chatEditText.getText().toString();
                if (!message.equalsIgnoreCase("")){
                    new PostMessageTask().execute(URL_POST_MESSAGE);
                }
            }
        });
    }

    private class PostMessageTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                HTTPHelper httpHelper = new HTTPHelper();
                String param = "sessionid=" + login_sessionId + "&targetname=" + contact_username_target + "&message=" + message;
                result = httpHelper.POST(URL_POST_MESSAGE, param);
                Log.d("Result", "Result: " + result);// OK

                JSONObject resultObject = new JSONObject(result);
                postMessageTypeStr = resultObject.getString("type");
//                postMessageInfo = resultObject.getString("info");
//                postMessageContent = resultObject.getString("content"); // [{...},{...},...]
                if (postMessageTypeStr.equalsIgnoreCase("error")){
                    Toast.makeText(getApplicationContext(), "Cannot Send Message", Toast.LENGTH_SHORT).show();
                }
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
                chatEditText.setText("");
                new GetMessageTask().execute(URL_GET_MESSAGE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, ContactListActivity.class);
        intent.putExtra("Username", login_usernameStr);
        intent.putExtra("type", login_typeStr);
        intent.putExtra("info", login_infoStr);
        intent.putExtra("content", login_sessionId);
        startActivity(intent);
//        Toast.makeText(getApplicationContext(), "onBackPressed", Toast.LENGTH_SHORT).show();
        finish();
    }


}