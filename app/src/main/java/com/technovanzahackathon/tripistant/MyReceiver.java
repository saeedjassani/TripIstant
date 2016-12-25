package com.technovanzahackathon.tripistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pooja S on 12/25/2016.
 */
public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        int noticode = intent.getExtras().getInt("code");
        Log.d("MyReceiver","NotifyCode:"+noticode);

        Intent intent1 = new Intent(context, MyNewIntentService.class);
        intent1.putExtra("rem_title",title);
        intent1.putExtra("rem_content",content);
        intent1.putExtra("code",noticode);
        context.startService(intent1);
    }
}
