package br.com.actia.event;

import br.com.actia.model.EquipmentStatusFrame;

/**
 * Created by Armani on 27/11/2015.
 */
public class DVDStatusEvent {
    private EquipmentStatusFrame equipmentStatusFrame = null;

    public DVDStatusEvent(byte[] data){
        equipmentStatusFrame = new EquipmentStatusFrame(data);
    }

    public EquipmentStatusFrame getEquipmentStatusFrame() {
        return equipmentStatusFrame;
    }

    public void setEquipmentStatusFrame(EquipmentStatusFrame equipmentStatusFrame) {
        this.equipmentStatusFrame = equipmentStatusFrame;
    }

    @Override
    public String toString() {
        return "DVDStatusEvent["+ equipmentStatusFrame +"]";
    }
}
