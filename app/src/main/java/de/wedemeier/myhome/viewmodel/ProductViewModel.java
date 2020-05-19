package de.wedemeier.myhome.viewmodel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;

import de.wedemeier.myhome.entity.Product;
import de.wedemeier.myhome.repository.ProductRepository;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository mRepository;
    private LiveData<List<Product>> allProducts;
    private MutableLiveData<List<Product>> searchResults;

    public ProductViewModel (Application application) {
        super(application);
        mRepository = new ProductRepository(application);
        allProducts = mRepository.getAllProducts();
        searchResults = mRepository.getSearchResults();
    }

    public MutableLiveData<List<Product>> getSearchResults() {
        return mRepository.getSearchResults();
    }

    public LiveData<List<Product>> getAllProducts() { return allProducts; }

    public void insertProduct(Product product) {
        mRepository.insertProduct(product);
    }

    public void findProductByName(String name) {
        mRepository.findProduct(name);
    }

    public void deleteProduct(String name) {
        mRepository.deleteProduct(name);
    }

}