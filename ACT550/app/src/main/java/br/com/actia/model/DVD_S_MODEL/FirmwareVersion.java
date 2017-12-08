package br.com.actia.model.DVD_S_MODEL;

/**
 * Created by Armani andersonaramni@gmail.com on 27/12/16.
 */

public class FirmwareVersion {
    byte majorVersion;
    byte minorVersion;
    int revisionNumber;

    public FirmwareVersion(byte value[]){
        majorVersion    = value[0];
        minorVersion    = value[1];
        revisionNumber  = (value[2] << 8) + value[3];
    }

    public byte getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(byte majorVersion) {
        this.majorVersion = majorVersion;
    }

    public byte getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(byte minorVersion) {
        this.minorVersion = minorVersion;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }
    
    public String getStringVersion() {
        return "Version " + String.valueOf(majorVersion) + "." + String.valueOf(minorVersion) + "." + String.valueOf(revisionNumber);
    }
}
