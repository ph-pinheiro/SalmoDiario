package com.phpinheiro.salmodiario;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String DEBUG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG, "Receiver captou. Criando notificação.");

        // start the download
        Intent notificador = new Intent(context, NotificarService.class);
        //downloader.setData(Uri.parse("http://feeds.feedburner.com/MobileTuts?format=xml"));
        context.startService(notificador);
    }
}
