package de.wedemeier.myhome.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import de.wedemeier.myhome.dbconnect.AppDatabase;
import de.wedemeier.myhome.dbconnect.ProductDao;
import de.wedemeier.myhome.entity.Product;

public class ProductRepository {

    private MutableLiveData<List<Product>> searchResults =
            new MutableLiveData<>();
    private LiveData<List<Product>> allProducts;
    private ProductDao ProductDao;

    public ProductRepository(Application application) {
        AppDatabase db;
        db = AppDatabase.getDatabase(application);
        ProductDao = db.productDao();
        allProducts = ProductDao.getAllProducts();
    }

    private void asyncFinished(List<Product> results) {
        searchResults.setValue(results);
    }

    private static class QueryAsyncTask extends
            AsyncTask<String, Void, List<Product>> {

        private ProductDao asyncTaskDao;
        private ProductRepository delegate = null;

        QueryAsyncTask(ProductDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<Product> doInBackground(final String... params) {
            return asyncTaskDao.findByName(params[0]);
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            delegate.asyncFinished(result);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Product, Void, Void>{

        private ProductDao asyncTaskDao;

        InsertAsyncTask(ProductDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            asyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {

        private ProductDao asyncTaskDao;

        DeleteAsyncTask(ProductDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void insertProduct(Product Product) {
        InsertAsyncTask task = new InsertAsyncTask(ProductDao);
        task.execute(Product);
    }

    public void deleteProduct(String name) {
        DeleteAsyncTask task = new DeleteAsyncTask(ProductDao);
        task.execute(name);
    }

    public void findProduct(String name) {
        QueryAsyncTask task = new QueryAsyncTask(ProductDao);
        task.delegate = this;
        task.execute(name);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public MutableLiveData<List<Product>> getSearchResults() {
        return searchResults;
    }
}
