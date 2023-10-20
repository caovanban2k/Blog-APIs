package com.springboot.blog.service.Imp;

import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDTO  createdPost(PostDTO postDTO);

    PostDTO getPostById(Long id);

    PostDTO updatePost(PostDTO postDTO, Long id);

    void deletePost(long id);

    PostResponse getAllPosts(int pageNo, int pageSIze, String sortBy, String sortDir);

    List<PostDTO> getPostByCategory(Long categoryId);
}
