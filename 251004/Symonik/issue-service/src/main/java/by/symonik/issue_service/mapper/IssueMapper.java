package by.symonik.issue_service.mapper;

import by.symonik.issue_service.dto.issue.IssueRequestTo;
import by.symonik.issue_service.dto.issue.IssueResponseTo;
import by.symonik.issue_service.entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IssueMapper {

    @Mapping(target = "editorId", source = "editor.id")
    IssueResponseTo toIssueResponseTo(Issue issue);

    @Mapping(target = "editor.id", source = "editorId")
    Issue toIssue(IssueRequestTo issueRequestTo);
}
