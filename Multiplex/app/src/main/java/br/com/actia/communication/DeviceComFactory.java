package br.com.actia.communication;

import br.com.actia.communication.C2BT.DeviceCom_C2BT;
import br.com.actia.communication.DeviceCOM.DeviceCom;

/**
 * Created by Armani on 26/11/2015.
 */
public class DeviceComFactory {


    public static DeviceCom getDevice(int deviceType) {
        DeviceCom deviceCom = null;

        switch(deviceType) {
            case DeviceCom.DEVICE_BT_ELM327:
                deviceCom = new DeviceCom_Elm327();
                break;
            case DeviceCom.DEVICE_BT_C2BT:
                deviceCom = new DeviceCom_C2BT();
                break;
            case DeviceCom.DEVICE_USB_GRIDCONNECT:
                break;
            default:
                break;
        }

        return deviceCom;
    }
}
