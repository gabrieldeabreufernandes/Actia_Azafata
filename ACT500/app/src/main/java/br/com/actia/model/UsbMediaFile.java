package br.com.actia.model;

import br.com.actia.controller.PlaylistHandler;

/**
 * Created by Armani andersonaramni@gmail.com on 22/02/17.
 */

public class UsbMediaFile {
    private String  playlistPath;
    private int     fileNumber;
    private String  fileName;

    public UsbMediaFile() {
        playlistPath    = "";
        fileNumber      = 0;
    }
    public String getPlaylistPath() {
        return playlistPath;
    }

    public void setPlaylistPath(String playlistPath) {
        this.playlistPath = playlistPath;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Playlist: " + playlistPath + " File Number: " + fileNumber + " File Name: " + fileName;
    }
}
