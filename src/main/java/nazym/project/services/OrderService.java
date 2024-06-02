package nazym.project.services;

import nazym.project.models.*;
import nazym.project.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired UserService userService;

    public void createOrder(ArrayList<BasketItem> basket)
    {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for(BasketItem basketItem : basket)
        {
            OrderItem orderItem = OrderItem.builder()
                    .product(basketItem.getProduct())
                    .quantity(basketItem.getQuantity())
                    .build();
            orderItems.add(orderItem);
            Product product = basketItem.getProduct();
            product.setQuantity(product.getQuantity()-basketItem.getQuantity());
            productService.updateProduct(product);
            orderItemService.addOrderItem(orderItem);
        }
        Order order = Order.builder()
                .orderItems(orderItems)
                .user(userService.getCurrentUser())
                .orderStatus(OrderStatus.CREATED)
                .dateTime(LocalDateTime.now())
                .build();
        addOrder(order);
        User user = userService.getCurrentUser();
        List<Order> user_orders = user.getOrders();
        user_orders.add(order);
        user.setOrders(user_orders);
        userService.updateUser(user);

    }

    public void addOrder(Order order)
    {
        orderRepository.save(order);
    }

    public List<Order> allOrders()
    {
        return orderRepository.findAll();
    }

    public List<Order> findUserOrders(Long id)
    {
        return orderRepository.findUserOrders(id);
    }
}