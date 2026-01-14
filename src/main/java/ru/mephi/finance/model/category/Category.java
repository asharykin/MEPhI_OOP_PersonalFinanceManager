package ru.mephi.finance.model.category;

import ru.mephi.finance.model.Entity;

public abstract class Category extends Entity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
