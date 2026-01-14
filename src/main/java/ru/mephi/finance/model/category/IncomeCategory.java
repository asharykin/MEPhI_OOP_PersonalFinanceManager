package ru.mephi.finance.model.category;

public class IncomeCategory extends Category {

    @Override
    public String toString() {
        return String.format("| %-5d | %-20s |", getId(), getName());
    }
}
