package br.com.actia.communication;

import br.com.actia.communication.DeviceCOM.DeviceCom;
import br.com.actia.communication.DeviceCOM.DeviceConnectionEventListener;
import br.com.actia.communication.DeviceCOM.DeviceReceivedCmdEventListener;

/**
 * Created by Armani on 26/11/2015.
 */
public class DeviceCom_Elm327 implements DeviceCom {

    @Override
    public boolean hasCommand() {
        return false;
    }

    @Override
    public CanMSG getCommand() {
        return null;
    }

    @Override
    public void sendCommand(CanMSG canMSG) {

    }

    @Override
    public void startDevice() {

    }

    @Override
    public void closeDevice() {

    }

    @Override
    public void addConnectionEventListener(DeviceConnectionEventListener listener) {

    }

    @Override
    public void removeConnectionEventListener(DeviceConnectionEventListener listener) {

    }

    @Override
    public void addReceivedCmdEventListener(DeviceReceivedCmdEventListener listener) {

    }

    @Override
    public void removeReceivedCmdEventListener(DeviceReceivedCmdEventListener listener) {

    }
}
