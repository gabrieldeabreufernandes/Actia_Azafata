package br.com.actia.mplxlauncher.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.actia.mplxlauncher.data.AppDatabase;
import br.com.actia.mplxlauncher.data.AppsAllowed;

/**
 * Created by Armani andersonaramni@gmail.com on 29/05/2017.
 */

public class InstalledAppsViewModel extends ViewModel {
    private MutableLiveData<List<AppDetail>> appList;
    private Context mContext;
    private AppDatabase mDB;

    private void loadList(final List<String> listBlockedApps){

        new AsyncTask<Void,Void,List<AppDetail>>() {

            @Override
            protected List<AppDetail> doInBackground(Void... params) {
                //List of APPs
                PackageManager manager = mContext.getPackageManager();
                List<AppDetail> apps = new ArrayList<AppDetail>();
                Intent i = new Intent(Intent.ACTION_MAIN, null);
                i.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
                for(ResolveInfo ri:availableActivities){

                    //Jump not Allowed APPs
                    if(listBlockedApps != null && !listBlockedApps.contains(ri.activityInfo.packageName)) {
                        continue;
                    }

                    AppDetail app = new AppDetail(ri.loadLabel(manager), ri.activityInfo.packageName, ri.activityInfo.loadIcon(manager));
                    apps.add(app);
                }

                Collections.sort(apps, new Comparator<AppDetail>() {
                    @Override
                    public int compare(AppDetail app1, AppDetail app2) {
                        String str1 = String.valueOf(app1.getLabel());
                        String str2 = String.valueOf(app2.getLabel());

                        return str1.compareTo(str2);
                    }
                });

                return apps;
            }

            @Override
            protected void onPostExecute(List<AppDetail> appDetails) {
                super.onPostExecute(appDetails);
                appList.setValue(appDetails);
            }
        }.execute();
    }

    public LiveData<List<AppDetail>> getAppList(Context context, List<String> listBlockedApps) {
        mContext = context;

        if(appList == null) {
            appList = new MutableLiveData<>();
            loadList(listBlockedApps);
        }

        return appList;
    }

    /**
     * Save a new APP into the Allowed APPs table
     * @param appsAllowed
     */
    public void saveAllowedApp(AppsAllowed appsAllowed) {
        createDB();

        new AsyncTask<AppsAllowed,Void,Void>() {
            @Override
            protected Void doInBackground(AppsAllowed... params) {
                mDB.appsAllowedDao().insertUsers(params[0]);
                return null;
            }
        }.execute(appsAllowed);
    }

    /**
     * Remove an APP from the Allowed APPs table
     * @param appsAllowed
     */
    public void removeAllowedApp(AppsAllowed appsAllowed) {
        createDB();

        new AsyncTask<AppsAllowed,Void,Void>() {
            @Override
            protected Void doInBackground(AppsAllowed... params) {
                mDB.appsAllowedDao().delete(params[0]);
                return null;
            }
        }.execute(appsAllowed);
    }

    /**
     * Read all Allowed APPs
     * @return
     */
    public LiveData<List<AppsAllowed>> getAllowedApps(Context context) {
        mContext = context;
        createDB();
        return mDB.appsAllowedDao().getAllAppsAllowed();
    }

    public LiveData<List<String>> getAllowedAppsNames(Context context) {
        mContext = context;
        createDB();
        return mDB.appsAllowedDao().getAllAppsAllowedNames();
    }


    private void createDB() {
        if(mDB == null || !mDB.isOpen()) {
            mDB = AppDatabase.getDatabase(mContext.getApplicationContext());
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        /*if(mDB != null && mDB.isOpen()) {
            mDB.close();
            mDB = null;
        }*/
    }
}
