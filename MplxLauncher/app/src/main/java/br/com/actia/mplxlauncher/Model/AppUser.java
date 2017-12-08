package br.com.actia.mplxlauncher.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Armani andersonaramni@gmail.com on 24/05/17.
 */
@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class AppUser implements Serializable {
    public static final boolean ADMIN = true;
    public static final boolean OPERATOR = false;

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "isAdmin")
    private boolean isAdming;

    public AppUser() {
        this.name = "";
        this.password = "";
        this.isAdming = OPERATOR;
    }

    @Ignore
    public AppUser(String name, String password, boolean isAdming) {
        this.name = name;
        this.password = password;
        this.isAdming = isAdming;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdming() {
        return isAdming;
    }

    public void setAdming(boolean adming) {
        isAdming = adming;
    }

    @Override
    public String toString() {
        return name;
    }
}
