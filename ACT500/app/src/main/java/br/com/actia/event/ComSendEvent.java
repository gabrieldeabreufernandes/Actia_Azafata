package br.com.actia.event;

import br.com.actia.communication.BuilCanMSG;
import br.com.actia.communication.CanMSG;

/**
 * Created by Armani on 26/11/2015.
 */
public class ComSendEvent {
    public final CanMSG canMSG;

    public ComSendEvent(CanMSG canMSG){
        this.canMSG = canMSG;
    }

    ComSendEvent(String id, byte type, int len, String data) {
        this.canMSG = new BuilCanMSG()
                .setId(id)
                .setType(type)
                .setLength(len)
                .setData(data)
                .build();
    }

    public CanMSG getCanMSG() {
        return canMSG;
    }

    @Override
    public String toString() {
        return "ComSentEvent["+canMSG.toString()+"]";
    }
}
