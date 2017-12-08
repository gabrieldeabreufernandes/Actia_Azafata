package br.com.actia.mplxlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.actia.mplxlauncher.LockedScreenActivity;

/**
 * Created by Armani andersonaramni@gmail.com on 17/05/17.
 */

public class ReceiverTurnedOff extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("TAG", "BROADCAST TURN OFF");

        String action = intent.getAction();
        if(action.equals(Intent.ACTION_SCREEN_OFF)) {
            Intent i = new Intent(context, LockedScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
