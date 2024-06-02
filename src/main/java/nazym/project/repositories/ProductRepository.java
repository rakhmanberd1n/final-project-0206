package nazym.project.repositories;

import nazym.project.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    @Override
    @Query("SELECT p FROM Product p ORDER BY p.id")
    List<Product> findAll();

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(concat('%', :pattern, '%'))")
    List<Product> searchProducts(String pattern);
}