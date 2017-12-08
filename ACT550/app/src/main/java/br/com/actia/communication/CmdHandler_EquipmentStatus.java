package br.com.actia.communication;

import android.content.Context;

import br.com.actia.Globals;
import br.com.actia.event.DVDStatusEvent;

/**
 * Created by Armani on 26/11/2015.
 */
public class CmdHandler_EquipmentStatus implements CmdHandler {
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
        if(globals.getEventBus().hasSubscriberForEvent(DVDStatusEvent.class))
            globals.getEventBus().post(new DVDStatusEvent(canMSG.getData()));
    }
}
