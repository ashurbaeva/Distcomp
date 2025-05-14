package by.symonik.issue_service.entity.service.impl;

import by.symonik.issue_service.dto.issue.IssueRequestTo;
import by.symonik.issue_service.dto.issue.IssueResponseTo;
import by.symonik.issue_service.dto.issue.IssueUpdateRequestTo;
import by.symonik.issue_service.entity.Editor;
import by.symonik.issue_service.entity.Issue;
import by.symonik.issue_service.entity.service.IssueService;
import by.symonik.issue_service.exception.EditorNotFoundException;
import by.symonik.issue_service.exception.IssueNotFoundException;
import by.symonik.issue_service.exception.IssueTitleDuplicationException;
import by.symonik.issue_service.mapper.IssueMapper;
import by.symonik.issue_service.repository.EditorRepository;
import by.symonik.issue_service.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final EditorRepository editorRepository;
    private final IssueMapper issueMapper;

    private void checkOnTitleDuplication(String title) {
        Optional<Issue> optionalIssue = issueRepository.findByTitle(title);
        if (optionalIssue.isPresent()) {
            throw IssueTitleDuplicationException.byTitle(title);
        }
    }

    @Override
    @Transactional
    public IssueResponseTo create(IssueRequestTo issueRequestTo) {
        checkOnTitleDuplication(issueRequestTo.title());

        Issue issue = issueMapper.toIssue(issueRequestTo);

        if (issue.getLabels() != null) {
            issue.getLabels().forEach(label -> {
                if (label.getIssues() == null) {
                    label.setIssues(new ArrayList<>());
                }
                label.getIssues().add(issue);
            });
        }

        return issueMapper.toIssueResponseTo(
                issueRepository.save(issue)
        );
    }

    @Override
    @Transactional
    public List<IssueResponseTo> readAll() {
        return issueRepository.findAll()
                .stream()
                .map(issueMapper::toIssueResponseTo)
                .toList();
    }

    @Override
    @Transactional
    public IssueResponseTo readById(Long id) {
        return issueMapper.toIssueResponseTo(
                issueRepository.findById(id).orElseThrow(() -> IssueNotFoundException.byId(id))
        );
    }

    @Override
    @Transactional
    public IssueResponseTo update(IssueUpdateRequestTo issueUpdateRequestTo) {
        long issueId = issueUpdateRequestTo.id();
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> IssueNotFoundException.byId(issueId));

        long editorId = issueUpdateRequestTo.editorId();
        Editor editor = editorRepository.findById(editorId)
                .orElseThrow(() -> EditorNotFoundException.byId(editorId));

        issue.setTitle(issueUpdateRequestTo.title());
        issue.setContent(issueUpdateRequestTo.content());
        issue.setEditor(editor);

        return issueMapper.toIssueResponseTo(issue);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        issueRepository.findById(id)
                .orElseThrow(() -> IssueNotFoundException.byId(id));
        issueRepository.deleteById(id);
    }
}
