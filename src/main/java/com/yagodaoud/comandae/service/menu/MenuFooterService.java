package com.yagodaoud.comandae.service.menu;

import com.yagodaoud.comandae.dto.menu.MenuFooterDTO;
import com.yagodaoud.comandae.model.menu.MenuFooter;
import com.yagodaoud.comandae.repository.menu.MenuFooterRepository;
import com.yagodaoud.comandae.service.ServiceInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuFooterService implements ServiceInterface<MenuFooter, MenuFooterDTO> {

    @Autowired
    private MenuFooterRepository menuFooterRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<MenuFooter> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedMenuFooterFilter");
        filter.setParameter("isDeleted", isDeleted);
        return menuFooterRepository.findAll();    }

    @Override
    public MenuFooter getById(Long id) {
        return menuFooterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rodapé do cardápio não encontrado."));
    }

    @Override
    public MenuFooter create(MenuFooterDTO entity) {
        MenuFooter menuFooter = new MenuFooter();

        menuFooter.setFooter(entity.getFooter());

        return menuFooterRepository.save(menuFooter);
    }

    @Override
    public MenuFooter update(Long id, MenuFooterDTO entity) {
        MenuFooter menuFooter = getById(entity.getId());

        menuFooter.setFooter(entity.getFooter());

        return menuFooterRepository.save(menuFooter);
    }

    @Override
    public void delete(Long id) {
        menuFooterRepository.deleteById(id);
    }
}
