package br.com.actia.mplxlauncher.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Armani andersonaramni@gmail.com on 28/06/2017.
 */

@Dao
public interface AppsAllowedDao {
    @Query("SELECT * FROM AppsAllowed")
    LiveData<List<AppsAllowed>> getAllAppsAllowed();

    @Query("SELECT * FROM AppsAllowed WHERE user_id IS (:userId)")
    LiveData<List<AppsAllowed>> getAllAppsByUserId(int userId);

    @Query("SELECT app_name FROM AppsAllowed")
    LiveData<List<String>> getAllAppsAllowedNames();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(AppsAllowed... apps);

    @Delete
    void delete(AppsAllowed app);

    @Update
    void updateUsers(AppsAllowed... apps);
}
