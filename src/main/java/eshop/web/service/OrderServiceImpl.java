package eshop.web.service;

import eshop.web.model.Order;
import eshop.web.model.OrderStatus;
import eshop.web.model.Product;
import eshop.web.model.User;
import eshop.web.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    @Override
    public List<Order> FindAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order FindOrderById(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order createNewOrder(User user, Product product, String address, int quant) {
        Order order = Order.builder()
            .buyer(user)
            .product(product)
            .address(address)
            .quantity(quant)
            .finalPrice(product.getPrice()*quant)
            .orderStatus(OrderStatus.IN_PROCESS)
            .build();
        return order;
    }

    @Override
    @Transactional
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public List<Order> findPaidOrdersForUser(User user) {
        return orderRepository.findAllByBuyerAndOrderStatusNot(user, OrderStatus.IN_PROCESS);
    }

    @Override
    public List<Order> findOrdersForBasket(User user) {
        return orderRepository.findAllByBuyerAndOrderStatusIs(user, OrderStatus.IN_PROCESS);
    }

    @Override
    @Transactional
    public void changeOrdersStatus(List<Order> orders) {
        orders.forEach(p -> {
            p.setOrderStatus(OrderStatus.ORDERING);
            saveOrder(p);
        });
    }
}
