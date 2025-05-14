package com.bsuir.dc.util.mapper;

import com.bsuir.dc.dto.request.MessageRequestTo;
import com.bsuir.dc.dto.response.MessageResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {
    @Mapping(target = "id", expression = "java(messageRequestTo.getId())")
    @Mapping(target = "tweetId", expression = "java(messageRequestTo.getTweetId())")
    @Mapping(target = "content", expression = "java(messageRequestTo.getContent())")
    MessageResponseTo toMessageResponse(MessageRequestTo messageRequestTo);
}
