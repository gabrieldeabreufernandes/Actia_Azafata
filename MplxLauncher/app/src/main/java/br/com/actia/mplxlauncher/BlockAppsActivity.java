package br.com.actia.mplxlauncher;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import br.com.actia.mplxlauncher.Model.AppDetail;
import br.com.actia.mplxlauncher.Model.InstalledAppsViewModel;
import br.com.actia.mplxlauncher.data.AppsAllowed;

public class BlockAppsActivity extends LifecycleActivity {
    private InstalledAppsViewModel  viewModel;
    private List<AppDetail>         mListAllAppDetails;
    private List<AppsAllowed>       mListAllowedApps;
    private List<String>            mListAllowedAppsName;
    private ListView                mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_apps);

        mListAllowedAppsName = new LinkedList<>();

        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.list_item_text);
                boolean state = !checkedTextView.isChecked();
                checkedTextView.setChecked(state);

                if(state) {
                    AppsAllowed newAPP = new AppsAllowed(AppsAllowed.DEFAULT_USER,
                            mListAllAppDetails.get(position).getName().toString());

                    mListAllowedAppsName.add(newAPP.getAppName());
                    viewModel.saveAllowedApp(newAPP);
                }
                else {
                    AppDetail removedApp = mListAllAppDetails.get(position);
                    mListAllowedAppsName.remove(removedApp.getName().toString());

                    for(AppsAllowed app: mListAllowedApps) {
                        if(app.getAppName().equals(removedApp.getName())) {
                            viewModel.removeAllowedApp(app);
                        }
                    }

                }
            }
        });

        //Load blocked APPs list
        /*BlockedAppsFileToObject blockedAppsFileToObject = new BlockedAppsFileToObject();
        mListAllowedApps = blockedAppsFileToObject.getBlockedApps();*/

        viewModel = ViewModelProviders.of(this).get(InstalledAppsViewModel.class);

        viewModel.getAllowedApps(getBaseContext()).observe(this, new Observer<List<AppsAllowed>>() {
            @Override
            public void onChanged(@Nullable List<AppsAllowed> appsAlloweds) {
                mListAllowedApps = appsAlloweds;

                if(mListAllowedApps == null) {
                    return;
                }

                for(AppsAllowed app : mListAllowedApps) {
                    mListAllowedAppsName.add(app.getAppName());
                }
            }
        });

        viewModel.getAppList(getBaseContext(), null).observe(this, new Observer<List<AppDetail>>() {
            @Override
            public void onChanged(@Nullable List<AppDetail> appDetails) {
                mListAllAppDetails = appDetails;

                AppDetailAdapter appDetailArrayAdapter = new AppDetailAdapter(getApplicationContext(), R.layout.list_app_item, mListAllAppDetails);
                mListView.setAdapter(appDetailArrayAdapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*BlockedAppsFileToObject blockedAppsFileToObject = new BlockedAppsFileToObject();
        blockedAppsFileToObject.setBlockedApps(mListAllowedApps);*/

//        viewModel.saveAllowedApps(mListAllowedApps);

        super.onBackPressed();
    }

    /**
     * Adapter for the ListView
     */
    private class AppDetailAdapter extends ArrayAdapter<AppDetail> {

        public AppDetailAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AppDetail> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_app_item, parent, false);
            }

            AppDetail appDetail = getItem(position);

            ImageView imageview = (ImageView) convertView.findViewById(R.id.list_item_image);
            CheckedTextView  textView  = (CheckedTextView) convertView.findViewById(R.id.list_item_text);

            imageview.setImageDrawable(appDetail.getIcon());
            textView.setText(appDetail.getLabel());

            boolean blocked = mListAllowedAppsName.contains(appDetail.getName().toString());
            textView.setChecked(blocked);

            return convertView;
        }
    }
}
