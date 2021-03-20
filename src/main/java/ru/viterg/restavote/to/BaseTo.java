package ru.viterg.restavote.to;

import ru.viterg.restavote.HasId;

public abstract class BaseTo implements HasId {
    protected Integer id;

    protected BaseTo() {
    }

    protected BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
