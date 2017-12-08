package br.com.actia.mplxlauncher;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.actia.mplxlauncher.Model.AppDetail;
import br.com.actia.mplxlauncher.Model.AppUser;
import br.com.actia.mplxlauncher.Model.InstalledAppsViewModel;
import br.com.actia.mplxlauncher.Model.UserAppsViewModel;
import br.com.actia.mplxlauncher.data.UserApps;

/**
 * Created by Armani andersonaramni@gmail.com on 01/06/2017.
 */

public class AllAppsActivity extends LifecycleActivity {
    public static final String INTENT_USER = "USER";
    public static final String INTENT_CLICK_MODE = "CLICK_MODE";
    public static final boolean MODE_SINGLE_CLICK = true;
    public static final boolean MODE_LONG_CLICK = false;

    private InstalledAppsViewModel viewModel;
    private static List<AppDetail> mListAllAppDetails;
    private Context                 mContext;
    private AppUser                 mCurrentUser;
    private boolean                 isModeSingleClick = false;
    private List<String>            mListBlockedApps = null;
    private GridView                gridView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_apps);
        mContext = this;

        Bundle bdl = getIntent().getExtras();
        if(bdl != null) {
            mCurrentUser = (AppUser) bdl.get(INTENT_USER);
            isModeSingleClick = bdl.getBoolean(INTENT_CLICK_MODE, MODE_LONG_CLICK);
            mListAllAppDetails = new ArrayList<>();
        }

        viewModel = ViewModelProviders.of(this).get(InstalledAppsViewModel.class);

        //Load all apps for Admin
        if(mCurrentUser.isAdming()) {
            callGetAppList(null);
        }
        else {
            //Load blocked APPs list for Operator user
            viewModel.getAllowedAppsNames(this).observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(@Nullable List<String> allowedApps) {
                    mListBlockedApps = allowedApps;
                    callGetAppList(mListBlockedApps);
                }
            });
        }
    }

    private void callGetAppList(List allowedAppsList) {
        viewModel.getAppList(getBaseContext(), allowedAppsList).observe(AllAppsActivity.this,
                new Observer<List<AppDetail>>() {
                    @Override
                    public void onChanged(@Nullable List<AppDetail> appDetails) {
                        mListAllAppDetails = appDetails;

                        setGridViewAdapter();
                    }
                });
    }

    private void setGridViewAdapter() {
        gridView = (GridView) findViewById(R.id.allAppsGridview);
        gridView.setAdapter(new AppsDetailAdapter());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isModeSingleClick) {
                    addAppToMainView(position);
                }
                else {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(
                            mListAllAppDetails.get(position).getName().toString());
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    }
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return addAppToMainView(position);
            }
        });
    }

    private boolean addAppToMainView(int position) {
        final int index = position;

        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.addapp_alert_title))
                .setMessage(getString(R.string.addapp_alert_text))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        AppDetail appSelected = mListAllAppDetails.get(index);

                        UserAppsViewModel userAppsViewModel = new UserAppsViewModel(mContext);
                        userAppsViewModel.addAppList(new UserApps(mCurrentUser.getUid(), appSelected.getName().toString()));

                        Snackbar.make(gridView, getString(R.string.addapp_alert_ok_text), Snackbar.LENGTH_LONG).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

        return true;
    }

    /**
     * Adapter to show APPs as Images in a GridView
     */
    private class AppsDetailAdapter extends BaseAdapter {

        public AppsDetailAdapter() {

        }

        @Override
        public int getCount() {
            return mListAllAppDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return mListAllAppDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.cardview_app, null, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.cardImageID);
            imageView.setImageDrawable(mListAllAppDetails.get(position).getIcon());

            TextView textView = (TextView) itemView.findViewById(R.id.cardTextID);
            textView.setText(mListAllAppDetails.get(position).getLabel());

            return itemView;
        }
    }
}
