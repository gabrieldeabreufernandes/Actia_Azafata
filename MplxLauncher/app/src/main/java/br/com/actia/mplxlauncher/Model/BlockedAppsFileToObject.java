package br.com.actia.mplxlauncher.Model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import br.com.actia.mplxlauncher.FirstApp;

/**
 * Created by Armani andersonaramni@gmail.com on 01/06/2017.
 */

public class BlockedAppsFileToObject {
    private static final String TAG = BlockedAppsFileToObject.class.getSimpleName();
    private static final String USER_APPS_FILE = "blocked_apps.json";
    private Context context;

    public BlockedAppsFileToObject() {
        context = FirstApp.getInstance().getApplicationContext();
    }

    /**
     * Get the list of Apps from one specific user
     * @return
     */
    public List<String> getBlockedApps() {
        List<String> blockedAppsList = null;
        GsonFiles gsonFiles = new GsonFiles(context);
        String jsonStr = gsonFiles.getFileString(USER_APPS_FILE);

        Gson gson = new Gson();
        blockedAppsList = gson.fromJson(jsonStr, new TypeToken<List<String>>(){}.getType());

        if(blockedAppsList == null) {
            blockedAppsList = new LinkedList<>();
        }

        return blockedAppsList;
    }

    public void setBlockedApps(List<String> listBlockedApps) {
        Gson gson = new Gson();
        String strJson = gson.toJson(listBlockedApps);

        //Write into FILE
        GsonFiles gsonFiles = new GsonFiles(context);
        gsonFiles.writeFileString(strJson, USER_APPS_FILE);
    }
}
