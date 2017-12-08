package br.com.actia.mplxlauncher.Service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import br.com.actia.mplxlauncher.receiver.ReceiverTurnedOff;

/**
 * Created by Armani andersonaramni@gmail.com on 17/05/17.
 */

public class MainService extends Service {
    private static final String TAG = MainService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(TAG, "OnCreate");

        KeyguardManager.KeyguardLock key;
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        key = km.newKeyguardLock("IN");
        key.disableKeyguard();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        ReceiverTurnedOff receiver = new ReceiverTurnedOff();
        registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "OnBind");
        return null;
    }
}
