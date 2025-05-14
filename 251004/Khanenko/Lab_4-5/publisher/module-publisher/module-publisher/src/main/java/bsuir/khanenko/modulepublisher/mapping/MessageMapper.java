package bsuir.khanenko.modulepublisher.mapping;

import bsuir.khanenko.modulepublisher.dto.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.MessageResponseTo;
import bsuir.khanenko.modulepublisher.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "id", expression = "java(messageRequestTo.getId())")
    @Mapping(target = "articleId", expression = "java(messageRequestTo.getArticleId())")
    @Mapping(target = "content", expression = "java(messageRequestTo.getContent())")
    MessageResponseTo toMessageResponse(MessageRequestTo  messageRequestTo);
}