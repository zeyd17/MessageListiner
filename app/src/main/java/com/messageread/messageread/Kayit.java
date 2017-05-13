package com.messageread.messageread;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

/**
 * Created by Zeyd on 2.8.2016.
 */
public class Kayit {
    private final String TELIDKEY="MSG";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public  Kayit(Context context)
    {
        this.context=context;
        sharedPreferences =context.getSharedPreferences("msg",Context.MODE_APPEND);
        editor =sharedPreferences.edit();
    }

    public String getTelId(){
        String telID=sharedPreferences.getString(TELIDKEY,"");
        if(telID ==""){
            telID =randomKey();
            setKeyId(telID);
        }
        return telID;
    }

    private void setKeyId(String telId)
    {
        editor.putString(TELIDKEY,telId);
        editor.commit();
    }

    private  String randomKey()
    {
        Random rnd =new Random();
        char [] keyArray =  {'A','B','C','D','E','F','G','H',
                             'I','J','K','L','M','N','O','P',
                             'R','S','T','U','V','Y','W','X',
                             'Y','Z','0','1','2','3','4','5',
                             '6','7','8','9',};
        String telKey="";
        for (int i=0;i<8;i++)
        {
            int indis =rnd.nextInt(keyArray.length);
            telKey +=keyArray[indis];
        }
        return  telKey;
    }


}
