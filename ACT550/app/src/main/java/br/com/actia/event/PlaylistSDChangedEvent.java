package br.com.actia.event;

import br.com.actia.model.UsbMediaFile;

/**
 * Created by Armani andersonaramni@gmail.com on 22/02/17.
 */

public class PlaylistSDChangedEvent {
    UsbMediaFile sdMediaFile;

    public PlaylistSDChangedEvent(UsbMediaFile sdMediaFile) {
        this.sdMediaFile = sdMediaFile;
    }

    public UsbMediaFile getSdMediaFile() {
        return sdMediaFile;
    }

    public void setSdMediaFile(UsbMediaFile sdMediaFile) {
        this.sdMediaFile = sdMediaFile;
    }

    @Override
    public String toString() {
        return "PlaylistSDChanged["+ sdMediaFile +"]";
    }
}
