package br.com.actia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import br.com.actia.Globals;
import br.com.actia.communication.CanMSG;
import br.com.actia.communication.CmdHandlerImp;
import br.com.actia.communication.DeviceCOM.DeviceCom;
import br.com.actia.communication.DeviceCOM.DeviceConnectionEvent;
import br.com.actia.communication.DeviceCOM.DeviceConnectionEventListener;
import br.com.actia.communication.DeviceCOM.DeviceReceivedCmdEventListener;
import br.com.actia.communication.DeviceCOM.DeviceReceivedEvent;
import br.com.actia.communication.DeviceComFactory;
import br.com.actia.event.ComSendEvent;
//import br.com.actia.event.RadioChangeEvent;

/**
 * Created by Armani on 26/11/2015.
 */
public class CanComService extends Service {
    private final static String TAG = "CanComService";
    private DeviceCom deviceCom = null;
    private CmdHandlerImp cmdHandlerImp = null;
    private Globals globals = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        globals.getInstance(this);
        if(!globals.getEventBus().isRegistered(this))
            globals.getEventBus().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "onStartCommand");
        Bundle extras = intent.getExtras();
        if(extras != null) {
            int deviceType = extras.getInt("COM_TYPE");
            cmdHandlerImp = new CmdHandlerImp(this);
            deviceCom = DeviceComFactory.getDevice(deviceType);
            deviceCom.startDevice();

            Log.i(TAG, "DeviceType = " + deviceType);
            Log.i(TAG, "DeviceType initialize = " + (deviceCom != null));
            //register an event
            /*if(!globals.getEventBus().getDefault().isRegistered(this))
                globals.getEventBus().getDefault().register(this);*/

            deviceCom.addConnectionEventListener(new DeviceConnectionEventListener() {
                @Override
                public void onConnectionEvent(DeviceConnectionEvent event) {
                    if(event.isConnected()) {
                        System.out.println("onConnectionEvent = CONNECTED");
                    }
                    else {
                        System.out.println("onConnectionEvent = DISCONNECTED");
                        deviceCom.closeDevice();
                        deviceCom.startDevice();
                    }
                }
            });

            deviceCom.addReceivedCmdEventListener(new DeviceReceivedCmdEventListener() {
                @Override
                public void OnReceivedEvent(DeviceReceivedEvent event) {
                    CanMSG canMSG = event.getCanMSG();
                    Log.d(TAG, "RECEIVED MSG\n" + canMSG.toString());

                    cmdHandlerImp.handle(canMSG);
                }
            });
        }

        return ret;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");

        if(globals.getEventBus().isRegistered(this))
            globals.getEventBus().unregister(this);

        //closeWorker();
        deviceCom.closeDevice();
        deviceCom = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onEventBackgroundThread(final ComSendEvent event) {
        CanMSG canMSG = event.getCanMSG();

        if(canMSG != null) {
            //Log.i(TAG, "CAN MSG FROM TABLET = " + canMSG.getId());
            deviceCom.sendCommand(canMSG);
        }
    }
}
