package br.com.actia.communication;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;

import br.com.actia.Globals;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.PassengerStatusEvent;

/**
 * Created by Armani on 26/11/2015.
 */
public class CmdHandler_EquipmentStatus implements CmdHandler {
    private final String TAG = "CmdHandler_EquipStatus";
    private final int FRAME_ID = CanMSG.MSGID_EQUIPMENT_STATUS;
    Globals globals = null;

    public CmdHandler_EquipmentStatus(Context context) {
        this.globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        if(globals.getEventBus().hasSubscriberForEvent(DVDStatusEvent.class)) {
            Log.d(TAG, "hasSubscriberForEvent");
            globals.getEventBus().post(new DVDStatusEvent(canMSG.getData()));
            Log.d(TAG, "globals.getEventBus().post = " + Arrays.toString(canMSG.getData()));
        }
    }
}
