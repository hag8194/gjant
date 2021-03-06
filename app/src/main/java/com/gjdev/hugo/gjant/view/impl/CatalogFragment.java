package com.gjdev.hugo.gjant.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.gjdev.hugo.gjant.R;
import com.gjdev.hugo.gjant.data.api.model.Product;
import com.gjdev.hugo.gjant.view.CatalogView;
import com.gjdev.hugo.gjant.presenter.loader.PresenterFactory;
import com.gjdev.hugo.gjant.presenter.CatalogPresenter;
import com.gjdev.hugo.gjant.injection.AppComponent;
import com.gjdev.hugo.gjant.injection.CatalogViewModule;
import com.gjdev.hugo.gjant.injection.DaggerCatalogViewComponent;
import com.gjdev.hugo.gjant.view.impl.adapter.ProductListAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public final class CatalogFragment extends BaseFragment<CatalogPresenter, CatalogView> implements CatalogView {

    @Inject
    PresenterFactory<CatalogPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.product_list)
    RecyclerView mRecyclerViewProductList;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainActivity mainActivity;
    private ProductListAdapter adapter;

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalog, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mainActivity = (MainActivity)getActivity();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_catalog_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        MaterialSearchView searchView = ButterKnife.findById(getActivity(), R.id.search_view);
        searchView.setMenuItem(searchItem);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                //Log.d(getClass().getName(), query);
                mPresenter.onSubmitQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                //Log.d(getClass().getName(), newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                //mPresenter.onClickSearchMenuItem();
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerCatalogViewComponent.builder()
                .appComponent(parentComponent)
                .catalogViewModule(new CatalogViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<CatalogPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void resetFloatingActionButton() {
        mainActivity.resetFloatingActionButton();
    }

    @Override
    public void setTitle(int resString) {
        mainActivity.mCollapsingToolbarLayout.setTitle(getResources().getString(resString));
    }

    @Override
    public void setAppBarExpanded(boolean expanded) {
        mainActivity.mAppBarLayout.setExpanded(expanded, true);
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.onRefreshRequest();
            }
        });
    }

    @Override
    public void setupRecyclerView(List<Product> products) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        //GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        //StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, 1);
        adapter = new ProductListAdapter(products);

        mRecyclerViewProductList.setHasFixedSize(true);
        mRecyclerViewProductList.setLayoutManager(mLayoutManager);
        mRecyclerViewProductList.setAdapter(adapter);
    }

    @Override
    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

}
