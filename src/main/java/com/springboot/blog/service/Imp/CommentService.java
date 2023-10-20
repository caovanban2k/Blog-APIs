package com.springboot.blog.service.Imp;

import com.springboot.blog.payload.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment (Long postId, CommentDTO commentDTO);

    List<CommentDTO> getCommentByPostId(Long postId);

    CommentDTO getCommentById(Long postId, Long commentId);

    CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentRequest);

    void deleteComment(Long postId, Long commentId);
}
