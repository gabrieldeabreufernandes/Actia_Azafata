package br.com.actia.communication;

import android.content.Context;

import br.com.actia.Globals;
import br.com.actia.event.DriverStatusEvent;
import br.com.actia.event.RadioStatusEvent;

/**
 * Created by Armani on 27/11/2015.
 */
public class CmdHandler_DriverStatus implements CmdHandler {
    private final int FRAME_ID = CanMSG.MSGID_DRIVER_STATUS;
    private Globals globals = null;

    public CmdHandler_DriverStatus(Context context) {
        globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        if(globals.getEventBus().hasSubscriberForEvent(DriverStatusEvent.class))
            globals.getEventBus().post(new DriverStatusEvent(canMSG.getData()));
    }
}
