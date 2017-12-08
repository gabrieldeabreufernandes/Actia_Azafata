package br.com.actia.mplxlauncher.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Armani andersonaramni@gmail.com on 28/06/2017.
 */
@Entity(indices = {@Index(value = {"app_name"}, unique = true)})
public class AppsAllowed {
    public static final int DEFAULT_USER = 0;
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "app_name")
    private String appName;

    public int getUid() {
        return uid;
    }

    public AppsAllowed(int userId, String appName){
        this.userId  = userId;
        this.appName = appName;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
