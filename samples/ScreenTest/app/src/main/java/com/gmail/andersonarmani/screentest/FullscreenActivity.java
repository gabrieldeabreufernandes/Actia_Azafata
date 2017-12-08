package com.gmail.andersonarmani.screentest;

import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private View mContentView;
    private LinearLayout chromoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        hide();

        mContentView = findViewById(R.id.fullscreen_content);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        chromoLayout = (LinearLayout) findViewById(R.id.chromotherapy);
        chromoLayout.setVisibility(View.GONE);

        ImageButton btnChromo = (ImageButton) findViewById(R.id.btn_chromotherapy);
        btnChromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                chromoLayout.startAnimation(fadeInAnimation);
                chromoLayout.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout buttonsPanel = (LinearLayout) findViewById(R.id.buttonsPanel);
        buttonsPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getVisibility() == View.VISIBLE) {
                    Animation fadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                    chromoLayout.startAnimation(fadeOutAnimation);
                    chromoLayout.setVisibility(View.GONE);
                }
            }
        });


        final MultipleStateButton btnRefrigerator = (MultipleStateButton) findViewById(R.id.btn_wifi);
        btnRefrigerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("### ON CLICK LISTNER");
            }
        });


        MultipleStateButton mbt = (MultipleStateButton) findViewById(R.id.btn_rcd);
        mbt.setEnabled(false);
        mbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleStateButton mbt = (MultipleStateButton)v;
                int state = mbt.getState();
                System.out.println("### STATE = " + state);
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
