package br.com.actia.model.DVD_TS_MODEL;

/**
 * Created by Armani on 19/11/2015.
 */
public class DZPower {
    /**Dual Zone Powered ON. */
    static public final byte DZ_POWER_ON = 0x01;
    /**Dual Zone Powered OFF. */
    static public final byte DZ_POWER_OFF = 0x00;

    private final byte value;

    public DZPower(byte value)
    {
        this.value = value;
    }


    public byte getValue() {
        return value;
    }
};
