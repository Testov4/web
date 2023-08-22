package eshop.service.implementation;

import eshop.model.Order;
import eshop.util.OrderStatus;
import eshop.model.Product;
import eshop.model.User;
import eshop.repository.OrderRepository;
import eshop.service.OrderService;
import eshop.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order findOrderById(UUID id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order buildOrder(User user, Product product, String address, Integer quantity) {
        return Order.builder()
            .buyer(user)
            .product(product)
            .address(address)
            .quantity(quantity)
            .finalPrice(calculateFinalPrice(product, quantity))
            .orderStatus(OrderStatus.IN_PROCESS)
            .build();
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
    public void changeOrdersStatusAndSave(List<Order> orders) {
        orders.forEach(p -> {
            p.setOrderStatus(OrderStatus.ORDERING);
            saveOrder(p);
        });
    }

    private Long calculateFinalPrice(Product product ,Integer quantity) {
        return product.getPrice() * quantity;
    }
}
