package br.com.actia.communication.DeviceCOM;

import br.com.actia.communication.CanMSG;

/**
 * Created by Actia on 04/12/2015.
 */
public class DeviceReceivedEvent {
    CanMSG canMSG = null;

    public DeviceReceivedEvent() {
        canMSG = new CanMSG();
    }

    public DeviceReceivedEvent(CanMSG canMSG) {
        this.canMSG = canMSG;
    }

    public CanMSG getCanMSG() {
        return canMSG;
    }

    public void setCanMSG(CanMSG canMSG) {
        this.canMSG = canMSG;
    }
}
