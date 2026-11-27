
package com.ecommerce.cartservice.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.ecommerce.cartservice.dto.ProductItemResponse;

@Component
public class ProductServiceClient {

    private final RestClient productRestClient;

    public ProductServiceClient(
            @Qualifier("productRestClient") RestClient productRestClient) {

        this.productRestClient = productRestClient;
    }

    public ProductItemResponse getProductItem(String productItemId) {

        try {

            ProductItemResponse response = productRestClient
                    .get()
                    .uri("/product-api/productItemById/{id}", productItemId)
                    .retrieve()
                    .body(ProductItemResponse.class);

            if (response == null) {
                throw new IllegalStateException(
                        "Empty response from Product Service for: "
                        + productItemId);
            }

            return response;

        } catch (RestClientResponseException ex) {

            throw new IllegalStateException(
                    "Product Service returned HTTP "
                    + ex.getStatusCode()
                    + " for product item: " + productItemId,
                    ex);

        } catch (RestClientException ex) {

            throw new IllegalStateException(
                    "Cannot connect to Product Service using Eureka. "
                    + "Check whether Product Service is registered and running.",
                    ex);
        }
    }
}
