package nazym.project.services;

import nazym.project.models.OrderItem;
import nazym.project.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService
{
    @Autowired
    private OrderItemRepository orderItemRepository;

    public void addOrderItem(OrderItem orderItem)
    {
        orderItemRepository.save(orderItem);
    }
}