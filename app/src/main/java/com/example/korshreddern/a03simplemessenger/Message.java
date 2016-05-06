package com.example.korshreddern.a03simplemessenger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Korshreddern on 21-Apr-16.
 */
public class Message {
    public String seqno;
    public String datetime;
    public String from;
    public String to;
    public String message;


    public Message() {
        this.seqno = "";
        this.datetime = "";
        this.from = "";
        this.to = "";
        this.message = "";
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


    public String getSeqNum(Context context) {
        DBHelper dbhelper=new DBHelper(context);
        SQLiteDatabase db=dbhelper.getReadableDatabase();
        String sql="SELECT seqno,from,to,message,datetime FROM message ORDER BY seqno";
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

    public void addMessageToDB(Context context, Message msg) {
        DBHelper dbhelper=new DBHelper(context);
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        String sql="INSERT INTO message (from,to,message,datetime) VALUES(?,?,?,?)";
        db.execSQL(sql, new String[] {
                msg.from,
                msg.to,
                msg.message,
                msg.datetime
        });
        db.close();
    }

    public void getMessage(Context context, Message msg) {
        DBHelper dbhelper=new DBHelper(context);
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        String sql="SELECT INTO message (from,to,message,datetime) VALUES(?,?,?,?)";
        db.execSQL(sql, new String[] {
                msg.from,
                msg.to,
                msg.message,
                msg.datetime
        });
        db.close();
    }

    public void sendMessage(Context context, String session_id) {

    }
}
