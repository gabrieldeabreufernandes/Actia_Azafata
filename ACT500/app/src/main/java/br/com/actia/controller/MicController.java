package br.com.actia.controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import br.com.actia.dualzoneinterface.Interface;
import br.com.actia.model.CONFIG.AppMenuBar;
import br.com.actia.model.CONFIG.AppWorkArea;

/**
 * Created by Armani(anderson.armani@actia.com.br) on 11/12/2015.
 */
public class MicController extends DefaultController {
    private boolean isDriver;

    public MicController(Interface mainActivity, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource, boolean isDriver) {
        super(mainActivity, frameLayout, button, resource);
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
    }

    @Override
    public void configView(AppWorkArea appWorkArea, AppMenuBar appMenuBar) {

    }
}
