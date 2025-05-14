package com.example.rv1.services;


import com.example.rv1.dto.responseDto.CommentResponseTo;
import com.example.rv1.dto.requestDto.CommentRequestTo;
import com.example.rv1.entities.Article;
import com.example.rv1.entities.Comment;
import com.example.rv1.kafka.KafkaClient;
import com.example.rv1.kafka.MessageData;
import com.example.rv1.mapper.CommentMapper;
import com.example.rv1.repositories.ArticleRepository;
import com.example.rv1.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class CommentService {

    private final KafkaClient kafkaClient;
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:24130/api/v1.0/comments";

    private final CommentRepository commentRepository;
    //private final LabelRepository repository;
    private final CommentMapper commentMapper;
    private final ArticleRepository articleRepository;

    public List<CommentResponseTo> getAll() {
//        return commentRepository
//                .findAll()
//                .stream()
//                .map(commentMapper::from)
//                .toList();


        //ResponseEntity<CommentResponseTo[]> response = restTemplate.getForEntity(BASE_URL, CommentResponseTo[].class);
        //CommentResponseTo[] comments = response.getBody();
        //return comments != null ? Arrays.asList(comments) : List.of();

        try {
            return kafkaClient.send(new MessageData(MessageData.Operation.GET_ALL)).responseTOs();
        }catch (TimeoutException e) {
            ResponseEntity<CommentResponseTo[]> response = restTemplate.getForEntity(BASE_URL, CommentResponseTo[].class);
            CommentResponseTo[] comments = response.getBody();
            return comments != null ? Arrays.asList(comments) : List.of();
        }
    }
    @Cacheable(value = "comments", key = "#id")
    public CommentResponseTo get(long id) {
//        return commentRepository
//                .findById(id)
//                .map(commentMapper::from)
//                .orElse(null);

        //return restTemplate.getForObject(BASE_URL + "/" + id, CommentResponseTo.class);

        try {
            return kafkaClient.send(new MessageData(MessageData.Operation.GET_BY_ID, id)).responseTOs().get(0);
        }catch (TimeoutException e) {
            return restTemplate.getForObject(BASE_URL + "/" + id, CommentResponseTo.class);

        }
    }
    @CachePut(value = "comments", key = "#input.id")
    public CommentResponseTo create(CommentRequestTo input) {

        if (!articleRepository.findById(input.getArticleId()).isPresent()) {
            throw new NoSuchElementException("Creator with ID " + input.getArticleId() + " not found");
        }

       // Comment comment = commentMapper.to(input);
        //Comment savedComment = commentRepository.save(comment);
//        return commentMapper.from(savedComment);


      //  return restTemplate.postForObject(BASE_URL, input, CommentResponseTo.class);

        try {
            var output  = kafkaClient.send(new MessageData(MessageData.Operation.CREATE, input)).responseTOs().get(0);
            input.setId(output.getId());
            return output;
        }catch (TimeoutException e) {
            var output = restTemplate.postForObject(BASE_URL, input, CommentResponseTo.class);
            input.setId(output.getId());
            return output;
        }
    }
    @CachePut(value = "comments", key = "#input.id")
    public CommentResponseTo update(CommentRequestTo input) {

//
//        Comment existingComment = commentRepository.findById(input.getId()).orElse(null);
//
//
//        assert existingComment != null;
//        existingComment.setArticleId(input.getArticleId());
//        existingComment.setContent(input.getContent());
//        Comment updatedComment = commentRepository.save(existingComment);
//
//        return commentMapper.from(updatedComment);



//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<CommentRequestTo> requestEntity = new HttpEntity<>(input, headers);
//
//        ResponseEntity<CommentResponseTo> response = restTemplate.exchange(
//                BASE_URL,
//                HttpMethod.PUT,
//                requestEntity,
//                new ParameterizedTypeReference<CommentResponseTo>() {}
//        );
//        return response.getBody();

        try {
            var output =  kafkaClient.send(new MessageData(MessageData.Operation.UPDATE, input)).responseTOs().get(0);
            input.setId(output.getId());
            return output;
        }catch (TimeoutException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CommentRequestTo> requestEntity = new HttpEntity<>(input, headers);

            ResponseEntity<CommentResponseTo> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.PUT,
                    requestEntity,
                    new ParameterizedTypeReference<CommentResponseTo>() {}
            );
            var output = response.getBody();
            input.setId(output.getId());
            return output;
        }
    }
    @CacheEvict(value = "comments", key = "#id")
    public boolean delete(long id) {
//        //return commentRepository.delete(id);
//        if (commentRepository.existsById(id)) {
//            commentRepository.deleteById(id);
//            return true;
//        } else {
//            return false;
//        }



//        try {
//            restTemplate.delete(BASE_URL + "/" + id);
//            return true;
//        } catch (HttpClientErrorException.NotFound ex) {
//            return false;
//        }

        try {
            kafkaClient.send(new MessageData(MessageData.Operation.DELETE_BY_ID, id));
            return true;
        }catch (TimeoutException e) {
            try {
                restTemplate.delete(BASE_URL + "/" + id);
                return true;
            } catch (HttpClientErrorException.NotFound ex) {
                return false;
            }

        }
    }
}