package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.CategoryDTO;
import com.yagodaoud.comandae.model.Category;
import com.yagodaoud.comandae.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ServiceInterface<Category, CategoryDTO>  {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Category> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }

    @Override
    public Category create(CategoryDTO entity) {
        Category category = new Category();

        category.setName(entity.getName());
        category.setImage(entity.getImage());

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, CategoryDTO entity) {
        Category category = getById(id);

        category.setName(entity.getName());
        category.setImage(entity.getImage());

        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
