package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.ProductDTO;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ServiceInterface<Product, ProductDTO> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Product> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        return productRepository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado,"));
    }

    @Override
    public Product create(ProductDTO productDTO) {
        Product product = new Product();

        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());

        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product entity) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(entity.getName());
                    product.setPrice(entity.getPrice());
                    product.setCategory(entity.getCategory());
                    return productRepository.save(product);
                }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
