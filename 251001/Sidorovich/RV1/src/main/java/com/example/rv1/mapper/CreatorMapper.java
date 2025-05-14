package com.example.rv1.mapper;

import com.example.rv1.dto.requestDto.CreatorRequestTo;
import com.example.rv1.dto.responseDto.CreatorResponseTo;
import com.example.rv1.entities.Creator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatorMapper {
    CreatorResponseTo from(Creator Creator);
    Creator to(CreatorRequestTo creator);
}