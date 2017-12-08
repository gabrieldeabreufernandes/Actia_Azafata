package br.com.actia.mplxlauncher.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.actia.mplxlauncher.Model.AppUser;

/**
 * Created by Armani andersonaramni@gmail.com on 28/06/2017.
 */
@Database(entities = {AppUser.class, AppsAllowed.class, UserApps.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "Launcher DB";
    private static AppDatabase INSTANCE;

    public abstract AppUserDao appUserDao();
    public abstract AppsAllowedDao appsAllowedDao();
    public abstract UserAppsDao userAppsDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}