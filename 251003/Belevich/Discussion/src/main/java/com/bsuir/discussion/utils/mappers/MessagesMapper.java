package com.bsuir.discussion.utils.mappers;

import com.bsuir.discussion.dto.requests.MessageRequestDTO;
import com.bsuir.discussion.dto.responses.MessageResponseDTO;
import com.bsuir.discussion.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessagesMapper {
    @Mapping(target = "id", expression = "java(message.getKey().getId())")
    @Mapping(target = "tweetId", expression = "java(message.getKey().getTweetId())")
    MessageResponseDTO toMessageResponse(Message message);

    List<MessageResponseDTO> toMessageResponseList(Iterable<Message> messages);
    @Mapping(target = "key", expression = "java(new Message.MessageKey(messageRequestDTO.getTweetId()))")
    Message toMessage(MessageRequestDTO messageRequestDTO);
}
