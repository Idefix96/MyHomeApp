package de.wedemeier.myhome.dbconnect;

import androidx.lifecycle.LiveData;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import de.wedemeier.myhome.entity.Product;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM Product WHERE id IN (:ProductIds)")
    List<Product> loadAllByIds(int[] ProductIds);

    @Query("SELECT * FROM Product WHERE name LIKE :name LIMIT 1")
    List<Product> findByName(String name);

    @Query("SELECT * FROM Product WHERE category = :category")
    List<Product> findByCategory(String category);

    @Insert
    void insertAll(Product... products);

    @Query("DELETE FROM Product WHERE name = :product")
    void delete(String product);

    @Query("SELECT * FROM Product")
    LiveData<List<Product>> getAllProducts();
}

