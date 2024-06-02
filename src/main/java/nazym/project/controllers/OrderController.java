package nazym.project.controllers;

import jakarta.servlet.http.HttpSession;
import nazym.project.models.BasketItem;
import nazym.project.models.Order;
import nazym.project.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController
{
    @Autowired
    private HttpSession httpSession;

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public RedirectView createOrder()
    {
        ArrayList<BasketItem> basket = (ArrayList<BasketItem>) httpSession.getAttribute("basket");
        orderService.createOrder(basket);
        httpSession.setAttribute("basket",new ArrayList<BasketItem>());
        return new RedirectView("/basket?success");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> allOrders()
    {
        return orderService.allOrders();
    }
}