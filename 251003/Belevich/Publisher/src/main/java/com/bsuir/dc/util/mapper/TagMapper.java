package com.bsuir.dc.util.mapper;

import com.bsuir.dc.dto.request.TagRequestTo;
import com.bsuir.dc.dto.response.TagResponseTo;
import com.bsuir.dc.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    TagResponseTo toTagResponse(Tag tag);
    List<TagResponseTo> toTagResponseList(List<Tag> tags);
    Tag toTag(TagRequestTo tagRequestTo);
    List<Tag> toTagList(List<TagRequestTo> tagRequestToList);
}