package eshop.web.repository;

import eshop.web.model.Order;
import eshop.web.model.OrderStatus;
import eshop.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByBuyerAndOrderStatusNot(User buyer, OrderStatus orderStatus);

    List<Order> findAllByBuyerAndOrderStatusIs(User buyer, OrderStatus orderStatus);
}
