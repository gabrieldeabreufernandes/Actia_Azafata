package br.com.actia.mplxlauncher.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import br.com.actia.mplxlauncher.Model.AppUser;

/**
 * Created by Armani andersonaramni@gmail.com on 30/06/2017.
 */
@Entity(foreignKeys = @ForeignKey(entity = AppUser.class,
        parentColumns = "uid",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class UserApps {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "app_name")
    private String appName;

    public UserApps(){
        userId = 0;
        appName = "";
    }

    @Ignore
    public UserApps(int userId, String appName) {
        this.userId = userId;
        this.appName = appName;
    }

    public int getUid() {
        return uid;
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
