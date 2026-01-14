package ru.mephi.finance.model.category;

import java.math.BigDecimal;

public class ExpenseCategory extends Category {
    private BigDecimal budget;

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %12.2f |", getId(), getName(), getBudget());
    }
}