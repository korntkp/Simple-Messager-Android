package com.example.korshreddern.a03simplemessenger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Korshreddern on 21-Apr-16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "NoteDB.sqlite";
    public static final int DB_VER = 1;
    public static final String PARM_COLUMN_ID = "id";
    public static final String PARM_COLUMN_VER = "ver";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    /*
    *       [2 TABLE]
    *
    * CREATE TABLE: parm
    *   id INTEGER (PRIMARY KEY)
    *   ver TEXT
    *
    * INSERT INTO: parm
    *   1
    *   '20140701'
    *
    * CREATE TABLE: message
    *   seqno INTEGER (PRIMARY KEY)
    *   from text
    *   to text
    *   message text
    *   datetime text
    * */
    @Override
    public void onCreate(SQLiteDatabase database) {
        String sql;
//        sql = "CREATE TABLE parm (id INTEGER PRIMARY KEY, ver TEXT);";
//        database.execSQL(sql);
//        sql = "INSERT INTO `parm` VALUES(1,'20140701');";
//        database.execSQL(sql);
        sql = "CREATE TABLE message(seqno INTEGER PRIMARY KEY AUTOINCREMENT ,from text, to text,message text,datetime text);";
        database.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldver, int newver) {
//        database.execSQL("DROP TABLE IF EXISTS parm");
        database.execSQL("DROP TABLE IF EXISTS message");
        onCreate(database);
    }

    public Cursor getDataParm(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from parm where id="+ id +"", null );
        return res;
    }


}
