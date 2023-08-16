package eshop.service;

import eshop.model.Order;
import eshop.model.Product;
import eshop.model.User;
import eshop.util.OrderNotFoundException;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> findAllOrders();

    Order findOrderById(UUID id) throws OrderNotFoundException;

    void saveOrder(Order order);

    Order buildNewOrder(User user, Product product, String address, Integer quantity);

    void deleteOrder(Order order) ;

    List<Order> findPaidOrdersForUser(User user);

    List<Order> findOrdersForBasket(User user);

    void changeOrdersStatus(List<Order> orders);
}
