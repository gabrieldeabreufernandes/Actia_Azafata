package br.com.actia.mplxlauncher.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import br.com.actia.mplxlauncher.data.AppDatabase;
import br.com.actia.mplxlauncher.data.UserApps;

/**
 * Created by Armani andersonaramni@gmail.com on 31/05/2017.
 */

public class UserAppsViewModel extends ViewModel {
    private LiveData<List<UserApps>> appList = null;
    private AppDatabase mDB;
    private Context mContext;

    public UserAppsViewModel() {
    }

    public UserAppsViewModel(Context context) {
        mContext = context;
    }

    public LiveData<List<UserApps>> getAppList(Context context,int userId) {
        this.mContext = context;

        if(appList == null) {
            //appList = new MutableLiveData<>();
            appList = loadUserApps(userId);
        }

        return appList;
    }

    public void addAppList(final UserApps userApp) {
        createDB();
        new AsyncTask<UserApps, Void, Void>() {
            @Override
            protected Void doInBackground(UserApps... params) {
                mDB.userAppsDao().insertUserApps(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                reload(userApp.getUserId());
            }
        }.execute(userApp);
    }

    public void deleteAppList(final UserApps userApp) {
        createDB();

        new AsyncTask<UserApps, Void, Void>() {
            @Override
            protected Void doInBackground(UserApps... params) {
                mDB.userAppsDao().delete(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                reload(userApp.getUid());
            }
        }.execute(userApp);
    }

    private LiveData<List<UserApps>> loadUserApps(int userId) {
        createDB();
        return mDB.userAppsDao().getListAppsName(userId);
    }

    /**
     * Force Reload data
     */
    public void reload(int userId) {
        /*if(userName != null & allAppsList != null)
            loadUserApps(userName, allAppsList);*/

        appList = loadUserApps(userId);
    }

    private void createDB() {
        if(mDB == null || !mDB.isOpen()) {
            mDB = AppDatabase.getDatabase(mContext.getApplicationContext());
        }
    }
}
