package br.com.actia.model;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */
public class DataChromoBrightness {
    public final static int MIN_BRIGHTNESS = 0x0A;
    public final static int MAX_BRIGHTNESS = 0x64;
    private int value;

    public DataChromoBrightness(int value) {
        setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = (value >= MIN_BRIGHTNESS && value <= MAX_BRIGHTNESS) ? value : MIN_BRIGHTNESS;
    }
}
