package com.springboot.blog.controller;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.Imp.PostService;
import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createdPost(@RequestBody @Valid PostDTO postDTO) {
       return new ResponseEntity<>(postService.createdPost(postDTO), HttpStatus.CREATED);
    }


     @GetMapping
     public PostResponse getAllPosts(
             @RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
             @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
             @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
             @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
     ){
         return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
     }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id){
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRoles('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostDTO  postDTO, @PathVariable("id") Long id){
        return new ResponseEntity<>(postService.updatePost(postDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id){
       postService.deletePost(id);

       return new ResponseEntity<>("Post entity deleted succesfully.", HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDTO>> getPostByCategory(@PathVariable("id") Long categoryId){
        List<PostDTO> post  = postService.getPostByCategory(categoryId);

        return ResponseEntity.ok(post);
    }
}
