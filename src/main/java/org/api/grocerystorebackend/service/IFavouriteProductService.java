package org.api.grocerystorebackend.service;
import org.api.grocerystorebackend.dto.response.ProductDTO;

import java.util.List;

public interface IFavouriteProductService {
    List<ProductDTO> getFavourites(Long userId);

    void addFavouriteProduct(Long productId, Long userId);

    void deleteFavouriteProduct(Long productId,Long userId);
}
