package br.com.actia.controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.widget.FrameLayout;
import android.widget.ImageButton;

/**
 * Created by Armani(anderson.armani@actia.com.br) on 08/10/2015.
 */
public class AuxController extends DefaultController {
    private boolean isDriver;

    public AuxController(Context context, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource, boolean isDriver) {
        super(context, frameLayout, button, resource);
        this.isDriver = isDriver;
    }

    @Override
    public void initializeView() {

    }

    @Override
    public void initializeActions() {

    }

    @Override
    public void sendChangeMode() {
        /*if(isVipArea)
            super.sendCommand("18FF6460", 8, "000F00ffffffffff");
        else
            super.sendCommand("18FF6460", 8, "00F000ffffffffff");*/
    }
}
