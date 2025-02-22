package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.CustomerDTO;
import com.yagodaoud.comandae.dto.OrderDTO;
import com.yagodaoud.comandae.exception.OrderSlipInUseException;
import com.yagodaoud.comandae.model.Customer;
import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.model.OrderProduct;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements ServiceInterface<Order, OrderDTO> {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Order> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        return orderRepository.findAll();
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));
    }

    @Override
    public Order create(OrderDTO orderDTO) {
        if (orderRepository.existsByOrderSlipIdAndActiveIsTrue(orderDTO.getOrderSlipId())) {
            throw new OrderSlipInUseException(orderDTO.getOrderSlipId());
        }

        Order order = new Order();

        order.setOrderSlipId(orderDTO.getOrderSlipId());
        order.setTotal(orderDTO.getTotal());

        CustomerDTO customerDTO = orderDTO.getCustomer();

        if (Optional.ofNullable(customerDTO).isPresent()) {

            Customer retrievedCustomer = customerService.getById(customerDTO.getId());

            order.setCustomer(retrievedCustomer);
        }

        Order savedOrder = orderRepository.save(order);

        orderDTO.getProducts().forEach(productDTO -> {
            OrderProduct orderProduct = new OrderProduct();

            Product retrievedProduct = productService.getById(productDTO.getId());

            orderProduct.setProduct(retrievedProduct);
            orderProduct.setQuantity(productDTO.getQuantity());
            orderProduct.setOrder(savedOrder);

            orderProductService.save(orderProduct);
        });

        return savedOrder;
    }

    @Override
    public Order update(Long id, OrderDTO orderDTO) {
        Order order = getById(id);

        if (orderRepository.existsByOrderSlipIdAndActiveIsTrue(orderDTO.getOrderSlipId())) {
            throw new OrderSlipInUseException(orderDTO.getOrderSlipId());
        }

        order.setPaymentType(orderDTO.getPaymentType());
        order.setOrderSlipId(orderDTO.getOrderSlipId());
        order.setTotal(orderDTO.getTotal());

        if (orderDTO.getCustomer() != null) {
            Customer customer = customerService.getById(orderDTO.getCustomer().getId());
            order.setCustomer(customer);
        }

        return orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public Order findByOrderSlipId(int orderSlipId) {
        return orderRepository.findByOrderSlipIdAndActiveTrue(orderSlipId).orElseThrow(() -> new EntityNotFoundException("Comanda não encontrada."));
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findByActiveTrue() {
        return orderRepository.findByActiveIsTrueOrderByCreatedAtDesc();
    }

    public List<Order> findByActiveFalse() {
        return orderRepository.findByActiveIsFalseOrderByCreatedAtDesc();
    }
}
