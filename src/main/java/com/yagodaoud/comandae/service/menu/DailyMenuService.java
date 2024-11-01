package com.yagodaoud.comandae.service.menu;

import com.yagodaoud.comandae.dto.menu.DailyMenuDTO;
import com.yagodaoud.comandae.model.menu.DailyMenu;
import com.yagodaoud.comandae.repository.menu.DailyMenuRepository;
import com.yagodaoud.comandae.service.ServiceInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyMenuService implements ServiceInterface<DailyMenu, DailyMenuDTO> {

    @Autowired
    private DailyMenuRepository dailyMenuRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<DailyMenu> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedDailyMenuFilter");
        filter.setParameter("isDeleted", isDeleted);
        return dailyMenuRepository.findAll();
    }

    @Override
    public DailyMenu getById(Long id) {
        return dailyMenuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cardápio não encontrado,"));
    }

    @Override
    public DailyMenu create(DailyMenuDTO entity) {
        return null;
    }

    @Override
    public DailyMenu update(Long id, DailyMenuDTO entity) {
        return null;
    }

    @Override
    public void delete(Long id) {
        dailyMenuRepository.deleteById(id);

    }
}
