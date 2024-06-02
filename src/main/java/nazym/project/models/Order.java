package nazym.project.models;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ORDERS")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @Column(name = "ORDER_STATUS")
    private OrderStatus orderStatus;

    public double orderTotal() {
        double total = 0;
        if (orderItems == null) return total;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getQuantity() * orderItem.getProduct().getPrice();
        }
        return total;
    }
}