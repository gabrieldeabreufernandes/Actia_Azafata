package br.com.actia.communication;

import android.content.Context;

import br.com.actia.Globals;
import br.com.actia.event.DVDStatusEvent;
import br.com.actia.event.DVDTrackStatusEvent;

/**
 * Created by Armani on 26/11/2015.
 */
public class CmdHandler_DVDTrackStatus implements CmdHandler {
    private final int FRAME_ID = CanMSG.MSGID_DVD_TRACK_STATUS;
    Globals globals = null;

    public CmdHandler_DVDTrackStatus(Context context) {
        this.globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        globals.getEventBus().post(new DVDTrackStatusEvent(canMSG.getData()));
    }

}
