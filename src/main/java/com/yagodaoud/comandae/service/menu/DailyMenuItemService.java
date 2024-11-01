package com.yagodaoud.comandae.service.menu;

import com.yagodaoud.comandae.dto.menu.DailyMenuItemDTO;
import com.yagodaoud.comandae.model.menu.DailyMenuItem;
import com.yagodaoud.comandae.repository.menu.DailyMenuItemRepository;
import com.yagodaoud.comandae.service.ServiceInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyMenuItemService implements ServiceInterface<DailyMenuItem, DailyMenuItemDTO> {

    @Autowired
    private DailyMenuItemRepository dailyMenuItemRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<DailyMenuItem> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedDailyMenuItemFilter");
        filter.setParameter("isDeleted", isDeleted);
        return dailyMenuItemRepository.findAll();
    }

    @Override
    public DailyMenuItem getById(Long id) {
        return dailyMenuItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item do cardápio diário não encontrado,"));
    }

    @Override
    public DailyMenuItem create(DailyMenuItemDTO entity) {
        DailyMenuItem dailyMenuItem = new DailyMenuItem();

        return dailyMenuItemRepository.save(dailyMenuItem);
    }

    @Override
    public DailyMenuItem update(Long id, DailyMenuItemDTO entity) {
        return null;
    }

    @Override
    public void delete(Long id) {
        dailyMenuItemRepository.deleteById(id);

    }
}
