package nazym.project.repositories;

import nazym.project.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    @Query("SELECT o from Order o where o.user.id = :id")
    List<Order> findUserOrders(Long id);
}