package com.example.rest.controller;

import com.example.rest.dto.PostRequestTo;
import com.example.rest.dto.PostResponseTo;
import com.example.rest.dto.PostUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/messages")
public class PublisherController {

    private final DiscussionClient discussionClient;

    public PublisherController(DiscussionClient discussionClient) {
        this.discussionClient = discussionClient;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseTo>> getAllPosts() {
        List<PostResponseTo> posts = discussionClient.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Создание нового поста
    @PostMapping
    public ResponseEntity<PostResponseTo> createPost(@RequestBody PostRequestTo requestTo) {
        PostResponseTo responseTo = discussionClient.createPost(requestTo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseTo);
    }

    // Получение поста по ID
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseTo> getPostById(@PathVariable Long id) {
        try {
            PostResponseTo responseTo = discussionClient.getPostById(id);
            return ResponseEntity.ok(responseTo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Обновление поста
    @PutMapping
    public ResponseEntity<PostResponseTo> updatePost(
            @RequestBody PostUpdate postUpdate) {

        PostResponseTo responseTo = discussionClient.updatePost(postUpdate);
        return ResponseEntity.ok(responseTo);
    }

    // Удаление поста
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        discussionClient.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}