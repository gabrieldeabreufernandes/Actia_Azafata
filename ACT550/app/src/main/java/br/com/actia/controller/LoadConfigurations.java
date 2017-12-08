package br.com.actia.controller;

import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.com.actia.model.CONFIG.FileStructure;

/**
 * Created by Armani andersonaramni@gmail.com on 22/10/16.
 */

public class LoadConfigurations {
    private static final String TAG = "LoadConfigurations";
    //private static String DEFAULT_FILE_CONFIG = "/multiplex/multiplex_config.json";
    private static String DEFAULT_FILE_CONFIG = "/act5xx/act5xx_config.json";

    public FileStructure getFileStructure() {
        String jsonStr = getFileString();
        Gson gson = new Gson();

        Log.d(TAG, jsonStr);
        FileStructure fileStructure = gson.fromJson(jsonStr, FileStructure.class);

        return fileStructure;
    }


    private String getFileString() {
        File externalStorageDirectory = new File("/mnt/extsd/");
        //File externalStorageDirectory = new File("/mnt/sdcard/act500/");
        File file = new File(externalStorageDirectory.getAbsoluteFile() + DEFAULT_FILE_CONFIG);

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
}