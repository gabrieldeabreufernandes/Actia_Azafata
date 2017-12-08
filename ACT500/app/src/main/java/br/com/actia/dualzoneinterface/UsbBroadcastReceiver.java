package br.com.actia.dualzoneinterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import br.com.actia.communication.DeviceCOM.DeviceCom;
import br.com.actia.service.CanComService;

/**
 * Created by Armani andersonaramni@gmail.com on 12/02/17.
 */

public class UsbBroadcastReceiver extends BroadcastReceiver {
    public static final String  USB_DATA = "USB_DATA";
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Broadcast received");

        //Get USB PATH
        /*Uri uri = intent.getData();
        String strUsbData = uri.getPath();

        //Start the Communication Service
        Intent newIntent = new Intent(context, UsbFilesActivity.class);
        newIntent.putExtra(USB_DATA, strUsbData);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(newIntent);*/
    }
}
