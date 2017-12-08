package br.com.actia.communication;

import android.content.Context;

import br.com.actia.Globals;
import br.com.actia.event.DVDStatusEvent;

/**
 * Created by Armani on 26/11/2015.
 */
public class CmdHandler_DVDStatus implements CmdHandler {
    private final int FRAME_ID = CanMSG.MSGID_DVD_STATUS;
    Globals globals = null;

    public CmdHandler_DVDStatus(Context context) {
        this.globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        globals.getEventBus().post(new DVDStatusEvent(canMSG.getData()));
    }
}
