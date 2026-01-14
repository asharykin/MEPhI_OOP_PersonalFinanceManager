package ru.mephi.finance.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.mephi.finance.model.category.ExpenseCategory;
import ru.mephi.finance.repository.category.ExpenseCategoryRepository;
import ru.mephi.finance.repository.operation.ExpenseOperationRepository;

import java.math.BigDecimal;

@Service
@Order(2)
@Qualifier("migratingService")
public class ExpenseCategoryService extends CategoryService<ExpenseCategory> {

    @Autowired
    public ExpenseCategoryService(ExpenseCategoryRepository categoryRepository,
                                  ExpenseOperationRepository operationRepository) {
        this.repository = categoryRepository;
        this.operationRepository = operationRepository;
        this.subtypeStr = "expenses";
    }

    @Override
    public void createCategory(String name, BigDecimal budget) {
        checkCategoryName(name);
        ExpenseCategory category = new ExpenseCategory();
        category.setId(++index);
        category.setName(name);
        category.setBudget(budget);
        repository.save(category);
    }
}
