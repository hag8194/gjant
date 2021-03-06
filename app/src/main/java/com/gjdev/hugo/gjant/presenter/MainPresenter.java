package com.gjdev.hugo.gjant.presenter;

import com.gjdev.hugo.gjant.data.api.event.createorder.SuccessCreateOrder;
import com.gjdev.hugo.gjant.data.event.NotifyChangeOfFragment;
import com.gjdev.hugo.gjant.view.MainView;

public interface MainPresenter extends BasePresenter<MainView> {

    void onHomeOptionSelected();

    void onCatalogOptionSelected();

    void onOrdersOptionSelected();

    void onCartOptionSelected();

    void onLogoutOptionSelected();

    void onSettingsOptionSelected();

    void onNotifyChangeOfFragment(NotifyChangeOfFragment changeOfFragment);

    void onSuccessCreateOrder(SuccessCreateOrder successCreateOrder);

}