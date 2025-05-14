package com.example.rv1.mapper;

import com.example.rv1.dto.requestDto.LabelRequestTo;
import com.example.rv1.dto.responseDto.LabelResponseTo;
import com.example.rv1.entities.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelResponseTo from(Label label);
    Label to(LabelRequestTo label);
}