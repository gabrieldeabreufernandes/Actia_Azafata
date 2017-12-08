package br.com.actia.event;

/**
 * Created by Armani on 11/12/2015.
 */
public class SelectConfigEvent {
    boolean isDriver;

    public SelectConfigEvent(boolean isDriver){
        this.isDriver = isDriver;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setIsDriver(boolean isDriver) {
        this.isDriver = isDriver;
    }
}
