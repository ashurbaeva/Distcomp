package bsuir.khanenko.modulepublisher.mapping;

import bsuir.khanenko.modulepublisher.dto.MessageRequestTo;
import bsuir.khanenko.modulepublisher.dto.MessageResponseTo;
import bsuir.khanenko.modulepublisher.entity.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toEntity(MessageRequestTo request);
    MessageResponseTo toResponse(Message message);
    List<MessageResponseTo> toMessageResponseList(Iterable<Message> comments);
}