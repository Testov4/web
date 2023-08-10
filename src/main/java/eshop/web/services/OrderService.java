package eshop.web.services;

import eshop.web.models.Order;
import eshop.web.models.OrderStatus;
import eshop.web.models.Product;
import eshop.web.models.User;
import eshop.web.repositories.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Order findOne(int id){
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Order order){
        orderRepository.save(order);
    }

    //Создание нового заказа
    @Transactional
    public Order newOrder(User user, Product product, String address, int quant){
        Order order = new Order();
        order.setBuyer(user);
        order.setProduct(product);
        order.setAddress(address);
        order.setQuantity(quant);
        order.setFinalPrice(product.getPrice()*quant);
        order.setOrderStatus(OrderStatus.IN_PROCESS);
        return order;
    }

    @Transactional
    public void delete(Order order){
        orderRepository.delete(order);
    }

    public List<Order> findPaidOrdersForUser(User user){
        return orderRepository.findAllByBuyerAndOrderStatusNot(user, OrderStatus.IN_PROCESS);
    }

    public List<Order> findOrdersForBasket(User user){
        return orderRepository.findAllByBuyerAndOrderStatusIs(user, OrderStatus.IN_PROCESS);
    }

}
