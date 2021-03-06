package br.com.actia.communication;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Armani on 27/11/2015.
 */
public class CmdHandlerImp {
    List<CmdHandler> listCmdHandlers = null;

    public CmdHandlerImp(Context context) {
        listCmdHandlers = new LinkedList<>();
        listCmdHandlers.add(new CmdHandler_Multiplex_Status(context));
    }

    public void handle(CanMSG canMSG) {
        for(CmdHandler cmdHandler : listCmdHandlers) {
            if(cmdHandler.getFrameId() == canMSG.getId()) {
                cmdHandler.handle(canMSG);
                break;
            }
        }
    }
}
