package br.com.actia.mplxlauncher.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.com.actia.mplxlauncher.data.AppDatabase;

/**
 * Created by Armani andersonaramni@gmail.com on 13/06/2017.
 */

public class UserDataViewModel extends ViewModel {
    private LiveData<List<AppUser>> usersList = null;
    private Context     context;
    private AppDatabase mDB;

    public LiveData<List<AppUser>> getUserList(Context context) {
        this.context = context;

        if(usersList == null) {
            loadUserApps();
        }

        return usersList;
    }

    public LiveData<List<AppUser>> addUserToList(AppUser appUser) {
        createDB();
        new AsyncTask<AppUser, Void, Void>() {
            @Override
            protected Void doInBackground(AppUser... params) {
                mDB.appUserDao().insertUsers(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadUserApps();
            }
        }.execute(appUser);

        return usersList;
    }

    public LiveData<List<AppUser>> deleteUserList(AppUser appUser) {
        createDB();
        new AsyncTask<AppUser, Void, Void>() {
            @Override
            protected Void doInBackground(AppUser... params) {
                mDB.appUserDao().delete(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadUserApps();
            }
        }.execute(appUser);

        return usersList;
    }

    private void loadUserApps() {
        createDB();
        usersList = mDB.appUserDao().getAllUsers();
    }

    /**
     * Force Reload data
     */
    public void reload() {
        loadUserApps();
    }

    private void createDB() {
        if(mDB == null || !mDB.isOpen()) {
            mDB = AppDatabase.getDatabase(context.getApplicationContext());
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
