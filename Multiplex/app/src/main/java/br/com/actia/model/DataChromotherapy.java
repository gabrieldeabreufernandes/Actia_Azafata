package br.com.actia.model;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */

public class DataChromotherapy {
    private DataFourStates          status      = null;
    private DataChromoTime          time        = null;
    private DataChromoColors        colors      = null;
    private DataChromoBrightness    brightness  = null;

    public DataChromotherapy(){
        //### BYTE 0 ###
        status      = new DataFourStates(DataFourStates.STATE_OFF);
        time        = new DataChromoTime(0);
        //### BYTE 1 ###
        colors      = new DataChromoColors(0);
        //### BYTE 2 ###
        brightness  = new DataChromoBrightness(0);
    }

    public DataChromotherapy(byte[] value){
        //### BYTE 0 ###
        status      = new DataFourStates((value[0] & 0xC0) >> 6);
        time        = new DataChromoTime((value[0] & 0x3F));
        //### BYTE 1 & 2 ###
        colors      = new DataChromoColors(((value[2] & 0x0080) << 1) + (value[1] & 0x00FF));
        //### BYTE 2 ###
        brightness  = new DataChromoBrightness((value[2] & 0x7F));
    }

    public DataFourStates getStatus() {
        return status;
    }

    public void setStatus(DataFourStates status) {
        this.status = status;
    }

    public DataChromoTime getTime() {
        return time;
    }

    public void setTime(DataChromoTime time) {
        this.time = time;
    }

    public DataChromoColors getColors() {
        return colors;
    }

    public void setColors(DataChromoColors colors) {
        this.colors = colors;
    }

    public DataChromoBrightness getBrightness() {
        return brightness;
    }

    public void setBrightness(DataChromoBrightness brightness) {
        this.brightness = brightness;
    }
}
