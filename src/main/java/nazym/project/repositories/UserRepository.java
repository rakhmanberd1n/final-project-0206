package nazym.project.repositories;

import nazym.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    @Override
    @Query("SELECT u FROM User u ORDER BY u.id")
    List<User> findAll();

    User findByEmail(String email);
}