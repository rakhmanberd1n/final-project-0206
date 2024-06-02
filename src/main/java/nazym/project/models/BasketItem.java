package nazym.project.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketItem
{
    @ManyToOne
    private Product product;

    private int quantity;
}