package br.com.actia.mplxlauncher;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.actia.mplxlauncher.Model.AppUser;
import br.com.actia.mplxlauncher.Model.UserDataViewModel;
import br.com.actia.mplxlauncher.Service.MainService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LockedScreenActivity extends LifecycleActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    private static final String TAG = LockedScreenActivity.class.getSimpleName();
    private static final String DEFAULT_ADM_PSW = "010203";
    private View mContentView;
    private Button mBtnLogin;
    private Spinner mUserSpinner;
    private TextView mTxtPassword;
    private List<AppUser> userList;
    private UserDataViewModel viewModel;

    public static final String USER_CHOOSED = "USER_CHOSEN";

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locked_screen);

        mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mUserSpinner = (Spinner) findViewById(R.id.spinner_user);
        mTxtPassword = (TextView) findViewById(R.id.txt_password);


        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login pressed");
                //rules to loggin
                checkLoginRules();
            }
        });

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggle();
                hide();
            }
        });

        //Start service
        Intent i = new Intent(LockedScreenActivity.this, MainService.class);
        startService(i);

        viewModel = ViewModelProviders.of(this).get(UserDataViewModel.class);
        viewModel.getUserList(getBaseContext()).observe(this, new Observer<List<AppUser>>() {
            @Override
            public void onChanged(@Nullable List<AppUser> appUsers) {
                userList = appUsers;

                if(userList == null || userList.isEmpty()) {
                    viewModel.addUserToList(new AppUser(getResources().getString(R.string.admin),
                            DEFAULT_ADM_PSW, AppUser.ADMIN));
                }

                if(userList != null && !userList.isEmpty()) {
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<AppUser> adapter = new ArrayAdapter<AppUser>(LockedScreenActivity.this,
                            android.R.layout.simple_spinner_dropdown_item);
                    adapter.addAll(userList);

                    mUserSpinner.setAdapter(adapter);
                }
            }
        });

        //SET Wallpaper
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mContentView.setBackground(wallpaperDrawable);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void checkLoginRules() {
        boolean loginStatus = false;
        AppUser currentUser = (AppUser) mUserSpinner.getSelectedItem();

        for(AppUser user: userList) {
            if(user.getName().equals(currentUser.getName()) &&
                    user.getPassword().equals(mTxtPassword.getText().toString()))
            {
                loginStatus = true;
                mTxtPassword.setText("");

                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                it.putExtra(USER_CHOOSED, currentUser);
                startActivity(it);
                break;
            }
        }

        if(loginStatus == false) {
            Log.d(TAG, "Login error");

            Snackbar.make(mContentView, getString(R.string.login_error), Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Custom action
                    }
                }).show();
        }
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
