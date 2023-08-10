package eshop.web.repositories;

import eshop.web.models.Order;
import eshop.web.models.OrderStatus;
import eshop.web.models.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findAllByBuyerAndOrderStatusNot(User buyer, OrderStatus orderStatus);


    List<Order> findAllByBuyerAndOrderStatusIs(User buyer, OrderStatus orderStatus);
}
