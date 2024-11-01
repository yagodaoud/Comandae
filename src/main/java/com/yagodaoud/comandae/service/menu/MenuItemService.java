package com.yagodaoud.comandae.service.menu;

import com.yagodaoud.comandae.dto.menu.MenuItemDTO;
import com.yagodaoud.comandae.model.menu.MenuCategory;
import com.yagodaoud.comandae.model.menu.MenuItem;
import com.yagodaoud.comandae.repository.menu.MenuItemRepository;
import com.yagodaoud.comandae.service.ServiceInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService implements ServiceInterface<MenuItem, MenuItemDTO> {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuCategoryService menuCategoryService;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<MenuItem> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedMenuItemFilter");
        filter.setParameter("isDeleted", isDeleted);
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado,"));
    }

    @Override
    public MenuItem create(MenuItemDTO entity) {
        MenuItem menuItem = new MenuItem();

        MenuCategory menuCategory = menuCategoryService.getById(entity.getCategoryId());

        menuItem.setEmoji(entity.getEmoji());
        menuItem.setName(entity.getName());
        menuItem.setCategory(menuCategory);

        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem update(Long id, MenuItemDTO entity) {
        MenuItem menuItem = getById(entity.getId());

        MenuCategory menuCategory = menuCategoryService.getById(entity.getCategoryId());

        menuItem.setEmoji(entity.getEmoji());
        menuItem.setName(entity.getName());
        menuItem.setCategory(menuCategory);

        return menuItemRepository.save(menuItem);
    }

    @Override
    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }
}
