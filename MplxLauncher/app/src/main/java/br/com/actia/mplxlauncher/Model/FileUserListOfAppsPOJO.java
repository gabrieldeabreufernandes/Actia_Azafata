package br.com.actia.mplxlauncher.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 01/06/2017.
 */

public class FileUserListOfAppsPOJO {
    List<UserListOfAppsPOJO> userListOfAppsPOJOArray;

    public FileUserListOfAppsPOJO() {
        userListOfAppsPOJOArray = new ArrayList<>();
    }

    public List<UserListOfAppsPOJO> getUserListOfAppsPOJOArray() {
        return userListOfAppsPOJOArray;
    }

    public void setUserListOfAppsPOJOArray(ArrayList<UserListOfAppsPOJO> userListOfAppsPOJOArray) {
        this.userListOfAppsPOJOArray = userListOfAppsPOJOArray;
    }

    public UserListOfAppsPOJO getUserList() {

        return null;
    }

    public void setUserList(UserListOfAppsPOJO userList) {
        boolean newUser = true;

        //Change APPs for a old user
        for(UserListOfAppsPOJO listUserPojo: userListOfAppsPOJOArray) {
            if(listUserPojo.getUserName().equalsIgnoreCase(userList.getUserName())) {
                listUserPojo.setUserApps(userList.getUserApps());
                newUser = false;
                break;
            }
        }

        //Add new user
        if(newUser == true) {
            userListOfAppsPOJOArray.add(userList);
        }
    }
}
