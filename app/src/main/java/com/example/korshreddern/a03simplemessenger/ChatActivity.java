package com.example.korshreddern.a03simplemessenger;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {
    TextView messageChatHistory;
    EditText chatEditText;
    Button sendBotton;
    String message = "";
    Message messageObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageChatHistory = (TextView) findViewById(R.id.chat_textView);
        chatEditText = (EditText) findViewById(R.id.chat_editText);
        sendBotton = (Button) findViewById(R.id.chat_send_button);

        sendBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HistoryChatStr = messageChatHistory.getText().toString();
                message = chatEditText.getText().toString();
                String strToShow = HistoryChatStr + "\nMe: " + message;

                messageObj = new Message();
                String seq = getSeqNum();
                        //Integer.parseInt(et.getText().toString());
//                messageObj.setSeqno("1");
                messageObj.setFromuser("Me");
                addMessage(messageObj);

                messageChatHistory.setText(strToShow);
                chatEditText.setText("");
            }
        });
    }

    public String getSeqNum() {
        DBHelper dbhelper=new DBHelper(this);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String sql="SELECT seqno,fromuser,touser,message,msgdate FROM message ORDER BY seqno";
        Cursor sqlList = db.rawQuery(sql, null);

        String seqNum = "";

        if (! sqlList.moveToFirst()) {
            return "0";
        }
        do {
            seqNum = sqlList.getString(0);
        } while (sqlList.moveToNext());
        sqlList.close();
        db.close();
        return seqNum;
    }

    public void addMessage(Message msg) {
        DBHelper dbhelper=new DBHelper(this);
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        String sql="INSERT INTO message (fromuser,touser,message,msgdate) VALUES(?,?,?,?)";
        db.execSQL(sql, new String[] {
                msg.fromuser,
                msg.touser,
                msg.message,
                msg.msgdate
        });
        db.close();
    }
}
