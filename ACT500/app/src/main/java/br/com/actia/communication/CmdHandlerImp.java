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

        listCmdHandlers.add(new CmdHandler_DVDStatus(context));
        listCmdHandlers.add(new CmdHandler_DVDTrackStatus(context));
        listCmdHandlers.add(new CmdHandler_DriverStatus(context));
        listCmdHandlers.add(new CmdHandler_PassengerStatus(context));
        //listCmdHandlers.add(new CmdHandler_RadioCT(context));
        listCmdHandlers.add(new CmdHandler_RadioPS(context));
        listCmdHandlers.add(new CmdHandler_RadioStatus(context));
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
