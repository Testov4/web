package eshop.web.service;

import eshop.web.model.Order;
import eshop.web.model.Product;
import eshop.web.model.User;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> FindAllOrders();

    Order FindOrderById(UUID id);

    @Transactional
    void saveOrder(Order order);

    Order createNewOrder(User user, Product product, String address, int quant);

    void deleteOrder(Order order) ;

    List<Order> findPaidOrdersForUser(User user);

    List<Order> findOrdersForBasket(User user);

    void changeOrdersStatus(List<Order> orders);
}
