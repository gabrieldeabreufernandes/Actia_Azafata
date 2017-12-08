package br.com.actia.communication;

import android.content.Context;

import br.com.actia.Globals;
import br.com.actia.event.RadioPSEvent;
import br.com.actia.event.RadioStatusEvent;

/**
 * Created by Armani on 27/11/2015.
 */
public class CmdHandler_RadioPS implements CmdHandler {
    private final int FRAME_ID = CanMSG.MSGID_RADIO_PS;
    private Globals globals = null;

    public CmdHandler_RadioPS(Context context) {
        globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        if(globals.getEventBus().hasSubscriberForEvent(RadioPSEvent.class))
            globals.getEventBus().post(new RadioPSEvent(canMSG.getData()));
    }
}
