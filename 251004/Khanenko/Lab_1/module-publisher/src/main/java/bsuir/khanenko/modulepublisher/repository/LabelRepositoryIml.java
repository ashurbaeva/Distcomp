package bsuir.khanenko.modulepublisher.repository;

import bsuir.khanenko.modulepublisher.entity.Label;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LabelRepositoryIml implements LabelRepository {

    private final Map<Long, Label> labels = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Label create(Label label) {
        label.setId(nextId++);
        labels.put(label.getId(), label);
        return label;
    }

    @Override
    public Label update(Label updatedLabel) {
        if (!labels.containsKey(updatedLabel.getId())) {
            throw new IllegalArgumentException("Label with ID " + updatedLabel.getId() + " not found");
        }
        labels.put(updatedLabel.getId(), updatedLabel);
        return updatedLabel;
    }

    @Override
    public void deleteById(Long id) {
        labels.remove(id);
    }

    @Override
    public List<Label> findAll() {
        return labels.values().stream().toList();
    }

    @Override
    public Optional<Label> findById(Long id) {
        return Optional.ofNullable(labels.get(id));
    }
}
