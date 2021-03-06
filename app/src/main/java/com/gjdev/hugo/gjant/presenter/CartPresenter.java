package com.gjdev.hugo.gjant.presenter;

import com.gjdev.hugo.gjant.data.event.ClickedProductListItem;
import com.gjdev.hugo.gjant.data.event.LongClickedProductListItem;
import com.gjdev.hugo.gjant.data.event.RefreshedList;
import com.gjdev.hugo.gjant.data.event.RemoveCartItem;
import com.gjdev.hugo.gjant.data.sql.event.SuccessCartProductsRetrieve;
import com.gjdev.hugo.gjant.view.CartView;

public interface CartPresenter extends BasePresenter<CartView> {

    void onSuccessCartProductsRetrieve(SuccessCartProductsRetrieve productsRetrieve);

    void onClickedProductListItem(ClickedProductListItem listItem);

    void onLongClickedProductListItem(LongClickedProductListItem listItem);

    void onClickCreateOrderButton();

    void onRemoveCartItem(RemoveCartItem removeCartItem);

    void onRefreshRequest();
}