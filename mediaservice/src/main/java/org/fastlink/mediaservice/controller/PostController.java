package org.fastlink.mediaservice.controller;


import lombok.AllArgsConstructor;

import org.fastlink.mediaservice.dto.CommentDto;
import org.fastlink.mediaservice.dto.PostDto;
import org.fastlink.mediaservice.dto.response.MediaUploadResponse;

import org.fastlink.mediaservice.mapper.PostMapper;
import org.fastlink.mediaservice.model.*;
import org.fastlink.mediaservice.services.CommentService;
import org.fastlink.mediaservice.services.PostService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

import java.util.*;

import static org.fastlink.mediaservice.model.MediaResourceType.POST;
import static org.fastlink.mediaservice.model.MediaType.PHOTO;
import static org.fastlink.mediaservice.model.MediaType.VIDEO;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController
{


    private final EntityManagerFactory emf;
    private final PostService postService;
    private final CommentService commentService;

    private final HttpServletRequest request;
    private final RestTemplate restTemplate;




    //ALL MAPPING FOR POSTS
    @GetMapping
    public List<Post> getAllPosts()
    {
        return postService.getAllPosts();
    }

/*    @GetMapping("user/{user_id}/posts")
    public List<PostDto> getPostsByAccount(@PathVariable(value = "user_id") Long userId)
    {
        if (!userService.isAccountValid(userId))
        {
            throw new ResponseStatusException(NOT_FOUND, "Account not found ...");
        }
        return postService.getAllMediaForUser(userId);
    }*/

    @PostMapping(value = "/add", consumes = {"*/*"})
    public ResponseEntity<Post> addPostForAccount(@RequestParam("content") String content, @RequestParam("userId") String userId ,@RequestParam(name = "files", required = false) MultipartFile[] files)
    {

        Long id = Long.parseLong(userId);

        Post post = new Post();
        post.setContent(content);
        post.setUserId(id);


        if (files != null)
        {
            List<Media> mediaList = new ArrayList<>();

            for (MultipartFile file : files)
            {
                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("file", file.getResource());
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                //headers.setAccept(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

                ResponseEntity<MediaUploadResponse> response;
                try {
                     response = restTemplate.exchange("http://localhost:9090/api/v1/file/upload?userId={userID}", HttpMethod.POST,requestEntity, MediaUploadResponse.class, userId);
                } catch (Exception e) {
                    throw new RuntimeException("unable to upload file");
                }

                Media media = new Media();
                MediaUploadResponse res = response.getBody();

                assert res != null;
                media.setMediaCode(res.getMediaCode());
                media.setMediaType(Objects.equals(res.getMediaType(), "photo") ? PHOTO : VIDEO);
                media.setDownloadUrl(res.getFileDownloadUri());
                media.setUserId(id);
                media.setMediaResourceType(POST);
                media.setPost(post);
                media.setMimeType(res.getFileType());
                media.setFileName(res.getFileName());


                mediaList.add(media);

            }


            post.setMediaList(mediaList);
        }

        postService.savePost(post);
        return ResponseEntity.ok().body(post);
    }


    @GetMapping("/user/{user_id}")
    public Set<Post> getPostsForUser(@PathVariable("user_id") Long userId)
    {
         return postService.getAllMediaForUser(userId);
    }

    @GetMapping("/{postId}")
    public Post findPostById(@PathVariable(value = "postId") Long postId)
    {
        return postService.findPostById(postId);
    }

    //ALL MAPPING FOR COMMENT
/*    @PutMapping("/posts/{postId}/addComment")
    public ResponseEntity<String> saveCommentForPost(@RequestBody CommentDto commentDto, @PathVariable("postId") Long postId)
    {
        Post post = postService.findPostById(postId).map(PostMapper::convertDtoToEntity)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found ..."));

        Comment comment = CommentMapper.convertDtoToEntity(commentDto);
        comment.setPost(post);
        post.getCommentList().add(comment);
        postService.savePost(post);

        return ResponseEntity.ok().body("Comment saved successfully ...");
    }*/

    @GetMapping("/comments")
    public List<CommentDto> getAllComments()
    {
        return commentService.getAllComments();
    }

    @GetMapping("/{userId}/countPosts")
    public Long countPostsByUserId(@PathVariable(value = "userId") Long userId)
    {
        return postService.countPostsByUserId(userId);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto findCommentById(@PathVariable(value = "commentId") Long commentId)
    {
        return commentService.findCommentById(commentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comment not found ..."));
    }

/*    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsForPost(@PathVariable("postId") Long postId)
    {
        PostDto postDto = postService.findPostById(postId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Post not found ..."));

        return commentService.getCommentsByPostId(postDto.getId());
    }*/

/*    @GetMapping("/posts/{postId}/comments/{commentId}")
    public CommentDto getCommentForPost(@PathVariable(value = "postId") Long postId, @PathVariable(value = "commentId") Long commentId) {
        Optional<PostDto> postDtoOptional = postService.findPostById(postId);
        if (postDtoOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found ...");
        }
        Comment comment = commentService.getCommentForPost(postDtoOptional.get(), commentId);
        return commentMapper.convertEntityToDto(comment);
    }*/



}
