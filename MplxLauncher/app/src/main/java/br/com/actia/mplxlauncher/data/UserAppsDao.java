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
 * Created by Armani andersonaramni@gmail.com on 30/06/2017.
 */
@Dao
public interface UserAppsDao {
    @Query("SELECT * FROM UserApps WHERE user_id IS (:userId)")
    LiveData<List<UserApps>> getListAppsName(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUserApps(UserApps users);

    @Delete
    void delete(UserApps user);

    @Update
    void updateUserApps(UserApps... users);
}
