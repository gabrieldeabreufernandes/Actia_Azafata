package br.com.actia.communication;

import android.content.Context;

import br.com.actia.Globals;
import br.com.actia.event.RadioStatusEvent;

/**
 * Created by Armani on 26/11/2015.
 */
public class CmdHandler_RadioStatus implements CmdHandler {
    private final int FRAME_ID = CanMSG.MSGID_RADIO_STATUS;
    private Globals globals = null;

    public CmdHandler_RadioStatus(Context context) {
        globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        globals.getEventBus().post(new RadioStatusEvent(canMSG.getData()));
    }
}
