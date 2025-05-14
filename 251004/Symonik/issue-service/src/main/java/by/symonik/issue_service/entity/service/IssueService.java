package by.symonik.issue_service.entity.service;

import by.symonik.issue_service.dto.issue.IssueRequestTo;
import by.symonik.issue_service.dto.issue.IssueResponseTo;
import by.symonik.issue_service.dto.issue.IssueUpdateRequestTo;

import java.util.List;

public interface IssueService {
    IssueResponseTo create(IssueRequestTo issueRequestTo);

    List<IssueResponseTo> readAll();

    IssueResponseTo readById(Long id);

    IssueResponseTo update(IssueUpdateRequestTo issueUpdateRequestTo);

    void deleteById(Long id);
}
