package de.wedemeier.myhome.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import de.wedemeier.myhome.dbconnect.AppDatabase;
import de.wedemeier.myhome.dbconnect.UserDao;
import de.wedemeier.myhome.entity.User;

public class UserRepository {

    private MutableLiveData<List<User>> searchResults =
            new MutableLiveData<>();
    private LiveData<List<User>> allUsers;
    private UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db;
        db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    private void asyncFinished(List<User> results) {
        searchResults.setValue(results);
    }

    private static class QueryAsyncTask extends
            AsyncTask<String, Void, List<User>> {

        private UserDao asyncTaskDao;
        private UserRepository delegate = null;

        QueryAsyncTask(UserDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<User> doInBackground(final String... params) {
            return asyncTaskDao.findByUsername(params[0]);
        }

        @Override
        protected void onPostExecute(List<User> result) {
            delegate.asyncFinished(result);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void>{

        private UserDao asyncTaskDao;

        InsertAsyncTask(UserDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            asyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {

        private UserDao asyncTaskDao;

        DeleteAsyncTask(UserDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void insertUser(User user) {
        InsertAsyncTask task = new InsertAsyncTask(userDao);
        task.execute(user);
    }

    public void deleteUser(String name) {
        DeleteAsyncTask task = new DeleteAsyncTask(userDao);
        task.execute(name);
    }

    public void findUser(String name) {
        QueryAsyncTask task = new QueryAsyncTask(userDao);
        task.delegate = this;
        task.execute(name);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public MutableLiveData<List<User>> getSearchResults() {
        return searchResults;
    }
}
