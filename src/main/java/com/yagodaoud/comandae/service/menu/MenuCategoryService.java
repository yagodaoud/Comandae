package com.yagodaoud.comandae.service.menu;

import com.yagodaoud.comandae.dto.menu.MenuCategoryDTO;
import com.yagodaoud.comandae.model.menu.MenuCategory;
import com.yagodaoud.comandae.model.menu.MenuItem;
import com.yagodaoud.comandae.repository.menu.MenuCategoryRepository;
import com.yagodaoud.comandae.service.ServiceInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuCategoryService implements ServiceInterface<MenuCategory, MenuCategoryDTO> {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<MenuCategory> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedMenuCategoryFilter");
        filter.setParameter("isDeleted", isDeleted);
        return menuCategoryRepository.findAll();
    }

    @Override
    public MenuCategory getById(Long id) {
        return menuCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoria do Cardápio não encontrada."));
    }

    @Override
    public MenuCategory create(MenuCategoryDTO entity) {
        MenuCategory menuCategory = new MenuCategory();

        menuCategory.setName(entity.getName());
        menuCategory.setDisplayOrder(entity.getDisplayOrder());

        return menuCategoryRepository.save(menuCategory);
    }

        @Override
        public MenuCategory update(Long id, MenuCategoryDTO entity) {
            MenuCategory menuCategory = getById(entity.getId());

            menuCategory.setName(entity.getName());
            menuCategory.setDisplayOrder(entity.getDisplayOrder());

            return menuCategoryRepository.save(menuCategory);
        }

    @Override
    public void delete(Long id) {
        menuCategoryRepository.deleteById(id);
    }
}
