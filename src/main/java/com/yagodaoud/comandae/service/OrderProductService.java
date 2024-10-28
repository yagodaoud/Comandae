package com.yagodaoud.comandae.service;

import com.yagodaoud.comandae.dto.OrderDTO;
import com.yagodaoud.comandae.dto.OrderProductDTO;
import com.yagodaoud.comandae.dto.ProductDTO;
import com.yagodaoud.comandae.model.Order;
import com.yagodaoud.comandae.model.OrderProduct;
import com.yagodaoud.comandae.model.Product;
import com.yagodaoud.comandae.repository.OrderProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService implements ServiceInterface<OrderProduct, OrderProductDTO> {

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<OrderProduct> getAll(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedProductFilter");
        filter.setParameter("isDeleted", isDeleted);
        return orderProductRepository.findAll();
    }

    @Override
    public OrderProduct getById(Long id) {
        return orderProductRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido do produto não encontrado,"));
    }

    @Override
    public OrderProduct create(OrderProductDTO orderProductDTO) {
        OrderProduct orderProduct = new OrderProduct();

//        orderProduct.setQuantity(orderProductDTO.getQuantity());
//
//        OrderDTO orderDTO = orderProductDTO.getOrderDTO();
//        Order order = orderService.getById(orderDTO.getId());
//
//        orderProduct.setOrder(order);
//
//        ProductDTO productDTO = orderProductDTO.getProduct();
//        Product product = productService.getById(productDTO.getId());
//
//        orderProduct.setProduct(product);

        return orderProductRepository.save(orderProduct);
    }

    @Override
    public OrderProduct update(Long id, OrderProduct entity) {
        return orderProductRepository.findById(id)
                .map(orderProduct -> {
                    orderProduct.setOrder(entity.getOrder());
                    orderProduct.setProduct(entity.getProduct());
                    orderProduct.setQuantity(entity.getQuantity());
                    return orderProductRepository.save(orderProduct);
                }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        orderProductRepository.deleteById(id);
    }

    public void save(OrderProduct orderProduct) {
        orderProductRepository.save(orderProduct);
    }
}

