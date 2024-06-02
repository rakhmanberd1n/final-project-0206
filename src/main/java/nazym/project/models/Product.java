package nazym.project.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="PRODUCTS")
@Builder
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", nullable = false)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="PRICE")
    private Long price;

    @Column(name="QUANTITY")
    private int quantity;

    @Column(name="PICTURE")
    private String picture;

    @JoinColumn(name="CATEGORY_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToMany(cascade = CascadeType.REMOVE)
    private Set<Feature> features;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public String getRating()
    {
        double rating = 0.0;
        if(comments.size()==0) return String.valueOf(rating);
        for(Comment c : comments) rating += c.getRate();
        return String.valueOf(rating / comments.size());

    }
}