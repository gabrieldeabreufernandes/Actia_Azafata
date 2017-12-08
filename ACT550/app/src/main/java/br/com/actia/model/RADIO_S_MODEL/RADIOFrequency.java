package br.com.actia.model.RADIO_S_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class RADIOFrequency {
    private int frequency;

    public RADIOFrequency(int highDigit, int lowDigit) {
        frequency = ((highDigit & 0xff) << 8) + (lowDigit & 0xff);
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getAMDial() {
        return String.valueOf(frequency);
    }

    public String getFMDial() {
        return String.valueOf(frequency / 100) + ','  + String.valueOf(frequency % 100);
    }
}
