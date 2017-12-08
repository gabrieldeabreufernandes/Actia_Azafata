package com.gmail.andersonarmani.screentest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

/**
 * Created by andersonarmani on 18/10/16.
 */

public class MultipleStateButton extends ImageButton {
    private static int ALPHA_MIN = 50;
    private static int ALPHA_MAX = 255;
    private static int STATE_DISABLED = 0;
    private static int STATE_MIN = 1;
    private static int STATE_MAX = 3;
    private int state;


    public MultipleStateButton(Context context) {
        super(context);
        init();
    }

    public MultipleStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultipleStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultipleStateButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //############  ARMANI  ############

    /**
     * Start component.
     */
    private void init() {
        state = STATE_MIN;
        drawInternalState();
    }

    /**
     * Override method OnTouchEvent to change internal state after a click.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP &&
                isEnabled()) {
            nextState();
        }

        return super.onTouchEvent(event);
    }

    /**
     * Change the internal state for the next valid state.
     */
    private void nextState() {
        state = (state == STATE_MAX ? STATE_MIN: ++state);
        drawInternalState();
    }

    /**
     * Set the component internal state. It will change the image background and image icon.
     * @param state
     */
    public void setState(int state) {
        if(!isEnabled())
            return;

        if(state < STATE_MIN || state > STATE_MAX)
            return;

        this.state = state;
        drawInternalState();
    }

    /**
     * Get component internal state
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * Change button background state.
     */
    private void drawInternalState() {


        Drawable d = getBackground();
        if(d != null)
            d.setLevel(state);
    }

    /**
     * Override SetEnabled method to change the icon alpha definition.
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        Drawable d = this.getDrawable();
        if(d == null)
            return;

        if(enabled == false) {
            d.setAlpha(ALPHA_MIN);
            state = STATE_DISABLED;
        }
        else {
            d.setAlpha(ALPHA_MAX);
            state = STATE_MIN;
        }

        drawInternalState();
        super.setEnabled(enabled);
    }
}
