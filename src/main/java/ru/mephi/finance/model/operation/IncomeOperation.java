package ru.mephi.finance.model.operation;

import ru.mephi.finance.model.category.IncomeCategory;

public class IncomeOperation extends Operation<IncomeCategory> {

    @Override
    public void setCategoryStub(Integer id) {
        IncomeCategory categoryStub = new IncomeCategory();
        categoryStub.setId(id);
        this.category = categoryStub;
    }
}
