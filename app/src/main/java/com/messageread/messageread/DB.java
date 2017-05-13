package com.messageread.messageread;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zeyd on 2.8.2016.
 */
public class DB extends SQLiteOpenHelper {
    final  static String DB_NAME="MESAJ.db";
    Context context;
    public DB(Context context) {
        super(context, DB_NAME, null, 1);
        this.context =context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table msg(id INTEGER primary key,telId Text,mesaj TEXT,gonderen Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists msg");
        onCreate(db);
    }

    public void mesajSil(int id)
    {
        SQLiteDatabase con =this.getWritableDatabase();
        con.execSQL("delete from msg where id="+id);
    }

    public List<Mesaj> getMesajList()
    {
        SQLiteDatabase con =this.getReadableDatabase();
        Cursor c =con.rawQuery("Select * from msg",null);
        List<Mesaj> mesajList =new ArrayList<Mesaj>();
        if(c.moveToFirst())
        {
            do
            {
                Mesaj mesaj =new Mesaj();
                mesaj.setId(c.getInt(c.getColumnIndex("id")));
                mesaj.setMesaj(c.getString(c.getColumnIndex("mesaj")));
                mesaj.setTelId(c.getString(c.getColumnIndex("telId")));
                mesaj.setGonderen(c.getString(c.getColumnIndex("gonderen")));
                mesajList.add(mesaj);
            }while (c.moveToNext());
        }
        return mesajList;
    }
    public  void  setMesaj(String mesaj,String gonderen)
    {
        SQLiteDatabase con  =this.getWritableDatabase();
        String telId =new Kayit(context).getTelId();
        mesaj =temizle(mesaj);
        con.execSQL("insert into msg(telId,mesaj,gonderen) values('"+telId+"','"+mesaj+"','"+gonderen+"')");
    }
    private  String temizle(String txt)
    {
        txt = txt.replace("'", " ");
        return  txt;
    }
}
