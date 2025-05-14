package com.alina.publisher.mapper;

import com.alina.publisher.dto.StickerCreateDTO;
import com.alina.publisher.dto.StickerDTO;
import com.alina.publisher.dto.StickerUpdateDTO;
import com.alina.publisher.model.Sticker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        MapperUtil.class
})
public interface StickerMapper {
    @Mapping(target = "tweets", ignore = true)
    @Mapping(target = "id",ignore = true)
    Sticker toSticker(StickerCreateDTO stickerCreateDTO);
    //@Mapping(target = "tweetIds", qualifiedByName = {"MapperUtil", "getTweetIds"},source = "id")
    @Mapping(target = "tweets", ignore = true)
    Sticker toSticker(StickerUpdateDTO stickerUpdateDTO);
    StickerDTO toStickerDTO(Sticker sticker);
    Set<StickerDTO> toStickerDTOSet(Set<Sticker> sticker);
    List<StickerDTO> toStickerDTOList(List<Sticker> sticker);
}
