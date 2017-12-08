package br.com.actia.controller;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import br.com.actia.model.PLAYLIST.MediaFile;

/**
 * Created by Armani andersonaramni@gmail.com on 18/02/17.
 */

public class PlaylistHandler {
    private static final String TAG = "PlaylistHandler";
    private static final String ROOT_PATH_ACTIA   = "/mnt/extsd/";
    private static final String ROOT_PATH_ASUS    = "/mnt/sdcard/Download/";
    private static final String ROOT_PATH_GENESIS = "/sdcard/external_sdcard/";
    private static final String FOLDER_PATH_ACTIA   = "/mnt/extsd/act5xx/playlist/";
    private static final String FOLDER_PATH_ASUS    = "/mnt/sdcard/Download/act5xx/playlist/";
    private static final String FOLDER_PATH_GENESIS = "/sdcard/external_sdcard/act5xx/playlist/";

    private static final String FOLDER = "/act5xx/playlist/";
    public static final String FOLDER_FILTER = ".json";
    public static final String FOLDER_FILTER_SD = "_sd.json";
    public static final String FOLDER_FILTER_USB = "_usb.json";
    public static final int PLAYLIST_USB    = 0x00;
    public static final int PLAYLIST_SD     = 0x01;
    public static final int PLAYLIST_EXTERNAL_USB     = 0x02;

    private String usbPath   = "";
    private String extsdPath = "";

    public PlaylistHandler(String usbPath) {
        this.usbPath = usbPath + FOLDER;
        this.extsdPath = getExternalsdPath();
    }

    /**
     * Get all playlists into the internal path
     * @param type
     * @return
     */
    public List getPlaylists(int type, final String folder_filter) {
        String path = "";
        switch (type){
            case PLAYLIST_SD:
                break;
            case PLAYLIST_USB:
                path = extsdPath;
                break;
            case PLAYLIST_EXTERNAL_USB:
                path = usbPath;
                break;
        }
        File directory = new File(path);

        //If the folder doesn't exists in the tablet
        if(type == PLAYLIST_USB && !directory.exists()) {
           try {
                directory.getParentFile().mkdirs();
                directory.mkdir();
                directory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String files[] = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                File newFile = new File(name);

                name = name.toLowerCase();
                return (!newFile.isHidden() && name.endsWith(folder_filter));
            }
        });

        List list = null;

        if(files != null) {
            Log.v(TAG, String.valueOf(files.length));
            list = new LinkedList(Arrays.asList(files));
        }

        if(list == null)
            list = new LinkedList();

        return list;
    }

    /**
     * Get a List of MediaFiles from playlist file.json
     * @param playlistName
     * @return
     */
    public List<MediaFile> getMedias(String playlistName) {
        Gson gson = new Gson();
        String fileString = getFileString(playlistName);

        Log.d(TAG, fileString);
        List<MediaFile> list = gson.fromJson(fileString,  new TypeToken<List<MediaFile>>(){}.getType());

        return list;
    }

    /**
     * Read playlist file content
     * @param playlistName
     * @return
     */
    private String getFileString(String playlistName) {
        File file = new File(extsdPath + playlistName);

        Log.d(TAG, file.getAbsolutePath());

        if(!file.exists()) {
            Log.d(TAG, "FILE NOT EXISTS");
            return "";
        }
        if(!file.canRead()) {
            Log.d(TAG, "FILE NOT CAN READ");
            return "";
        }
        //Read text from file
        StringBuilder fileText = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                fileText.append(line);
            }

            br.close();
        }
        catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return fileText.toString();
    }

    /**
     * Delete one playlist from the tablet memory
     * @param playlistName
     */
    public boolean deletePlaylist(String playlistName) {
        File file = new File(extsdPath + playlistName);

        if(file != null && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Copy files from USB to tablet memory
     * @param playlistName
     * @return
     */
    public boolean copyPlaylistFromUSB(String playlistName) {
        boolean ret = false;

        File usbFile = new File(usbPath + playlistName);

        if(!usbFile.exists()) {
            Log.d(TAG, "File " + playlistName + " does not exists!");
            return false;
        }

        File tabletFile = new File(extsdPath + playlistName);

        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(usbFile).getChannel();
            destChannel = new FileOutputStream(tabletFile).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            ret = true;
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                sourceChannel.close();
                destChannel.close();
            }catch (Exception e) { }
        }

        return ret;
    }

    /**
     * Define what external path should be used
     * @return
     */
    private String getExternalsdPath() {
        String path = "";

        File file = new File(ROOT_PATH_ACTIA);
        if(file != null && file.exists() && file.isDirectory()) {
            return FOLDER_PATH_ACTIA;
        }

        file = new File(ROOT_PATH_GENESIS);
        if(file != null && file.exists() && file.isDirectory()) {
            return FOLDER_PATH_GENESIS;
        }

        file = new File(ROOT_PATH_ASUS);
        if(file != null && file.exists() && file.isDirectory()) {
            return FOLDER_PATH_ASUS;
        }

        Log.v(TAG, "EXTSD PATH = " + path);

        return path;
    }
}


