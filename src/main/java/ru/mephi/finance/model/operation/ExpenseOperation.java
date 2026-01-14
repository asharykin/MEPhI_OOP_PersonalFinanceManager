package ru.mephi.finance.model.operation;

import ru.mephi.finance.model.category.ExpenseCategory;

public class ExpenseOperation extends Operation<ExpenseCategory> {

    @Override
    public void setCategoryStub(Integer id) {
        ExpenseCategory categoryStub = new ExpenseCategory();
        categoryStub.setId(id);
        this.category = categoryStub;
    }
}
