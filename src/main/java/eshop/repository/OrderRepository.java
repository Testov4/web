package eshop.repository;

import eshop.model.Order;
import eshop.util.OrderStatus;
import eshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByBuyerAndOrderStatusNot(User buyer, OrderStatus orderStatus);

    List<Order> findAllByBuyerAndOrderStatusIs(User buyer, OrderStatus orderStatus);
}
