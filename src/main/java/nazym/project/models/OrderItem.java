package nazym.project.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ORDERITEMS")
@Builder
public class OrderItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID",nullable = false)
    private Long id;

    @JoinColumn(name="PRODUCT_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(name="QUANTITY")
    private int quantity;}
