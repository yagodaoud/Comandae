package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.CustomerDTO;
import com.yagodaoud.comandae.dto.OrderDTO;
import com.yagodaoud.comandae.dto.OrderProductDTO;
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
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado,"));
    }

    @Override
    public Order create(OrderDTO orderDTO) {
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
    public Order update(Long id, Order entity) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setOrderSlipId(entity.getOrderSlipId());
                    order.setCustomer(entity.getCustomer());
                    order.setTotal(entity.getTotal());
                    return orderRepository.save(order);
                }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
