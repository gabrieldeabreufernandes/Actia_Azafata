package br.com.actia.mplxlauncher.Model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.actia.mplxlauncher.FirstApp;

/**
 * Created by Armani andersonaramni@gmail.com on 01/06/2017.
 */

public class UserAppsFileToObject {
    private static final String TAG = UserAppsFileToObject.class.getSimpleName();
    private static final String USER_APPS_FILE = "user_apps.json";
    private Context context;

    UserAppsFileToObject() {
        context = FirstApp.getInstance().getApplicationContext();
    }

    /**
     * Get the list of Apps from one specific user
     * @return
     */
    public List<String> getUserApps(String userName) {
        List<String> listOfApps = null;
        List<UserListOfAppsPOJO> arrListOfApps = null;
        GsonFiles gsonFiles = new GsonFiles(context);

        String jsonStr = gsonFiles.getFileString(USER_APPS_FILE);
        Gson gson = new Gson();
        FileUserListOfAppsPOJO fileUserListOfAppsPOJO = gson.fromJson(jsonStr, FileUserListOfAppsPOJO.class);

        if(fileUserListOfAppsPOJO != null) {
            arrListOfApps = fileUserListOfAppsPOJO.getUserListOfAppsPOJOArray();

            for (UserListOfAppsPOJO userList : arrListOfApps) {
                if (userList.getUserName().equalsIgnoreCase(userName)) {
                    listOfApps = userList.getUserApps();
                    break;
                }
            }
        }

        if(listOfApps == null) {
            listOfApps = new ArrayList<>();
        }

        return listOfApps;
    }

    public void writeStructureToFile(String userName, List<String> listApps) {
        FileUserListOfAppsPOJO fileUserListOfAppsPOJO = null;
        Gson gson = new Gson();
        String strJson = gson.toJson(listApps);
        GsonFiles gsonFiles = new GsonFiles(context);

        //get data from file
        if(strJson != null && !strJson.isEmpty()) {
            String jsonStr = gsonFiles.getFileString(USER_APPS_FILE); //read string json from file
            fileUserListOfAppsPOJO = gson.fromJson(jsonStr, FileUserListOfAppsPOJO.class);
        }

        if(fileUserListOfAppsPOJO == null) {
            fileUserListOfAppsPOJO = new FileUserListOfAppsPOJO();
        }

        //ADD new or change a current USER APPS
        UserListOfAppsPOJO userListOfAppsPOJO = new UserListOfAppsPOJO();
        userListOfAppsPOJO.setUserName(userName);
        userListOfAppsPOJO.setUserApps(listApps);

        fileUserListOfAppsPOJO.setUserList(userListOfAppsPOJO);

        //Write into FILE
        String strFileJson = gson.toJson(fileUserListOfAppsPOJO, FileUserListOfAppsPOJO.class);

        gsonFiles.writeFileString(strFileJson, USER_APPS_FILE);
    }
}
