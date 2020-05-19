package de.wedemeier.myhome.dbconnect;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import de.wedemeier.myhome.entity.Product;
import de.wedemeier.myhome.entity.User;

@Database(entities = {User.class, Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, "database-name")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
