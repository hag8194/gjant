package com.gjdev.hugo.gjant.presenter;

import com.gjdev.hugo.gjant.data.api.event.clientwallet.ErrorClientWalletRetrieve;
import com.gjdev.hugo.gjant.data.api.event.clientwallet.FailClientWalletRetrieve;
import com.gjdev.hugo.gjant.data.api.event.clientwallet.SuccessClientWalletRetrieve;
import com.gjdev.hugo.gjant.data.event.ClickedClientWalletListItem;
import com.gjdev.hugo.gjant.view.SelectClientView;
import com.stepstone.stepper.VerificationError;

public interface SelectClientPresenter extends BasePresenter<SelectClientView> {

    void onSelected();

    void onHasError(VerificationError error);

    void onSuccessClientWalletRetrieve(SuccessClientWalletRetrieve retrieve);

    void onErrorClientWalletRetrieve(ErrorClientWalletRetrieve retrieve);

    void onFailClientWalletRetrieve(FailClientWalletRetrieve retrieve);

    void onClickedClientWalletListItem(ClickedClientWalletListItem listItem);
}