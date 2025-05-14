package com.example.rv1.mapper;

import com.example.rv1.dto.requestDto.CommentRequestTo;
import com.example.rv1.dto.responseDto.CommentResponseTo;
import com.example.rv1.entities.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponseTo from(Comment comment);
    Comment to(CommentRequestTo comment);
}