package br.com.actia.mplxlauncher.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.actia.mplxlauncher.Model.AppUser;

/**
 * Created by Armani andersonaramni@gmail.com on 28/06/2017.
 */

@Dao
public interface AppUserDao {
    @Query("SELECT * FROM AppUser")
    LiveData<List<AppUser>> getAllUsers();

    @Query("SELECT * FROM AppUser WHERE uid IS (:userId)")
    LiveData<AppUser> getUserById(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(AppUser... users);

    @Delete
    void delete(AppUser user);

    @Update
    void updateUsers(AppUser... users);
}
