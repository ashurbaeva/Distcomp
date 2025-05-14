package by.symonik.issue_service.mapper;

import by.symonik.issue_service.dto.editor.EditorRequestTo;
import by.symonik.issue_service.dto.editor.EditorResponseTo;
import by.symonik.issue_service.entity.Editor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditorMapper {

    @Mapping(target = "id", ignore = true)
    Editor toEditor(EditorRequestTo editorRequestTo);
    EditorResponseTo toEditorResponseTo(Editor editor);
}
