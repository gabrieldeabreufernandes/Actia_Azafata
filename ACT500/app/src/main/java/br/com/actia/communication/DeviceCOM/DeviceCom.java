package br.com.actia.communication.DeviceCOM;

import javax.sql.ConnectionEventListener;

import br.com.actia.communication.CanMSG;

/**
 * Created by Armani on 26/11/2015.
 */
public interface DeviceCom {
    public static final int DEVICE_BT_ELM327 = 0;
    public static final int DEVICE_BT_C2BT = 1;
    public static final int DEVICE_USB_GRIDCONNECT = 2;

    public static final int DEVICE_STATE_DISCONNECTED = 0;
    public static final int DEVICE_STATE_CONNECTING = 1;
    public static final int DEVICE_STATE_CONNECTED = 2;
    public static final int DEVICE_STATE_RUNNING = 3;

    /**
     *
     * @return true - has any command / false - doesn't has command
     */
    public abstract boolean hasCommand();

    /**
     * Gets the oldest command in the list
     * @return
     */
    public abstract CanMSG getCommand();

    /**
     * Puts a command in the list to send
     * @param canMSG
     */
    public abstract void sendCommand(CanMSG canMSG);

    /**
     * Start device interface and communication threads
     */
    public abstract  void startDevice();

    /**
     * Close com device interface and threads
     */
    public abstract void closeDevice();

    /**
     * Add listener to ConnectionEvent event
     * @param listener
     */
    public void addConnectionEventListener(DeviceConnectionEventListener listener);

    /**
     * Remove listener to ConnectionEvent event
     * @param listener
     */
    public void removeConnectionEventListener(DeviceConnectionEventListener listener);

    /**
     * Add listener to Device Received CMD Event Listener
     * @param listener
     */
    public void addReceivedCmdEventListener(DeviceReceivedCmdEventListener listener);

    /**
     * Remove listener to Device Received CMD Event Listener
     * @param listener
     */
    public void removeReceivedCmdEventListener(DeviceReceivedCmdEventListener listener);
}
