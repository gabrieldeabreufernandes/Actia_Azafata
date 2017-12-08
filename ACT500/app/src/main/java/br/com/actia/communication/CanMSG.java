package br.com.actia.communication;

/**
 * Created by Armani on 26/11/2015.
 */
public class CanMSG {
    static public final byte CAN_NO_CMD = 0x00;
    static public final byte CAN_CMD_POWER_ON = 0x01;
    static public final byte CAN_CMD_POWER_OFF = 0x02;
    static public final byte CAN_CMD_EJECT = 0x03;
    static public final byte CAN_CMD_INSERT = 0x04;
    static public final byte CAN_CMD_DECK_VOLUP = 0x05;
    static public final byte CAN_CMD_DECK_VOLDOWN = 0x06;
    static public final byte CAN_CMD_MUTE = 0x07;
    static public final byte CAN_CMD_STOP = 0x08;
    static public final byte CAN_CMD_PAUSE = 0x09;
    static public final byte CAN_CMD_PLAY = 0x0A;
    static public final byte CAN_CMD_BACK = 0x0B;
    static public final byte CAN_CMD_FRX4 = 0x0C;
    static public final byte CAN_CMD_FRX32 = 0x0D;
    static public final byte CAN_CMD_NEXT = 0x0E;
    static public final byte CAN_CMD_FF4 = 0x0F;
    static public final byte CAN_CMD_FF32 = 0x10;
    static public final byte CAN_CMD_SLOWR = 0x11;
    static public final byte CAN_CMD_SLOWF = 0x12;
    static public final byte CAN_CMD_SEEK_UP = 0x13;
    static public final byte CAN_CMD_SEEK_DOWN = 0x14;
    static public final byte CAN_CMD_ENTER = 0x15;
    static public final byte CAN_CMD_LEFT_ARROW = 0x16;
    static public final byte CAN_CMD_RIGHT_ARROW = 0x17;
    static public final byte CAN_CMD_DOWN_ARROW = 0x18;
    static public final byte CAN_CMD_UP_ARROW = 0x19;
    //1A TO 1B NOT USED
    static public final byte CAN_CMD_SETUP = 0x20;
    static public final byte CAN_CMD_MENU = 0x21;
    static public final byte CAN_CMD_TITLE = 0x22;
    static public final byte CAN_CMD_SUBTITLE = 0x23;
    static public final byte CAN_CMD_LANGUAGE = 0x24;
    static public final byte CAN_CMD_DIR_DOWN = 0x25;
    static public final byte CAN_CMD_DIR_UP = 0x26;
    static public final byte CAN_CMD_DISK_DOWN = 0x27;
    static public final byte CAN_CMD_DISK_UP = 0x28;
    static public final byte CAN_CMD_DISPLAY = 0x29;
    static public final byte CAN_CMD_ENABLE_BLOCKED_STATE = 0x2A;
    static public final byte CAN_CMD_DISABLE_BLOCKED_STATE = 0x2B;
    static public final byte CAN_CMD_SCAN = 0x2C;
    static public final byte CAN_CMD_AUTO_MEM = 0x2D;
    static public final byte CAN_CMD_PLAY_MODE = 0x2E;
    static public final byte CAN_CMD_EQU_DRV_FLAT = 0x35;
    static public final byte CAN_CMD_EQU_DRV_ROCK = 0x36;
    static public final byte CAN_CMD_EQU_DRV_OPERA = 0x37;
    static public final byte CAN_CMD_EQU_DRV_POP = 0x38;
    static public final byte CAN_CMD_EQU_DRV_VOICE = 0x39;
    static public final byte CAN_CMD_EQU_PAS_FLAT = 0x3A;
    static public final byte CAN_CMD_EQU_PAS_ROCK = 0x3B;
    static public final byte CAN_CMD_EQU_PAS_OPERA = 0x3C;
    static public final byte CAN_CMD_EQU_PAS_POP = 0x3D;
    static public final byte CAN_CMD_EQU_PAS_VOICE = 0x3F; //OR 0X3F LIKE THE MANUAL
    static public final byte CAN_CMD_RADIO_CHL1 = 0x40;
    static public final byte CAN_CMD_RADIO_CHL2 = 0x41;
    static public final byte CAN_CMD_RADIO_CHL3 = 0x42;
    static public final byte CAN_CMD_RADIO_CHL4 = 0x43;
    static public final byte CAN_CMD_RADIO_CHL5 = 0x44;
    static public final byte CAN_CMD_RADIO_CHL6 = 0x45;
    static public final byte CAN_CMD_RADIO_FM1 = 0x46;
    static public final byte CAN_CMD_RADIO_FM2 = 0x47;
    static public final byte CAN_CMD_RADIO_FM3 = 0x48;
    static public final byte CAN_CMD_RADIO_AM1 = 0x49;
    static public final byte CAN_CMD_RADIO_AM2 = 0x4A;
    static public final byte CAN_CMD_TA = 0x4B;
    static public final byte CAN_CMD_AF = 0x4C;
    static public final byte CAN_CMD_RDS = 0x4D;
    static public final byte CAN_CMD_FILE_MUSIC = 0x4E;
    static public final byte CAN_CMD_FILE_MOVIE = 0x4F;
    static public final byte CAN_CMD_FILE_PICTURE = 0x50;

    static public final int MSGID_EQUIPAMENT_CTRL = 0x18FF6460;
    static public final int MSGID_DVD_TRACK_STATUS = 0x18FF6564;
    static public final int MSGID_DVD_STATUS = 0x18FF6664;
    static public final int MSGID_RADIO_STATUS = 0x18FF6864;
    static public final int MSGID_RADIO_CTRL = 0x18FF6760;
    static public final int MSGID_RADIO_CT = 0x18FF6764;
    static public final int MSGID_RADIO_PS = 0x18FF6A64;
    static public final int MSGID_PASSENGER_CTRL = 0x18FF6560;
    static public final int MSGID_DRIVER_CTRL = 0x18FF6660;
    static public final int MSGID_PASSENGER_STATUS = 0x18FF6964;
    static public final int MSGID_DRIVER_STATUS = 0x18FF6B64;
    static public final int MSGID_TCO1 = 0x18FF6CEE;

    static public final byte DEFAULT_MSG_LEN = 8;

    /**
     * 11bit message type (standard)
     */
    static public final byte MSGTYPE_STANDARD = 0x0;
    /**
     * Remote request
     */
    static public final byte MSGTYPE_RTR = 0x1;
    /**
     * 29bit message type (extended)
     */
    static public final byte MSGTYPE_EXTENDED = 0x2;

    static public final byte[] DEFAULT_NO_CMD = "00ff00ffffffffff".getBytes();

    private int id;
    private byte type;
    private byte length;
    private byte data[];

    /**
     * Default constructor
     */
    public CanMSG() {
        data = new byte[8];
    }

    /**
     * Constructs a new message object.
     * @param id the message id
     * @param type the message type
     * @param length the message length
     * @param data the message data
     */
    public CanMSG(int id, byte type, byte length, byte[] data) {
        this.id = id;
        this.type = type;
        this.length = length;
        this.data = new byte[length];

        int len = length < data.length ? length : data.length;
        for (int j = 0; j < len; j++)
        {
            this.data[j] = data[j];
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    /*public void setData(byte[] data) {
        this.data = data;
    }*/

    public void setData(byte[] data, byte length)
    {
        this.length = (byte)((length > this.data.length) ? this.data.length : length);
        for (int j = 0; j < this.length; j++)
        {
            this.data[j] = data[j];
        }
    }

    public void setData(String strData)
    {
        int len = strData.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(strData.charAt(i), 16) << 4)
                    + Character.digit(strData.charAt(i+1), 16));
        }

        int cpLen = len > this.length ? this.length : len;
        for (int j = 0; j < cpLen; j++)
        {
            this.data[j] = data[j];
        }

        return ;
    }

    /**
     * Set command data
     * @param cmdIdentifier - byte cmd identifier
     */
    public void buildCmdData(byte cmdIdentifier) {
        //Default CMD
        byte bt[] = {cmdIdentifier, (byte)0xFF, 0x00, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};

        for(int i = 0; i < DEFAULT_MSG_LEN; i++) {
            this.data[i] = bt[i];
        }
    }
}
