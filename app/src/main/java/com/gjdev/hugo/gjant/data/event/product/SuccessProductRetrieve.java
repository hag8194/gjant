package com.gjdev.hugo.gjant.data.event.product;

import com.gjdev.hugo.gjant.data.model.Product;

/**
 * Created by Hugo on 04/01/2017.
 */

public class SuccessProductRetrieve {
    private Product product;

    public SuccessProductRetrieve(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
