package com.leverx.controller;

import com.leverx.error.exception.UserNotFoundException;
import com.leverx.model.Comment;
import com.leverx.model.User;
import com.leverx.service.CommentService;
import com.leverx.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for comment's URL.
 * Check src/main/resources/postman/Dealer_stat.postman_collection.json for more detail URL mapping.
 *
 * @author Andrew Panas
 */

@RestController
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/users/{id}/comments")
    public ResponseEntity<List<Comment>> getAllApprovedCommentsByTraderId(@PathVariable Long id) {
        List<Comment> comments = commentService.findAllApprovedByTraderId(id);
        return comments != null && !comments.isEmpty()
                ? new ResponseEntity<>(comments, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("users/{id}/comments")
    public ResponseEntity<?> saveComment(@RequestBody Comment newComment, @PathVariable Long id) {
        User user = userService.findUserById(id);
        Comment comment = new Comment();
        if (user != null && user.isActive()) {
            comment.setApproved(false);
            comment.setMessage(newComment.getMessage());
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUser(user);
            commentService.save(comment);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else {
            log.debug("In method POST/saveComment: User not found exception");
            throw new UserNotFoundException("User not found");
        }
    }
}
