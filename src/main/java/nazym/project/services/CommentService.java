package nazym.project.services;

import nazym.project.models.Comment;
import nazym.project.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService
{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public List<Comment> allComments()
    {
        return commentRepository.findAll();
    }

    public Comment getComment(Long id)
    {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteComment(Long id)
    {
        commentRepository.deleteById(id);
    }

    public void addComment(String comment, int rate, Long product_id)
    {
        Comment comment1 = Comment.builder()
                .comment(comment)
                .user(userService.getCurrentUser())
                .rate(rate)
                .date(LocalDate.from(LocalDateTime.now()))
                .product(productService.findProduct(product_id))
                .build();
        commentRepository.save(comment1);
    }
}