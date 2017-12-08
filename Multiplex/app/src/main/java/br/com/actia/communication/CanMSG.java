package br.com.actia.communication;

/**
 * Created by Armani on 26/11/2015.
 */
public class CanMSG {
    static public final int MSGID_MULTIPLEX_CMD = 0x18FF6580;
    static public final int MSGID_MULTIPLEX_STATUS = 0x18FF8065;

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

    @Override
    public String toString() {
        String ret = "CMD_ID: " + id + " TYPE: " + type + " LENGTH: " + length + " DATA: " + data[0] + "/" +
                                + data[1] + "/" + data[2]  + "/" + data[3]  + "/" + data[4]  + "/" + data[5]
                                + "/" + data[6]  + "/" + data[7];
        return ret;
    }
}
