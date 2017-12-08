package br.com.actia.event;

import br.com.actia.model.UsbMediaFile;

/**
 * Created by Armani andersonaramni@gmail.com on 22/02/17.
 */

public class PlaylistChangedEvent {
    UsbMediaFile usbMediaFile;

    public PlaylistChangedEvent(UsbMediaFile usbMediaFile) {
        this.usbMediaFile = usbMediaFile;
    }

    public UsbMediaFile getUsbMediaFile() {
        return usbMediaFile;
    }

    public void setUsbMediaFile(UsbMediaFile usbMediaFile) {
        this.usbMediaFile = usbMediaFile;
    }

    @Override
    public String toString() {
        return "PlaylistChanged["+ usbMediaFile +"]";
    }
}
