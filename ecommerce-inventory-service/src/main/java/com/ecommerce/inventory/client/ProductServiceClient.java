
package com.ecommerce.inventory.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.ecommerce.inventory.dto.ProductItemResponse;

@Component
public class ProductServiceClient {

    private final RestClient productRestClient;

    public ProductServiceClient(RestClient productRestClient) {
        this.productRestClient = productRestClient;
    }

    public ProductItemResponse getProductItemById(
            String productItemId) {

        try {
            return productRestClient.get()
                    .uri("/product-api/productItemById/{id}", productItemId)
                    .retrieve()
                    .body(ProductItemResponse.class);

        } catch (RestClientException ex) {
            throw new IllegalStateException(
                    "Unable to retrieve product item from Product Service: "
                            + productItemId, ex);
        }
    }
}
