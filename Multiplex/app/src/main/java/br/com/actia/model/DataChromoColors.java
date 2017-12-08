package br.com.actia.model;

/**
 * Created by Armani andersonaramni@gmail.com on 26/10/16.
 */
public class DataChromoColors {
    public final static int COLOR_NO_COLOR = 0X00;
    public final static int COLOR_PINK   = 0X01;
    public final static int COLOR_GREEN  = 0X02;
    public final static int COLOR_ORANGE = 0X04;
    public final static int COLOR_PURPLE = 0X08;
    public final static int COLOR_RED    = 0X10;
    public final static int COLOR_CYAN   = 0X20;
    public final static int COLOR_YELLOW = 0X40;
    public final static int COLOR_BLUE   = 0X80;
    public final static int COLOR_WHITE_GREEN = 0X0100;

    private int value;

    public DataChromoColors(int value) {
        if(value > COLOR_NO_COLOR && value <= COLOR_WHITE_GREEN)
            this.value = value;
        else
            this.value = COLOR_NO_COLOR;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
