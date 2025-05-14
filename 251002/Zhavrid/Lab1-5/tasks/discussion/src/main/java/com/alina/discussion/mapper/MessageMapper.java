package com.alina.discussion.mapper;

import com.alina.discussion.dto.MessageCreateDTO;
import com.alina.discussion.dto.MessageDTO;
import com.alina.discussion.dto.MessageUpdateDTO;
import com.alina.discussion.model.Message;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        MapperUtil.class
})
public interface MessageMapper {
    //@Mapping(target = "stickerIds", qualifiedByName = {"MapperUtil", "getStickerIds"},source = "id")
    @Mapping(target = "id",ignore = true)
   // @Mapping(target = "state",ignore = true)
    Message toMessage(MessageCreateDTO messageCreateDTO);
    //@Mapping(target = "stickerIds", qualifiedByName = {"MapperUtil", "getStickerIds"},source = "id")
    //@Mapping(target = "state",ignore = true)
    Message toMessage(MessageUpdateDTO messageUpdateDTO);
    MessageDTO toMessageDTO(Message message);
}
