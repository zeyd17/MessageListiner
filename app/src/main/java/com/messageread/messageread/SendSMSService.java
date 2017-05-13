package com.messageread.messageread;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Zeyd on 2.8.2016.
 */
public class SendSMSService extends IntentService {

    DB db;
    public SendSMSService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //mesajı gönder
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}
        if(NetworkReceiver.isOnline(getApplication()))
        {
            //network erişimi var.
           // Toast.makeText(getApplication(),"Net Erişimi var",Toast.LENGTH_LONG).show();
             db =new DB(getApplication());
            List<Mesaj> mesajList =db.getMesajList();
            for (int i=0;i<mesajList.size();i++)
            {
                mesajGonder(mesajList.get(i).getId(),mesajList.get(i).getTelId(),mesajList.get(i).getMesaj(),mesajList.get(i).getGonderen());
            }
        }

    }

    private void mesajGonder(final int id, final String telId, final String mesaj, final String gonderen){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String sonuc;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://website/mesajKaydet.php");
                try {
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                    nameValuePair.add(new BasicNameValuePair("id", Integer.toString(id)));
                    nameValuePair.add(new BasicNameValuePair("tel", telId));
                    nameValuePair.add(new BasicNameValuePair("mesaj", mesaj));
                    nameValuePair.add(new BasicNameValuePair("gonderen", gonderen));

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity httpEntity = response.getEntity();
                    sonuc = EntityUtils.toString(httpEntity);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("Q", "veri geldi" + sonuc);
                                JSONObject jsonObject = new JSONObject(sonuc);
                                Boolean durum =jsonObject.getBoolean("durum");
                                if (jsonObject != null && durum) {
                                    int id = jsonObject.getInt("id");
                                    db.mesajSil(id);
                                }

                             //   Toast.makeText(getApplication(),"sonuc geldi"+sonuc,Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                            }
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
            }
        }).start();
    }
}
