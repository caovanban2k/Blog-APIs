package com.springboot.blog.service.Imp;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImp implements CommentService{

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImp(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this. mapper = mapper;
    }

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comment comment =  mapper.map(commentDTO, Comment.class);

        Post post =  postRepository.findById(postId).orElseThrow(
                () ->  new ResourceNotFoundException("Post", "id", postId)
        );

        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        //return map2DTO(savedComment);
        return mapper.map(savedComment, CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getCommentByPostId(Long postId) {

        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapper.map(comment, CommentDTO.class)).collect(Collectors.toList());

    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {

        Post post =  postRepository.findById(postId).orElseThrow(
                () ->  new ResourceNotFoundException("Post", "id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return  mapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentRequest) {

        Post post =  postRepository.findById(postId).orElseThrow(
                () ->  new ResourceNotFoundException("Post", "id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment.setBody(commentRequest.getBody());
        comment.setEmail(commentRequest.getEmail());
        comment.setName(commentRequest.getName());

        Comment updatedComment = commentRepository.save(comment);

        //return map2DTO(updatedComment);
        return mapper.map(updatedComment, CommentDTO.class);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post =  postRepository.findById(postId).orElseThrow(
                () ->  new ResourceNotFoundException("Post", "id", postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        commentRepository.delete(comment);
    }
}
