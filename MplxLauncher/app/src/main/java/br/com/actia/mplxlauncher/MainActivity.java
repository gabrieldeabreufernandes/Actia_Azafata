package br.com.actia.mplxlauncher;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.actia.mplxlauncher.Model.AppDetail;
import br.com.actia.mplxlauncher.Model.AppUser;
import br.com.actia.mplxlauncher.Model.InstalledAppsViewModel;
import br.com.actia.mplxlauncher.Model.UserAppsViewModel;
import br.com.actia.mplxlauncher.data.UserApps;

public class MainActivity extends LifecycleActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NUM_APPS_SCREEN = 10;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private InstalledAppsViewModel  viewModel;
    private static List<AppDetail>  mListAllAppDetails;
    private UserAppsViewModel       userAppsViewModel;
    private static List<AppDetail>  mListUserAppDetails;
    private static AppUser          currentUser;
    private List<String>            mListAllowedApps;
    private static List<UserApps>          mUserApps;

    private static Context mContext;
    private static PackageManager mPackageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mPackageManager = getPackageManager();

        Bundle bdl = getIntent().getExtras();
        if(bdl != null) {
            currentUser = (AppUser) bdl.get(LockedScreenActivity.USER_CHOOSED);
            Log.v(TAG, "Loged as " + currentUser.getName());
        }

        viewModel = ViewModelProviders.of(this).get(InstalledAppsViewModel.class);

        if(!currentUser.isAdming()) {
            viewModel.getAllowedAppsNames(this).observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> allowedApps) {
                    mListAllowedApps = allowedApps;
                }
            });
        }

        LinearLayout mainContent = (LinearLayout) findViewById(R.id.main_content);
        ImageButton ibtAllApps = (ImageButton) findViewById(R.id.ibt_allApps);
        ImageButton ibtBlockApps = (ImageButton) findViewById(R.id.ibt_blockApps);
        ImageButton ibtUsers = (ImageButton) findViewById(R.id.ibt_users);

        ibtAllApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show new Intent
                Intent it = new Intent(MainActivity.this, AllAppsActivity.class);
                it.putExtra(AllAppsActivity.INTENT_USER, currentUser);
                it.putExtra(AllAppsActivity.INTENT_CLICK_MODE, AllAppsActivity.MODE_LONG_CLICK);
                startActivityForResult(it, 0);
            }
        });


        //IF is ADMIN enable BLOCK APPs functionality
        if(currentUser.isAdming()) {
            ibtBlockApps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Show new Intent
                    Intent it = new Intent(MainActivity.this, BlockAppsActivity.class);
                    startActivityForResult(it, 0);
                }
            });

            ibtUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Show new Intent
                    Intent it = new Intent(MainActivity.this, UsersActivity.class);
                    it.putExtra(UsersActivity.INTENT_USER, currentUser);
                    startActivityForResult(it, 0);
                }
            });
        }
        else {
            ibtBlockApps.setEnabled(false);
            ibtBlockApps.setVisibility(View.INVISIBLE);

            ibtUsers.setEnabled(false);
            ibtUsers.setVisibility(View.INVISIBLE);
        }

        //set current wallpaper
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mainContent.setBackground(wallpaperDrawable);

        reloadScreen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        userAppsViewModel.reload(currentUser.getUid());
    }

    private void reloadScreen() {
        viewModel.getAppList(getBaseContext(), null).observe(this, new Observer<List<AppDetail>>() {
            @Override
            public void onChanged(@Nullable List<AppDetail> appDetails) {
                mListAllAppDetails = appDetails;

                userAppsViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(UserAppsViewModel.class);
                userAppsViewModel.getAppList(mContext, currentUser.getUid()).observe((LifecycleOwner) mContext, new Observer<List<UserApps>>() {
                            @Override
                            public void onChanged(@Nullable List<UserApps> userAppses) {
                                mUserApps = userAppses;

                                if(mListUserAppDetails == null) {
                                    mListUserAppDetails = new ArrayList<>();
                                }
                                else {
                                    mListUserAppDetails.clear();
                                }

                                for(UserApps userApp: mUserApps) {
                                    for(AppDetail appDetail: mListAllAppDetails) {
                                        if(appDetail.getName().equals(userApp.getAppName())) {
                                            if(mListAllowedApps != null) {
                                                if(mListAllowedApps.contains(appDetail.getName().toString())) {
                                                    mListUserAppDetails.add(appDetail);
                                                }
                                            }
                                            else {
                                                mListUserAppDetails.add(appDetail);
                                            }
                                            break;
                                        }
                                    }
                                }

                                Log.v(TAG, "Num of apps = " + String.valueOf(mListUserAppDetails.size()));
                                // Create the adapter that will return a fragment for each of the three
                                // primary sections of the activity.
                                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mListUserAppDetails);

                                // Set up the ViewPager with the sections adapter.
                                mViewPager = (ViewPager) findViewById(R.id.container);
                                mViewPager.setAdapter(mSectionsPagerAdapter);
                            }
                        });
            }
        });
    }

//##################################################################################################
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
        private GridLayout mGridContainer = null;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int totalOfApps) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            int screenNum = getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mGridContainer = (GridLayout) rootView.findViewById(R.id.grid_container);

            createAppCards(screenNum);
            return rootView;
        }

        private void createAppCards(int screenNum) {
            int start = NUM_APPS_SCREEN * (screenNum -1);
            int end = (start + NUM_APPS_SCREEN) > mListUserAppDetails.size() ? mListUserAppDetails.size() : start + NUM_APPS_SCREEN;

            for(int i = start; i < end; i++) {
                View itemView = LayoutInflater.from(getContext())
                        .inflate(R.layout.cardview_app, null, false);

                ImageView imageView = (ImageView) itemView.findViewById(R.id.cardImageID);
                imageView.setImageDrawable(mListUserAppDetails.get(i).getIcon());

                TextView textView = (TextView) itemView.findViewById(R.id.cardTextID);
                textView.setText(mListUserAppDetails.get(i).getLabel());

                mGridContainer.addView(itemView);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            //ADD a button to get new APPs for the main view
            createADDButton();
        }

        private void createADDButton() {
            View itemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.cardview_app, null, false);

            itemView.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));

            ImageView imageView = (ImageView) itemView.findViewById(R.id.cardImageID);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

            TextView textView = (TextView) itemView.findViewById(R.id.cardTextID);
            textView.setText(getString(R.string.new_icon));
            textView.setTextColor(getResources().getColor(R.color.cardview_light_background));

            mGridContainer.addView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Show new Intent
                    Intent it = new Intent(mContext, AllAppsActivity.class);
                    it.putExtra(AllAppsActivity.INTENT_USER, currentUser);
                    it.putExtra(AllAppsActivity.INTENT_CLICK_MODE, AllAppsActivity.MODE_SINGLE_CLICK);
                    startActivityForResult(it, 0);
                }
            });
        }

        @Override
        public void onClick(View view) {
            AppDetail clickedAppDetail = null;
            clickedAppDetail = getClickedApp(view);

            if(clickedAppDetail != null) {
                Intent launchIntent = mPackageManager.getLaunchIntentForPackage(
                        clickedAppDetail.getName().toString());
                if (launchIntent != null) {
                    startActivityForResult(launchIntent, 0);
                }
            }
        }


        @Override
        public boolean onLongClick(final View view) {
            Log.v("ITEM VIEW", "LONG CLICK");
            final AppDetail clickedAppDetail = getClickedApp(view);

            if(clickedAppDetail != null) {

                final AppDetail finalClickedAppDetail = clickedAppDetail;
                new AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.delete_alert_title))
                        .setMessage(getString(R.string.delete_alert_text))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                UserApps removedUserApps = null;
                                for(UserApps userApp: mUserApps) {
                                    if(userApp.getAppName().equals(clickedAppDetail.getName())) {
                                        removedUserApps = userApp;
                                        break;
                                    }
                                }

                                if(removedUserApps != null) {
                                    UserAppsViewModel userAppsViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(UserAppsViewModel.class);
                                    userAppsViewModel.deleteAppList(removedUserApps);

                                    Snackbar.make(view, getString(R.string.delete_alert_ok_text), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            return true;
        }

        private AppDetail getClickedApp(View view) {
            AppDetail clickedAppDetail = null;
            TextView tv = (TextView) view.findViewById(R.id.cardTextID);
            String viewTitle = tv.getText().toString();

            for(AppDetail appDetail: mListUserAppDetails) {
                if(viewTitle.equalsIgnoreCase(appDetail.getLabel().toString())) {
                    clickedAppDetail = appDetail;
                    break;
                }
            }

            return clickedAppDetail;
        }
    }
//##################################################################################################
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<AppDetail> mAppDetails;


        public SectionsPagerAdapter(FragmentManager fm, List<AppDetail> appDetails) {
            super(fm);
            this.mAppDetails = appDetails;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, mAppDetails.size());
        }

        @Override
        public int getCount() {
            int appsQty = mAppDetails.size() + 1; //+1 button AddAPP

            int numScreens =  (appsQty % NUM_APPS_SCREEN) == 0 ? appsQty / NUM_APPS_SCREEN :
                    (appsQty / NUM_APPS_SCREEN) +1;

            return numScreens;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "SECTION " + position;
        }
    }
}