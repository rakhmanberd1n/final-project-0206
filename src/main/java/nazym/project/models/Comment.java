package nazym.project.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.Name;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="COMMENTS")
@Builder
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable = false)
    private Long id;

    @Column(name="COMMENT")
    private String comment;

    @Column(name="DATE")
    private LocalDate date;

    @Column(name="RATE")
    private int rate;

    @JoinColumn(name="USER_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name="PRODUCT_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
}