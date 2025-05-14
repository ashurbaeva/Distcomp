package by.symonik.issue_service.mapper;

import by.symonik.issue_service.dto.label.LabelRequestTo;
import by.symonik.issue_service.dto.label.LabelResponseTo;
import by.symonik.issue_service.entity.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    LabelResponseTo toLabelResponseTo(Label label);

//    @Mapping(target = "id", ignore = true)
    Label toLabel(LabelRequestTo labelRequestTo);
}
