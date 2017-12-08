package br.com.actia.mplxlauncher.Model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Armani andersonaramni@gmail.com on 13/06/2017.
 */

public class GsonFiles {
    private static final String TAG = GsonFiles.class.getSimpleName();
    private Context context;

    public GsonFiles(Context context) {
        this.context = context;
    }

    /**
     * Read the user apps json file and return it's string
     * @return
     */
    public String getFileString(String fileName) {
        String filePath = context.getFilesDir().getPath() + fileName;
        File file = new File(filePath);

        Log.d(TAG, "Reading file " + file.getAbsolutePath());

        if(!file.exists()) {
            try {
                file.createNewFile();
                file.setReadable(true);
                file.setWritable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
     * Write String Json into file
     * @param strJson
     */
    public void writeFileString(String strJson, String fileName) {
        String filePath = context.getFilesDir().getPath() + fileName;
        File file = new File(filePath);

        Log.d(TAG, "Writing file " + file.getAbsolutePath());

        //delete current file to rewrite it
        if(file.exists()) {
            file.delete();
        }

        if(!file.exists()) {
            try {
                file.createNewFile();
                file.setReadable(true);
                file.setWritable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream outputStream;

        try {
            //outputStream = context.openFileOutput(USER_APPS_FILE, Context.MODE_PRIVATE);
            outputStream = new FileOutputStream(filePath);
            outputStream.write(strJson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
