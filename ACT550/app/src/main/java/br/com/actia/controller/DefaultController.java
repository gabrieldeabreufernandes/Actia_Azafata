package br.com.actia.controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

/**
 * Created by Armani(anderson.armani@actia.com.br) on 08/10/2015.
 */
public abstract class DefaultController {
    Context mainContext = null;
    private FrameLayout mainFrame = null;
    private ImageButton button = null;
    private int resource;
    public View view = null;

    private DefaultController(){}

    public DefaultController(Context context, FrameLayout frameLayout, ImageButton button, @LayoutRes int resource) {
        this.mainFrame = frameLayout;
        this.button = button;
        this.resource = resource;
        this.mainContext = context;

        /*if(button == null) {
          throw new IllegalArgumentException("Button is null");
        }*/

        if(frameLayout == null) {
            throw new IllegalArgumentException("Main Frame is null");
        }

        view = View.inflate(mainContext, resource, null);

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
    }

    public abstract void initializeView();
    public abstract void initializeActions();
    public abstract void sendChangeMode();
}
