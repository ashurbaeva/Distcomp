package com.bsuir.dc.dto.response;

import java.io.Serializable;

public class TagResponseTo implements Serializable {
    private long id;
    private String name;

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
}
