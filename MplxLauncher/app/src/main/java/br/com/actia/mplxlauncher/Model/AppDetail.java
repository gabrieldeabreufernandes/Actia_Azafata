package br.com.actia.mplxlauncher.Model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Armani andersonaramni@gmail.com on 29/05/2017.
 */

public class AppDetail implements Serializable {
    private CharSequence label;
    private CharSequence name;
    private Drawable icon;

    public AppDetail(CharSequence label, CharSequence name, Drawable icon) {
        Log.v("APP DETAIL", "label = " + label);

        this.label = label;
        this.name = name;
        this.icon = icon;
    }

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
