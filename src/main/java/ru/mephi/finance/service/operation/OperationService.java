package ru.mephi.finance.service.operation;

import ru.mephi.finance.model.category.Category;
import ru.mephi.finance.model.operation.Operation;
import ru.mephi.finance.service.EntityService;
import ru.mephi.finance.service.category.CategoryService;

import java.math.BigDecimal;

public abstract class OperationService<O extends Operation<C>, C extends Category> extends EntityService<O> {
    protected CategoryService<C> categoryService;

    protected OperationService() {
        this.typeStr = "operations";
    }

    @Override
    protected void processEntityBeforeImport(O operation) {
        C category = categoryService.getCategoryById(operation.getCategoryId());
        operation.setCategory(category);
    }

    public abstract void createOperation(C category, BigDecimal amount);

    public BigDecimal getSumByCategory(C category) {
        BigDecimal sumByCategory = BigDecimal.ZERO;
        for (O operation : getAllEntities()) {
            if (category.equals(operation.getCategory())) {
                sumByCategory = sumByCategory.add(operation.getAmount());
            }
        }
        return sumByCategory;
    }
}
