package br.com.actia.controller;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import br.com.actia.dualzoneinterface.Interface;
import br.com.actia.model.CONFIG.AppMenuBar;
import br.com.actia.model.CONFIG.AppWorkArea;

/**
 * Created by Armani(anderson.armani@actia.com.br) on 08/10/2015.
 */
public abstract class DefaultController {
    Interface mainActivity = null;
    private FrameLayout mainFrame = null;
    private ImageButton button = null;
    private int resource;
    public View view = null;

    private DefaultController(){}

    public DefaultController(Interface mainActivity, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource) {
        this.mainFrame = frameLayout;
        this.button = button;
        this.resource = resource;
        this.mainActivity = mainActivity;

        /*if(button == null) {
          throw new IllegalArgumentException("Button is null");
        }*/

        if(frameLayout == null) {
            throw new IllegalArgumentException("Main Frame is null");
        }

        view = View.inflate(this.mainActivity, resource, null);

        btnMenuInitialize();
        //initializeView();
        //initializeActions();
    }

    private void btnMenuInitialize() {
        if(button == null)
            return;

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
                sendChangeMode();
            }
        });
    }

    public void showView() {
        mainFrame.removeAllViews();
        mainFrame.addView(view);

        mainActivity.resetMenu(button);
        button.setSelected(true);
    }

    public void resetButton() {
        button.setSelected(false);
    }

    public abstract void initializeView();
    public abstract void initializeActions();
    public abstract void sendChangeMode();
    public abstract void configView(AppWorkArea appWorkArea, AppMenuBar appMenuBar);
}
