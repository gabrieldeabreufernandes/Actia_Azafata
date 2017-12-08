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
                        Log.v(TAG, "onConnectionEvent = CONNECTED");
                    }
                    else {
                        Log.v(TAG, "onConnectionEvent = DISCONNECTED");
                        deviceCom.closeDevice();
                        deviceCom.startDevice();
                    }
                }
            });

            deviceCom.addReceivedCmdEventListener(new DeviceReceivedCmdEventListener() {
                @Override
                public void OnReceivedEvent(DeviceReceivedEvent event) {
                    CanMSG canMSG = event.getCanMSG();
                    cmdHandlerImp.handle(canMSG);


                    //Send the same frame as a confirmation to Multiplex (needed for Marcos Irizar Multiplex software)
                    //canMSG.setId(CanMSG.MSGID_MULTIPLEX_CMD);
                    //globals.sendCanCMDEvent(canMSG);
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

            String  strData = "";
            byte    btData[] = canMSG.getData();
            for(int i = 0; i < btData.length; i++)
                strData += " 0x"+ Integer.toHexString(btData[i]);

            String str = "ID = ["+ Integer.toHexString(canMSG.getId()) + "]" + "  DATA = [" + strData + "]";

            Log.v(TAG, str);
            deviceCom.sendCommand(canMSG);
        }
    }
}
