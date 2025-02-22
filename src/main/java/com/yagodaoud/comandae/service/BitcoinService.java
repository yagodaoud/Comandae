package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.BitcoinDTO;
import com.yagodaoud.comandae.model.Bitcoin;
import com.yagodaoud.comandae.model.BitcoinNetwork;
import com.yagodaoud.comandae.repository.BitcoinRepository;
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
public class BitcoinService implements ServiceInterface<Bitcoin, BitcoinDTO> {

    @Autowired
    private BitcoinRepository bitcoinRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Bitcoin> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedBitcoinFilter");
        filter.setParameter("isDeleted", isDeleted);

        return bitcoinRepository.findAll();
    }

    @Override
    public Bitcoin getById(Long id) {
        return bitcoinRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bitcoin not found with ID: " + id));
    }

    @Override
    public Bitcoin create(BitcoinDTO bitcoinDTO) {
        Bitcoin bitcoin = new Bitcoin();
        bitcoin.setNetwork(bitcoinDTO.getNetwork());
        bitcoin.setAddress(bitcoinDTO.getAddress());
        bitcoin.setIsActive(bitcoinDTO.getIsActive());

        return bitcoinRepository.save(bitcoin);
    }

    @Override
    public Bitcoin update(Long id, BitcoinDTO bitcoinDTO) {
        Bitcoin bitcoin = getById(id);

        bitcoin.setNetwork(bitcoinDTO.getNetwork());
        bitcoin.setAddress(bitcoinDTO.getAddress());
        bitcoin.setIsActive(bitcoinDTO.getIsActive());

        return bitcoinRepository.save(bitcoin);
    }

    @Override
    public void delete(Long id) {
        Bitcoin bitcoin = getById(id);
        bitcoin.setDeletedAt(LocalDateTime.now());
        bitcoinRepository.save(bitcoin);
    }

    public List<BitcoinNetwork> getAvailableNetworks() {
        return Arrays.asList(BitcoinNetwork.class.getEnumConstants());
    }

    public String getActiveBitcoinAddress() {
        Bitcoin bitcoin = bitcoinRepository.findByIsActive(true);
        return bitcoin != null ? bitcoin.getAddress() : null;
    }

    public Bitcoin getActiveWallet() {
        return bitcoinRepository.findByIsActive(true);
    }
}
