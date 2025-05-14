package by.symonik.issue_service.entity.service;

import by.symonik.issue_service.dto.label.LabelRequestTo;
import by.symonik.issue_service.dto.label.LabelResponseTo;
import by.symonik.issue_service.dto.label.LabelUpdateRequestTo;

import java.util.List;

public interface LabelService {
    LabelResponseTo create(LabelRequestTo labelRequestTo);

    List<LabelResponseTo> readAll();

    LabelResponseTo readById(Long id);

    LabelResponseTo update(LabelUpdateRequestTo labelUpdateRequestTo);

    void deleteById(Long id);
}
