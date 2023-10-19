package com.springboot.blog.service.Imp;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements PostService{

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    public PostServiceImp(PostRepository postRepository,
                          ModelMapper mapper,
                          CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDTO createdPost(PostDTO postDTO) {

        Category category = categoryRepository.findById(postDTO.getCategoryId())
                .orElseThrow(()  -> new ResourceNotFoundException("Category","Id", postDTO.getCategoryId()));

        Post post = mapper.map(postDTO, Post.class);
        post.setCategory(category);
        Post newPost = postRepository.save(post);

        //PostDTO postResponse = map2DTO(newPost);

        return mapper.map(newPost, PostDTO.class);
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id) {
        //get post by id from database
        Post post = postRepository.findById(id).orElseThrow(
                ()  ->  new ResourceNotFoundException("Post", "id" , id)
        );

       Category category = categoryRepository.findById(postDTO.getCategoryId())
                       .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));

        post.setTitle(post.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setContent(postDTO.getContent());
        post.setCategory(category);

        //return map2DTO(postRepository.save(post));
        return mapper.map(postRepository.save(post), PostDTO.class);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post",  "id", id)
        );
        postRepository.delete(post);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort  = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts =  postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDTO> content = posts.stream().map(post -> mapper.map(post, PostDTO.class)).collect(Collectors.toList());

        PostResponse postResponse =  new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public List<PostDTO> getPostByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        List<Post> posts = postRepository.findByCategoryId(categoryId);


        return posts.stream().map((post) -> mapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

}
