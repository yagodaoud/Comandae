package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.CustomerDTO;
import com.yagodaoud.comandae.model.Customer;
import com.yagodaoud.comandae.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ServiceInterface<Customer, CustomerDTO> {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Customer> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        return customerRepository.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado,"));
    }

    @Override
    public Customer create(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        customer.setName(customerDTO.getName());
        customer.setAddress(customerDTO.getAddress());
        customer.setPhone(customerDTO.getPhone());
        customer.setCpfCnpj(customerDTO.getCpfCnpj());

        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, CustomerDTO customerDTO) {
        Customer customer = getById(id);

        customer.setAddress(customerDTO.getAddress());
        customer.setName(customerDTO.getName());
        customer.setCpfCnpj(customerDTO.getCpfCnpj());
        customer.setPhone(customerDTO.getPhone());

        return customerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}