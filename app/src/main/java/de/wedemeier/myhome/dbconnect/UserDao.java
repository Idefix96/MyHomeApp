package de.wedemeier.myhome.dbconnect;

import androidx.lifecycle.LiveData;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import de.wedemeier.myhome.entity.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Query("SELECT * FROM user WHERE user_name = :username")
    List<User> findByUsername(String username);

    @Insert
    void insertAll(User... users);

    @Query("DELETE FROM user WHERE user_name = :user")
    void delete(String user);

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();
}

