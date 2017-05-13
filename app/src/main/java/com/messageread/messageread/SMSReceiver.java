package com.messageread.messageread;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Zeyd on 2.8.2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
       // Object[] pdus = (Object[]) pudsBundle.get("pdus");
        //SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
        DB db =new DB(context);

        String mesaj="";

        SmsMessage [] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage msg:messages) {
            String gonderen =msg.getDisplayOriginatingAddress();
           String content=msg.getDisplayMessageBody();

            db.setMesaj(content,gonderen);
        }
        //Toast.makeText(context,"Mesaj content"+mesaj,Toast.LENGTH_LONG).show();
        Intent service= new Intent(context,SendSMSService.class);
        context.startService(service);
    }
}
