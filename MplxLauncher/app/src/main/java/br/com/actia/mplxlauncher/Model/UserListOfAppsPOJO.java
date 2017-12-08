package br.com.actia.mplxlauncher.Model;

import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 31/05/2017.
 */

class UserListOfAppsPOJO {
    private String userName;
    private List<String> userApps;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getUserApps() {
        return userApps;
    }

    public void setUserApps(List<String> userApps) {
        this.userApps = userApps;
    }
}
