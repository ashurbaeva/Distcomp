package bsuir.khanenko.modulepublisher.mapping;

import bsuir.khanenko.modulepublisher.dto.label.LabelRequestTo;
import bsuir.khanenko.modulepublisher.dto.label.LabelResponseTo;
import bsuir.khanenko.modulepublisher.entity.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    Label toEntity(LabelRequestTo labelRequestTo);
    LabelResponseTo toResponse(Label label);
}
