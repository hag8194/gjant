package com.gjdev.hugo.gjant.data.api.event.products;

import com.gjdev.hugo.gjant.data.api.model.Product;

import java.util.List;

/**
 * Created by Hugo on 04/01/2017.
 */

public class SuccessProductsRetrieve {
    private List<Product> productList;

    public SuccessProductsRetrieve(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProducts() {
        return productList;
    }
}
