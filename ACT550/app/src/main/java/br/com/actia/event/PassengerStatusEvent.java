package br.com.actia.event;

import br.com.actia.model.StatusFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class PassengerStatusEvent {
    private StatusFrame passengerStatusFrame = null;

    public PassengerStatusEvent(byte[] data) {
        passengerStatusFrame = new StatusFrame(data);
    }

    public StatusFrame getPassengerStatusFrame() {
        return passengerStatusFrame;
    }

    public void setPassengerStatusFrame(StatusFrame passengerStatusFrame) {
        this.passengerStatusFrame = passengerStatusFrame;
    }

    @Override
    public String toString() {
        return "PassengerStatusEvent["+ passengerStatusFrame +"]";
    }
}
