package br.com.actia.communication;

import android.content.Context;

import br.com.actia.Globals;
import br.com.actia.event.MultiplexStatusEvent;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class CmdHandler_Multiplex_Status implements CmdHandler {
    private final int FRAME_ID = CanMSG.MSGID_MULTIPLEX_STATUS;
    Globals globals = null;

    public CmdHandler_Multiplex_Status(Context context) {
        this.globals = Globals.getInstance(context);
    }

    @Override
    public int getFrameId() {
        return FRAME_ID;
    }

    @Override
    public void handle(CanMSG canMSG) {
        globals.getEventBus().post(new MultiplexStatusEvent(canMSG.getData()));
    }
}
