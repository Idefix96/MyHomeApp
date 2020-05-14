package de.wedemeier.myhome.viewmodel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;

import de.wedemeier.myhome.entity.User;
import de.wedemeier.myhome.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    private LiveData<List<User>> allUsers;
    private MutableLiveData<List<User>> searchResults;

    public UserViewModel (Application application) {
        super(application);
        mRepository = new UserRepository(application);
        allUsers = mRepository.getAllUsers();
        searchResults = mRepository.getSearchResults();
    }

    public MutableLiveData<List<User>> getSearchResults() {
        return mRepository.getSearchResults();
    }

    public LiveData<List<User>> getAllUsers() { return allUsers; }

    public void insertProduct(User product) {
        mRepository.insertUser(product);
    }

    public void findUserByName(String name) {
        mRepository.findUser(name);
    }

    public void deleteProduct(String name) {
        mRepository.deleteUser(name);
    }

}