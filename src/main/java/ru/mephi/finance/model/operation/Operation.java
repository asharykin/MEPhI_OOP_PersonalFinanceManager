package ru.mephi.finance.model.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mephi.finance.model.Entity;
import ru.mephi.finance.model.category.Category;

import java.math.BigDecimal;

public abstract class Operation<C extends Category> extends Entity {
    private BigDecimal amount;

    @JsonIgnore
    protected C category;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public C getCategory() {
        return category;
    }

    public void setCategory(C category) {
        this.category = category;
    }

    @JsonProperty("category_id")
    public Integer getCategoryId() {
        return category.getId();
    }

    @JsonProperty("category_id")
    protected abstract void setCategoryStub(Integer id);

    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %12.2f |", getId(), getCategory().getName(), getAmount());
    }
}
