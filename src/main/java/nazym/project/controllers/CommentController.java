package nazym.project.controllers;

import nazym.project.models.Comment;
import nazym.project.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController
{
    @Autowired
    private CommentService commentService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Comment> allComments()
    {
        return commentService.allComments();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteComment(@RequestBody Long id)
    {
        commentService.deleteComment(id);
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public void addComment(@RequestParam(name="comment") String comment,
                           @RequestParam(name="rate") int rate,
                           @RequestParam(name="product_id") Long product_id)
    {
        commentService.addComment(comment,rate,product_id);

    }
}