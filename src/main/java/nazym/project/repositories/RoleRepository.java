package nazym.project.repositories;

import nazym.project.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    @Query("SELECT r FROM Role r where r.name='ROLE_USER'")
    Role findRoleUser();
}