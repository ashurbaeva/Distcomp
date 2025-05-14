package com.bsuir.dc.util.mapper;

import com.bsuir.dc.dto.request.EditorRequestTo;
import com.bsuir.dc.dto.response.EditorResponseTo;
import com.bsuir.dc.model.Editor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EditorMapper {
    EditorResponseTo toEditorResponse(Editor editor);
    List<EditorResponseTo> toEditorResponseList(List<Editor> editors);
    Editor toEditor(EditorRequestTo editorRequestTo);
}
