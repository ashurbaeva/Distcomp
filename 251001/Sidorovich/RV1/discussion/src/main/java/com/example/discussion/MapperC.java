package com.example.discussion;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperC {
    CommentResponse from(CommentEntity comment);
    CommentEntity to(CommentRequest comment);
}