package bsuir.khanenko.modulepublisher.dto.label;

import jakarta.validation.constraints.Size;

public class LabelUpdate {
    private Long id;
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}