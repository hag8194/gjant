package com.gjdev.hugo.gjant.interactor.impl;

import javax.inject.Inject;

import com.gjdev.hugo.gjant.R;
import com.gjdev.hugo.gjant.data.api.ApiService;
import com.gjdev.hugo.gjant.data.api.event.products.ErrorProductsRetrieve;
import com.gjdev.hugo.gjant.data.api.event.products.FailProductsRetrieve;
import com.gjdev.hugo.gjant.data.api.event.products.SuccessProductsRetrieve;
import com.gjdev.hugo.gjant.data.api.model.Tags;
import com.gjdev.hugo.gjant.data.event.SelectedProduct;
import com.gjdev.hugo.gjant.data.api.model.ApiError;
import com.gjdev.hugo.gjant.data.api.model.Product;
import com.gjdev.hugo.gjant.data.api.model.User;
import com.gjdev.hugo.gjant.data.sql.model.DaoSession;
import com.gjdev.hugo.gjant.interactor.CatalogInteractor;
import com.gjdev.hugo.gjant.util.ApiErrorHandler;
import com.gjdev.hugo.gjant.util.InternalStorageHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class CatalogInteractorImpl implements CatalogInteractor {
    private ApiService mApiService;
    private ApiErrorHandler mApiErrorHandler;
    private InternalStorageHandler mInternalStorageHandler;

    private List<Product> productList;
    private List<Product> filteredList;

    @Inject
    public CatalogInteractorImpl(ApiService apiService, ApiErrorHandler apiErrorHandler,
                                 InternalStorageHandler internalStorageHandler) {
        mApiService = apiService;
        mApiErrorHandler = apiErrorHandler;
        mInternalStorageHandler = internalStorageHandler;
        filteredList = new ArrayList<>();
    }

    @Override
    public void retrieveProducts(boolean refresh) {
        User user = (User)mInternalStorageHandler.readObject(R.string.user_data);

        Call<List<Product>> productsCall = mApiService.products(user.getAccessToken());

        if(productList == null || refresh) {
            productsCall.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if(response.isSuccessful()) {
                        productList = response.body();
                        postEvent(SUCCESS_EVENT, null);
                    }
                    else
                        postEvent(ERROR_EVENT, mApiErrorHandler.parseError(response));
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    postEvent(FAILURE_EVENT, t);
                }
            });
        }
        else
            postEvent(SUCCESS_EVENT, null);
    }

    @Override
    public Product getProduct(int position) {
        return productList.get(position);
    }

    @Override
    public void postSelectedProduct(int id) {
        EventBus.getDefault().postSticky(new SelectedProduct(id));
    }

    @Override
    public List<Product> search(String query) {
        String cad = "";

        if(!filteredList.isEmpty())
            filteredList.clear();

        for (Product product : productList) {
            cad = product.getName() + " " + product.getBrand().getName() + " " +
                    String.valueOf(product.getPrice()) + " " + product.getDescription();
            if(cad.matches("(?i).*" + query + ".*"))
                filteredList.add(product);

            for (Tags tag : product.getTags()) {
                if(tag.getName().matches("(?i).*" + query + ".*") && !filteredList.contains(product))
                    filteredList.add(product);
            }
        }

        return filteredList;
    }

    @Override
    public void postEvent(int kindOfEvent, Object object) {
        EventBus eventBus = EventBus.getDefault();
        switch (kindOfEvent) {
            case FAILURE_EVENT:
                eventBus.post(new FailProductsRetrieve((Throwable) object));
                break;
            case SUCCESS_EVENT:
                eventBus.post(new SuccessProductsRetrieve(productList));
                break;
            case ERROR_EVENT:
                eventBus.post(new ErrorProductsRetrieve((ApiError) object));
                break;
        }
    }
}