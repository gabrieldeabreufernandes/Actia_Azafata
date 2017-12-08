package br.com.actia.communication;

/**
 * Created by Armani on 26/11/2015.
 */
public interface CmdHandler {
    /**
     * Get frame id to be handle
     * @return
     */
    public abstract int getFrameId();

    /**
     * Start command handle process
     * @param canMSG
     */
    public abstract void handle(CanMSG canMSG);
}
