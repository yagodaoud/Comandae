package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.PixDTO;
import com.yagodaoud.comandae.model.Pix;
import com.yagodaoud.comandae.model.PixType;
import com.yagodaoud.comandae.repository.PixRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PixService implements ServiceInterface<Pix, PixDTO> {

    @Autowired
    private PixRepository pixRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Pix> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedPixFilter");
        filter.setParameter("isDeleted", isDeleted);


        return pixRepository.findAll();
    }

    @Override
    public Pix getById(Long id) {
        return pixRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pix not found with ID: " + id));
    }

    @Override
    public Pix create(PixDTO pixDTO) {
        Pix pix = new Pix();
        pix.setType(pixDTO.getType());
        pix.setKey(pixDTO.getKey());
        pix.setIsActive(pixDTO.getIsActive());

        return pixRepository.save(pix);
    }

    @Override
    public Pix update(Long id, PixDTO pixDTO) {
        Pix pix = getById(id);

        pix.setType(pixDTO.getType());
        pix.setKey(pixDTO.getKey());
        pix.setIsActive(pixDTO.getIsActive());

        return pixRepository.save(pix);
    }

    @Override
    public void delete(Long id) {
        Pix pix = getById(id);
        pix.setDeletedAt(LocalDateTime.now());
        pixRepository.save(pix);
    }

    public List<PixType> getAvailableTypes() {
        return Arrays.asList(PixType.class.getEnumConstants());
    }
}
