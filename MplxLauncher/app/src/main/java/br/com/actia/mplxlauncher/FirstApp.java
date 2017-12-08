package br.com.actia.mplxlauncher;

import android.app.Application;

/**
 * Created by Armani andersonaramni@gmail.com on 01/06/2017.
 */

public class FirstApp extends Application {
    private static FirstApp instance;
    public static FirstApp getInstance() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
