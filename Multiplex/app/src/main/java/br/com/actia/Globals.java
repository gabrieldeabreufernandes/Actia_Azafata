package br.com.actia;

import android.content.Context;
import android.util.Log;

import br.com.actia.communication.CanMSG;
import br.com.actia.event.ComSendEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by Armani on 25/11/2015.
 */
public class Globals {
    private static Globals instance = null;
    private Context context;
    private static EventBus eventBus;

    private Globals(Context context) {
        this.context = context;
    }

    public static Globals getInstance(Context context) {
        if(instance == null) {
            instance = new Globals(context.getApplicationContext());
            eventBus = new EventBus();
        }
        else {
            if (instance.context != context.getApplicationContext()) {
                // I think this should not happen since the application is simple enough that it
                // runs in a single process (no contentprovider or broadcastreceiver).
                // If we extend the application and need to handle multiple contexts, we should
                // have a per-context Globals instance
                throw new IllegalStateException("Globals already instanciated with a different context");
            }
        }
        return instance;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    public static void sendCanCMDEvent(CanMSG canMSG) {
        if(eventBus == null)
            return;

        Log.v("GLOBALS", "SendCanCMDEvent");
        eventBus.post(new ComSendEvent(canMSG));
    }

    public static void sendCanCtrlCMDEvent(byte cmd) {
        if(eventBus == null)
            return;

        Log.v("GLOBALS", "sendCanCtrlCMDEvent");
        CanMSG canMSG = new CanMSG();
        canMSG.setLength((byte) 8);
        canMSG.setType(CanMSG.MSGTYPE_EXTENDED);
        canMSG.buildCmdData(cmd);

        eventBus.post(new ComSendEvent(canMSG));
    }
}
