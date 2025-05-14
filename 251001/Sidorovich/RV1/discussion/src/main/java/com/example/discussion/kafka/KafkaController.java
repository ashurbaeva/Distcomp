package com.example.discussion.kafka;


import com.example.discussion.CommentRequest;
import com.example.discussion.CommentResponse;
import com.example.discussion.service;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@AllArgsConstructor
public class KafkaController {
    private final service serviceImpl;
    public List<CommentResponse> getAll(){
        return serviceImpl.getAll();
    }
    public CommentResponse getById(Long id){
        return serviceImpl.get(id);
    }
    public CommentResponse create(CommentRequest request){
        return serviceImpl.create(request);
    }
    public void delete(Long id){
        serviceImpl.delete(id);
    }
    public CommentResponse update(CommentRequest request){
        return serviceImpl.update(request);
    }
}