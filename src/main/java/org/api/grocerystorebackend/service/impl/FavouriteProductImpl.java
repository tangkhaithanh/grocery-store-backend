package org.api.grocerystorebackend.service.impl;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.FavouriteProduct;
import org.api.grocerystorebackend.mapper.ProductMapper;
import org.api.grocerystorebackend.repository.FavouriteProductRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.repository.UserRepository;
import org.api.grocerystorebackend.service.IFavouriteProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavouriteProductImpl implements IFavouriteProductService {
    @Autowired
    private FavouriteProductRepository favouriteProductRepository;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getFavourites(Long userId) {
        List<FavouriteProduct> favourites = favouriteProductRepository.findByUserId(userId);
        return favourites.stream()
                .map(fav -> productMapper.toDTO(fav.getProduct()))
                .collect(Collectors.toList());
    }

    @Override
    public void addFavouriteProduct(Long productId, Long userId) {
        FavouriteProduct favouriteProduct = new FavouriteProduct();
        favouriteProduct.setUser(userRepository.findById(userId).orElse(null));
        favouriteProduct.setProduct(productRepository.findById(productId).orElse(null));
        favouriteProduct.setCreatedAt(LocalDateTime.now());
        favouriteProductRepository.save(favouriteProduct);
    }

    @Override
    public void deleteFavouriteProduct(Long productId,Long userId) {
        Optional<FavouriteProduct> favouriteOpt = favouriteProductRepository
                .findByUserIdAndProductId(userId, productId);
        favouriteProductRepository.delete(favouriteOpt.get());
    }
}
