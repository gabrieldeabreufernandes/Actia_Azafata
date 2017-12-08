package br.com.actia.event;


import br.com.actia.model.StatusFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class DriverStatusEvent {
    private StatusFrame driverStatusFrame = null;

    public DriverStatusEvent(byte[] data) {
        driverStatusFrame = new StatusFrame(data);
    }

    public StatusFrame getDriverStatusFrame() {
        return driverStatusFrame;
    }

    public void setDriverStatusFrame(StatusFrame driverStatusFrame) {
        this.driverStatusFrame = driverStatusFrame;
    }

    @Override
    public String toString() {
        return "DriverStatusEvent["+ driverStatusFrame +"]";
    }
}
