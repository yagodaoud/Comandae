package com.yagodaoud.comandae.service.menu;

import com.yagodaoud.comandae.dto.menu.MenuHeaderDTO;
import com.yagodaoud.comandae.model.menu.MenuHeader;
import com.yagodaoud.comandae.repository.menu.MenuHeaderRepository;
import com.yagodaoud.comandae.service.ServiceInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuHeaderService implements ServiceInterface<MenuHeader, MenuHeaderDTO> {

    @Autowired
    private MenuHeaderRepository menuHeaderRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<MenuHeader> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedMenuHeaderFilter");
        filter.setParameter("isDeleted", isDeleted);
        return menuHeaderRepository.findAll();
    }

    @Override
    public MenuHeader getById(Long id) {
        return menuHeaderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cabeçalho não encontrado."));

    }

    @Override
    public MenuHeader create(MenuHeaderDTO entity) {
        MenuHeader menuHeader = new MenuHeader();

        menuHeader.setHeader(entity.getHeader());

        return menuHeaderRepository.save(menuHeader);
    }

    @Override
    public MenuHeader update(Long id, MenuHeaderDTO entity) {
        MenuHeader menuHeader = getById(entity.getId());

        menuHeader.setHeader(entity.getHeader());

        return menuHeaderRepository.save(menuHeader);
    }

    @Override
    public void delete(Long id) {
        menuHeaderRepository.deleteById(id);
    }
}
