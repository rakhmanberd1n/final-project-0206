package nazym.project.repositories;

import nazym.project.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{
    @Override
    @Query("SELECT c FROM Comment c ORDER BY c.id")
    List<Comment> findAll();
}