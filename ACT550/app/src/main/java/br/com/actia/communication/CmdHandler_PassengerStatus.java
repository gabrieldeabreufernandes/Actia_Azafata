package br.com.actia.communication;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;

import br.com.actia.Globals;
import br.com.actia.event.DriverStatusEvent;
import br.com.actia.event.MediaPlayerStatusEvent;
import br.com.actia.event.PassengerStatusEvent;
import br.com.actia.event.RadioStatusEvent;

/**
 * Created by Armani on 27/11/2015.
 */
public class CmdHandler_PassengerStatus implements CmdHandler {
    private final String TAG = "CmdHandler_PSGStatus";
    private final int FRAME_ID = CanMSG.MSGID_PASSENGER_STATUS;
    private Globals globals = null;

    public CmdHandler_PassengerStatus(Context context) {
        globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        if(globals.getEventBus().hasSubscriberForEvent(PassengerStatusEvent.class)) {
            Log.d(TAG, "hasSubscriberForEvent");
            globals.getEventBus().post(new PassengerStatusEvent(canMSG.getData()));
            Log.d(TAG, "globals.getEventBus().post = " + Arrays.toString(canMSG.getData()));
        }
    }
}
