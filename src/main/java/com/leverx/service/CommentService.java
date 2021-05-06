package com.leverx.service;

import java.util.List;

import com.leverx.model.Comment;

/**
 * Interface of comment service
 *
 * @author Andrew Panas
 */

public interface CommentService {
    void save(Comment comment);
    boolean deleteById(Long id);
    boolean update(Comment comment);
    List<Comment> findAllNotApproved();
    List<Comment> findAllApprovedByTraderId(Long id);
    Comment findById(Long id);
}
